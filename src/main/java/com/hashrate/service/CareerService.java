package com.hashrate.service;

import com.hashrate.model.Career;
import com.hashrate.model.Career.Department;
import com.hashrate.repository.CareerRepository;
import com.hashrate.util.SeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CareerService {
    
    private final CareerRepository careerRepository;
    private final SeoUtils seoUtils;
    private final SeoService seoService;
    
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
        return careerRepository.findDistinctLocations();
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
    
    private String generateJobId(Career career) {
        String baseId = seoUtils.generateSlug(career.getTitle());
        return "JOB-" + baseId.toUpperCase() + "-" + System.currentTimeMillis();
    }
}