package com.hashrate.service;

import com.hashrate.controller.CareerController;
import com.hashrate.dto.CareerApplicationDTO;
import com.hashrate.model.Career;
import com.hashrate.model.Career.Department;
import com.hashrate.repository.CareerRepository;
import com.hashrate.service.EmailService;
import com.hashrate.util.SeoUtils;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
public class CareerService {
	
	private static final Logger log = LoggerFactory.getLogger(CareerController.class);
	
	
    private final CareerRepository careerRepository;
    private final SeoUtils seoUtils;
    private final SeoService seoService;
    private final EmailService emailService;

    @Value("${app.upload.dir:${user.home}/uploads}")
    private String uploadDir;

    @Value("${app.upload.max-file-size:10485760}") // 10MB
    private long maxFileSize;

    @Autowired
    public CareerService(CareerRepository careerRepository, 
                        SeoUtils seoUtils, 
                        SeoService seoService, 
                        EmailService emailService) {
        this.careerRepository = careerRepository;
        this.seoUtils = seoUtils;
        this.seoService = seoService;
        this.emailService = emailService;
    }

    @Cacheable(value = "careers", key = "'all-active'")
    public Page<Career> findAllActive(Pageable pageable) {
        log.debug("Finding all active careers with pagination");
        return careerRepository.findByIsActiveTrueAndExpiryDateAfter(LocalDateTime.now(), pageable);
    }

    @Cacheable(value = "careers", key = "#department + '-' + #pageable.pageNumber")
    public Page<Career> findByDepartment(Department department, Pageable pageable) {
        log.debug("Finding careers by department: {} with pagination", department);
        return careerRepository.findByDepartmentAndIsActiveTrueAndExpiryDateAfter(department, LocalDateTime.now(), pageable);
    }

    @Cacheable(value = "careers", key = "#location + '-' + #pageable.pageNumber")
    public Page<Career> findByLocation(String location, Pageable pageable) {
        log.debug("Finding careers by location: {} with pagination", location);
        return careerRepository.findByLocationAndIsActiveTrueAndExpiryDateAfter(location, LocalDateTime.now(), pageable);
    }

    @Cacheable(value = "careers", key = "'locations'")
    public List<String> findAllLocations() {
        log.debug("Finding all active career locations");
        return careerRepository.findDistinctLocations(LocalDateTime.now());
    }

    public long countUrgent() {
        return careerRepository.countByIsUrgentTrueAndIsActiveTrueAndExpiryDateAfter(LocalDateTime.now());
    }

    public long countActive() {
        return careerRepository.countByIsActiveTrueAndExpiryDateAfter(LocalDateTime.now());
    }

    @Cacheable(value = "careers", key = "'active-not-expired'")
    public List<Career> findActiveNotExpired() {
        log.debug("Finding active careers that are not expired");
        return careerRepository.findByIsActiveTrueAndExpiryDateAfter(LocalDateTime.now());
    }

    @Cacheable(value = "careers", key = "'urgent'")
    public List<Career> findUrgent() {
        log.debug("Finding urgent active careers");
        return careerRepository.findByIsUrgentTrueAndIsActiveTrueAndExpiryDateAfter(LocalDateTime.now());
    }

    public Optional<Career> findByJobId(String jobId) {
        return careerRepository.findByJobIdAndIsActiveTrueAndExpiryDateAfter(jobId, LocalDateTime.now());
    }

    @Transactional
    public Career save(Career career) {
        log.info("Saving career: {}", career.getTitle());

        // Generate job ID if not provided
        if (career.getJobId() == null || career.getJobId().isEmpty()) {
            career.setJobId(generateJobId(career));
        }

        // Generate SEO metadata if not provided
        if (career.getSeoMetadata() == null) {
            career.setSeoMetadata(seoService.generateCareerSeoMetadata(career));
        }

        return careerRepository.save(career);
    }

    @Transactional
    public void applyForCareer(String jobId, CareerApplicationDTO applicationForm) throws IOException {
        log.info("Processing career application for job ID: {}", jobId);
        
        // Validate the career exists
        Career career = findByJobId(jobId)
                .orElseThrow(() -> new RuntimeException("Career not found with job ID: " + jobId));
        
        // Validate file upload
        MultipartFile resume = applicationForm.getResume();
        if (resume == null || resume.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }
        
        // Validate file size
        if (resume.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of " + (maxFileSize / 1024 / 1024) + "MB");
        }
        
        // Validate file type
        String contentType = resume.getContentType();
        if (contentType == null || (!contentType.equals("application/pdf") && 
            !contentType.equals("application/msword") && 
            !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new IllegalArgumentException("Only PDF, DOC, and DOCX files are allowed");
        }
        
        try {
            // Save the resume file
            String resumePath = saveResumeFile(resume, applicationForm);
            
            // Send notification email
            emailService.sendCareerApplicationEmail(career, applicationForm, resumePath);
            
            log.info("Career application processed successfully for job ID: {}", jobId);
            
        } catch (IOException e) {
            log.error("Error processing career application for job ID: {}", jobId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error processing career application for job ID: {}", jobId, e);
            throw new IOException("Failed to process application", e);
        }
    }

    private String saveResumeFile(MultipartFile file, CareerApplicationDTO application) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir, "resumes");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = application.getFirstName() + "_" + application.getLastName() + "_" + 
                         UUID.randomUUID().toString() + fileExtension;
        
        // Save file
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        log.debug("Resume file saved: {}", filePath.toString());
        return filePath.toString();
    }

    private String generateJobId(Career career) {
        String baseId = seoUtils.generateSlug(career.getTitle());
        return "JOB-" + baseId.toUpperCase() + "-" + System.currentTimeMillis();
    }
}