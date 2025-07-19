package com.hashrate.service;

import com.hashrate.model.Solution;
import com.hashrate.model.Solution.SolutionType;
import com.hashrate.repository.SolutionRepository;
import com.hashrate.util.SeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SolutionService {
    
    private final SolutionRepository solutionRepository;
    private final SeoUtils seoUtils;
    private final SeoService seoService;
    
    @Cacheable(value = "solutions", key = "#slug")
    public Optional<Solution> findBySlug(String slug) {
        log.debug("Finding solution by slug: {}", slug);
        return solutionRepository.findBySlug(slug);
    }
    
    @Cacheable(value = "solutions", key = "'all-active'")
    public List<Solution> findAllActive() {
        log.debug("Finding all active solutions");
        return solutionRepository.findAllActiveOrderByDisplayOrder();
    }
    
    @Cacheable(value = "solutions", key = "'featured'")
    public List<Solution> findFeatured() {
        log.debug("Finding featured solutions");
        return solutionRepository.findByIsActiveTrueAndIsFeaturedTrue();
    }
    
    @Cacheable(value = "solutions", key = "#type")
    public List<Solution> findByType(SolutionType type) {
        log.debug("Finding solutions by type: {}", type);
        return solutionRepository.findByTypeAndIsActiveTrue(type);
    }
    
    public Page<Solution> findAllActive(Pageable pageable) {
        log.debug("Finding all active solutions with pagination");
        return solutionRepository.findByIsActiveTrue(pageable);
    }
    
    public Page<Solution> searchSolutions(String keyword, Pageable pageable) {
        log.debug("Searching solutions with keyword: {}", keyword);
        return solutionRepository.searchSolutions(keyword, pageable);
    }
    
    @Cacheable(value = "solutions", key = "'top-featured-' + #limit")
    public List<Solution> findTopFeatured(int limit) {
        log.debug("Finding top {} featured solutions", limit);
        return solutionRepository.findTopFeaturedSolutions(limit);
    }
    
    @Transactional
    @CacheEvict(value = "solutions", allEntries = true)
    public Solution save(Solution solution) {
        log.info("Saving solution: {}", solution.getTitle());
        
        // Generate slug if not provided
        if (solution.getSlug() == null || solution.getSlug().isEmpty()) {
            solution.setSlug(seoUtils.generateSlug(solution.getTitle()));
        }
        
        // Generate SEO metadata if not provided
        if (solution.getSeoMetadata() == null) {
            solution.setSeoMetadata(seoService.generateSolutionSeoMetadata(solution));
        }
        
        return solutionRepository.save(solution);
    }
    
    @Transactional
    @CacheEvict(value = "solutions", allEntries = true)
    public Solution update(Long id, Solution solution) {
        log.info("Updating solution with id: {}", id);
        
        Solution existingSolution = solutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solution not found with id: " + id));
        
        // Update fields
        existingSolution.setTitle(solution.getTitle());
        existingSolution.setDescription(solution.getDescription());
        existingSolution.setShortDescription(solution.getShortDescription());
        existingSolution.setType(solution.getType());
        existingSolution.setIconClass(solution.getIconClass());
        existingSolution.setImageUrl(solution.getImageUrl());
        existingSolution.setBannerImageUrl(solution.getBannerImageUrl());
        existingSolution.setFeatures(solution.getFeatures());
        existingSolution.setBenefits(solution.getBenefits());
        existingSolution.setUseCases(solution.getUseCases());
        existingSolution.setArchitectureDetails(solution.getArchitectureDetails());
        existingSolution.setDisplayOrder(solution.getDisplayOrder());
        existingSolution.setActive(solution.isActive());
        existingSolution.setFeatured(solution.isFeatured());
        
        // Update SEO metadata
        if (solution.getSeoMetadata() != null) {
            existingSolution.setSeoMetadata(solution.getSeoMetadata());
        }
        
        return solutionRepository.save(existingSolution);
    }
    
    @Transactional
    @CacheEvict(value = "solutions", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting solution with id: {}", id);
        solutionRepository.deleteById(id);
    }
    
    public long countActive() {
        return solutionRepository.countByIsActiveTrue();
    }
    
    public long countByType(SolutionType type) {
        return solutionRepository.countByTypeAndIsActiveTrue(type);
    }
}