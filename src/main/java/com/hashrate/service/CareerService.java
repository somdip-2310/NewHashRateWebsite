package com.hashrate.service;

import com.hashrate.dto.CareerApplicationDTO;
import com.hashrate.model.Career;
import com.hashrate.model.Career.Department;
import com.hashrate.model.Career.ExperienceLevel;
import com.hashrate.model.Career.JobType;
import com.hashrate.repository.CareerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CareerService {
    
    private final CareerRepository careerRepository;
    private final EmailService emailService;
    private final SeoService seoService;
    
    private static final String RESUME_UPLOAD_DIR = "uploads/resumes/";
    
    @Cacheable(value = "careers", key = "#jobId")
    public Optional<Career> findByJobId(String jobId) {
        log.debug("Finding career by job ID: {}", jobId);
        return careerRepository.findByJobId(jobId);
    }
    
    @Cacheable(value = "careers", key = "'all-active'")
    public List<Career> findAllActive() {
        log.debug("Finding all active careers");
        return careerRepository.findByIsActiveTrue();
    }
    
    @Cacheable(value = "careers", key = "'urgent'")
    public List<Career> findUrgent() {
        log.debug("Finding urgent careers");
        return careerRepository.findByIsActiveTrueAndIsUrgentTrue();
    }
    
    @Cacheable(value = "careers", key = "#department")
    public List<Career> findByDepartment(Department department) {
        log.debug("Finding careers by department: {}", department);
        return careerRepository.findByDepartmentAndIsActiveTrue(department);
    }
    
    @Cacheable(value = "careers", key = "#jobType")
    public List<Career> findByJobType(JobType jobType) {
        log.debug("Finding careers by job type: {}", jobType);
        return careerRepository.findByJobTypeAndIsActiveTrue(jobType);
    }
    
    @Cacheable(value = "careers", key = "#experienceLevel")
    public List<Career> findByExperienceLevel(ExperienceLevel experienceLevel) {
        log.debug("Finding careers by experience level: {}", experienceLevel);
        return careerRepository.findByExperienceLevelAndIsActiveTrue(experienceLevel);
    }
    
    @Cacheable(value = "careers", key = "#location")
    public List<Career> findByLocation(String location) {
        log.debug("Finding careers by location: {}", location);
        return careerRepository.findByLocation(location);
    }
    
    public Page<Career> findAllActive(Pageable pageable) {
        log.debug("Finding all active careers with pagination");
        return careerRepository.findAllActiveOrderByUrgencyAndDate(pageable);
    }
    
    public Page<Career> searchCareers(String keyword, Pageable pageable) {
        log.debug("Searching careers with keyword: {}", keyword);
        return careerRepository.searchCareers(keyword, pageable);
    }
    
    @Cacheable(value = "career-locations")
    public List<String> findAllLocations() {
        log.debug("Finding all active career locations");
        return careerRepository.findAllActiveLocations();
    }
    
    public List<Career> findByExperienceRange(Integer experience) {
        log.debug("Finding careers for experience: {} years", experience);
        return careerRepository.findByExperienceRange(experience);
    }
    
    public List<Career> findActiveNotExpired() {
        log.debug("Finding active careers not expired");
        return careerRepository.findByIsActiveTrueAndClosingDateAfter(LocalDateTime.now());
    }
    
    @Transactional
    public void applyForCareer(String jobId, CareerApplicationDTO applicationDTO) throws IOException {
        log.info("Processing career application for job: {}", jobId);
        
        Career career = careerRepository.findByJobId(jobId)
                .orElseThrow(() -> new RuntimeException("Career not found with job ID: " + jobId));
        
        // Save resume file
        String resumePath = saveResume(applicationDTO.getResume());
        
        // Send email notification
        emailService.sendCareerApplicationEmail(career, applicationDTO, resumePath);
        
        // You might want to save the application to a database table
        // For now, we're just sending the email
        
        log.info("Career application processed successfully for job: {}", jobId);
    }
    
    private String saveResume(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(RESUME_UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        
        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);
        
        return filePath.toString();
    }
    
    @Transactional
    @CacheEvict(value = {"careers", "career-locations"}, allEntries = true)
    public Career save(Career career) {
        log.info("Saving career: {}", career.getTitle());
        
        // Generate job ID if not provided
        if (career.getJobId() == null || career.getJobId().isEmpty()) {
            career.setJobId(generateJobId(career));
        }
        
        // Set posted date if not provided
        if (career.getPostedDate() == null) {
            career.setPostedDate(LocalDateTime.now());
        }
        
        // Generate SEO metadata if not provided
        if (career.getSeoMetadata() == null) {
            career.setSeoMetadata(seoService.generateCareerSeoMetadata(career));
        }
        
        return careerRepository.save(career);
    }
    
    @Transactional
    @CacheEvict(value = {"careers", "career-locations"}, allEntries = true)
    public Career update(Long id, Career career) {
        log.info("Updating career with id: {}", id);
        
        Career existingCareer = careerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Career not found with id: " + id));
        
        // Update fields
        existingCareer.setTitle(career.getTitle());
        existingCareer.setDepartment(career.getDepartment());
        existingCareer.setLocation(career.getLocation());
        existingCareer.setJobType(career.getJobType());
        existingCareer.setExperienceLevel(career.getExperienceLevel());
        existingCareer.setMinExperience(career.getMinExperience());
        existingCareer.setMaxExperience(career.getMaxExperience());
        existingCareer.setDescription(career.getDescription());
        existingCareer.setResponsibilities(career.getResponsibilities());
        existingCareer.setRequirements(career.getRequirements());
        existingCareer.setSkills(career.getSkills());
        existingCareer.setSalaryRange(career.getSalaryRange());
        existingCareer.setActive(career.isActive());
        existingCareer.setUrgent(career.isUrgent());
        existingCareer.setClosingDate(career.getClosingDate());
        
        // Update SEO metadata
        if (career.getSeoMetadata() != null) {
            existingCareer.setSeoMetadata(career.getSeoMetadata());
        }
        
        return careerRepository.save(existingCareer);
    }
    
    @Transactional
    @CacheEvict(value = {"careers", "career-locations"}, allEntries = true)
    public void delete(Long id) {
        log.info("Deleting career with id: {}", id);
        careerRepository.deleteById(id);
    }
    
    private String generateJobId(Career career) {
        String prefix = career.getDepartment().name().substring(0, 3);
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        return prefix + "-" + timestamp;
    }
    
    public long countActive() {
        return careerRepository.countByIsActiveTrue();
    }
    
    public long countUrgent() {
        return careerRepository.countByIsActiveTrueAndIsUrgentTrue();
    }
    
    public long countByDepartment(Department department) {
        return careerRepository.countByDepartmentAndIsActiveTrue(department);
    }
}