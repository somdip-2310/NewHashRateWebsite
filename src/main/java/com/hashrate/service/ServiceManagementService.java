package com.hashrate.service;

import com.hashrate.model.Service;
import com.hashrate.model.Service.ServiceCategory;
import com.hashrate.repository.ServiceRepository;
import com.hashrate.util.SeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ServiceManagementService {
    
    private final ServiceRepository serviceRepository;
    private final SeoUtils seoUtils;
    private final SeoService seoService;
    
    @Cacheable(value = "services", key = "#slug")
    public Optional<Service> findBySlug(String slug) {
        log.debug("Finding service by slug: {}", slug);
        return serviceRepository.findBySlug(slug);
    }
    
    @Cacheable(value = "services", key = "'all-active'")
    public List<Service> findAllActive() {
        log.debug("Finding all active services");
        return serviceRepository.findAllActiveOrderByDisplayOrder();
    }
    
    @Cacheable(value = "services", key = "'featured'")
    public List<Service> findFeatured() {
        log.debug("Finding featured services");
        return serviceRepository.findByIsActiveTrueAndIsFeaturedTrue();
    }
    
    @Cacheable(value = "services", key = "#category")
    public List<Service> findByCategory(ServiceCategory category) {
        log.debug("Finding services by category: {}", category);
        return serviceRepository.findByCategoryAndIsActiveTrue(category);
    }
    
    public Page<Service> findAllActive(Pageable pageable) {
        log.debug("Finding all active services with pagination");
        return serviceRepository.findByIsActiveTrue(pageable);
    }
    
    public Page<Service> searchServices(String keyword, Pageable pageable) {
        log.debug("Searching services with keyword: {}", keyword);
        return serviceRepository.searchServices(keyword, pageable);
    }
    
    @Cacheable(value = "services", key = "'top-featured-' + #limit")
    public List<Service> findTopFeatured(int limit) {
        log.debug("Finding top {} featured services", limit);
        return serviceRepository.findTopFeaturedServices(limit);
    }
    
    @Cacheable(value = "service-categories")
    public List<ServiceCategory> findAllCategories() {
        log.debug("Finding all active service categories");
        return serviceRepository.findAllActiveCategories();
    }
    
    @Transactional
    @CacheEvict(value = {"services", "service-categories"}, allEntries = true)
    public Service save(Service service) {
        log.info("Saving service: {}", service.getName());
        
        // Generate slug if not provided
        if (service.getSlug() == null || service.getSlug().isEmpty()) {
            service.setSlug(seoUtils.generateSlug(service.getName()));
        }
        
        // Generate SEO metadata if not provided
        if (service.getSeoMetadata() == null) {
            service.setSeoMetadata(seoService.generateServiceSeoMetadata(service));
        }
        
        return serviceRepository.save(service);
    }
    
    @Transactional
    @CacheEvict(value = {"services", "service-categories"}, allEntries = true)
    public Service update(Long id, Service service) {
        log.info("Updating service with id: {}", id);
        
        Service existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        // Update fields
        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());
        existingService.setShortDescription(service.getShortDescription());
        existingService.setCategory(service.getCategory());
        existingService.setIconClass(service.getIconClass());
        existingService.setImageUrl(service.getImageUrl());
        existingService.setDeliverables(service.getDeliverables());
        existingService.setTechnologies(service.getTechnologies());
        existingService.setDisplayOrder(service.getDisplayOrder());
        existingService.setActive(service.isActive());
        existingService.setFeatured(service.isFeatured());
        
        // Update SEO metadata
        if (service.getSeoMetadata() != null) {
            existingService.setSeoMetadata(service.getSeoMetadata());
        }
        
        return serviceRepository.save(existingService);
    }
    
    @Transactional
    @CacheEvict(value = {"services", "service-categories"}, allEntries = true)
    public void delete(Long id) {
        log.info("Deleting service with id: {}", id);
        serviceRepository.deleteById(id);
    }
    
    public long countActive() {
        return serviceRepository.countByIsActiveTrue();
    }
}