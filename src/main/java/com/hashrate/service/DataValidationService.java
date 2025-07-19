package com.hashrate.service;

import com.hashrate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Order(20) // Execute after data initialization
public class DataValidationService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataValidationService.class);

    @Autowired private ProductRepository productRepository;
    @Autowired private SolutionRepository solutionRepository;
    @Autowired private CareerRepository careerRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private ProjectRepository projectRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("🔍 Starting data validation and integrity checks...");
        
        try {
            validateProducts();
            validateSolutions();
            validateCareers();
            validateServices();
            validateProjects();
            
            log.info("✅ Data validation completed successfully");
        } catch (Exception e) {
            log.error("❌ Data validation failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void validateProducts() {
        log.info("🔍 Validating products...");
        
        AtomicInteger issues = new AtomicInteger(0);
        
        // Check for duplicate slugs
        List<String> duplicateSlugs = productRepository.findAll().stream()
            .collect(java.util.stream.Collectors.groupingBy(
                p -> p.getSlug(),
                java.util.stream.Collectors.counting()
            ))
            .entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(entry -> entry.getKey())
            .toList();
        
        if (!duplicateSlugs.isEmpty()) {
            log.warn("⚠️ Found duplicate product slugs: {}", duplicateSlugs);
            issues.incrementAndGet();
        }
        
        // Check for products without categories
        long productsWithoutCategory = productRepository.findAll().stream()
            .filter(p -> p.getCategory() == null)
            .count();
        
        if (productsWithoutCategory > 0) {
            log.warn("⚠️ Found {} products without category", productsWithoutCategory);
            issues.incrementAndGet();
        }
        
        log.info("✅ Product validation completed - {} issues found", issues.get());
    }

    private void validateSolutions() {
        log.info("🔍 Validating solutions...");
        
        AtomicInteger issues = new AtomicInteger(0);
        
        // Check for duplicate slugs
        List<String> duplicateSlugs = solutionRepository.findAll().stream()
            .collect(java.util.stream.Collectors.groupingBy(
                s -> s.getSlug(),
                java.util.stream.Collectors.counting()
            ))
            .entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(entry -> entry.getKey())
            .toList();
        
        if (!duplicateSlugs.isEmpty()) {
            log.warn("⚠️ Found duplicate solution slugs: {}", duplicateSlugs);
            issues.incrementAndGet();
        }
        
        log.info("✅ Solution validation completed - {} issues found", issues.get());
    }

    private void validateCareers() {
        log.info("🔍 Validating careers...");
        
        AtomicInteger issues = new AtomicInteger(0);
        
        // Check for expired careers that are still active
        long expiredActiveCareers = careerRepository.findAll().stream()
            .filter(c -> c.isActive() && c.getExpiryDate() != null && 
                        c.getExpiryDate().isBefore(LocalDateTime.now()))
            .count();
        
        if (expiredActiveCareers > 0) {
            log.warn("⚠️ Found {} expired careers that are still active", expiredActiveCareers);
            issues.incrementAndGet();
        }
        
        // Check for duplicate job IDs
        List<String> duplicateJobIds = careerRepository.findAll().stream()
            .filter(c -> c.getJobId() != null)
            .collect(java.util.stream.Collectors.groupingBy(
                c -> c.getJobId(),
                java.util.stream.Collectors.counting()
            ))
            .entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(entry -> entry.getKey())
            .toList();
        
        if (!duplicateJobIds.isEmpty()) {
            log.warn("⚠️ Found duplicate job IDs: {}", duplicateJobIds);
            issues.incrementAndGet();
        }
        
        log.info("✅ Career validation completed - {} issues found", issues.get());
    }

    private void validateServices() {
        log.info("🔍 Validating services...");
        
        AtomicInteger issues = new AtomicInteger(0);
        
        // Check for duplicate slugs
        List<String> duplicateSlugs = serviceRepository.findAll().stream()
            .collect(java.util.stream.Collectors.groupingBy(
                s -> s.getSlug(),
                java.util.stream.Collectors.counting()
            ))
            .entrySet().stream()
            .filter(entry -> entry.getValue() > 1)
            .map(entry -> entry.getKey())
            .toList();
        
        if (!duplicateSlugs.isEmpty()) {
            log.warn("⚠️ Found duplicate service slugs: {}", duplicateSlugs);
            issues.incrementAndGet();
        }
        
        log.info("✅ Service validation completed - {} issues found", issues.get());
    }

    private void validateProjects() {
        log.info("🔍 Validating projects...");
        
        AtomicInteger issues = new AtomicInteger(0);
        
        // Check for projects without client names
        long projectsWithoutClient = projectRepository.findAll().stream()
            .filter(p -> p.getClientName() == null || p.getClientName().trim().isEmpty())
            .count();
        
        if (projectsWithoutClient > 0) {
            log.warn("⚠️ Found {} projects without client names", projectsWithoutClient);
            issues.incrementAndGet();
        }
        
        log.info("✅ Project validation completed - {} issues found", issues.get());
    }
}