package com.hashrate.service;

import com.hashrate.model.Project;
import com.hashrate.model.Project.ProjectCategory;
import com.hashrate.repository.ProjectRepository;
import com.hashrate.util.SeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final SeoUtils seoUtils;
    private final SeoService seoService;
    
    @Cacheable(value = "projects", key = "'all-active'")
    public Page<Project> findAllActive(Pageable pageable) {
        log.debug("Finding all active projects with pagination");
        return projectRepository.findByIsActiveTrue(pageable);
    }
    
    @Cacheable(value = "projects", key = "#category + '-' + #pageable.pageNumber")
    public Page<Project> findByCategory(ProjectCategory category, Pageable pageable) {
        log.debug("Finding projects by category: {} with pagination", category);
        return projectRepository.findByCategoryAndIsActiveTrue(category, pageable);
    }
    
    @Cacheable(value = "projects", key = "'clients'")
    public List<String> findAllClients() {
        log.debug("Finding all distinct clients");
        return projectRepository.findDistinctClients();
    }
    
    @Cacheable(value = "projects", key = "'featured'")
    public List<Project> findFeatured() {
        log.debug("Finding featured projects");
        return projectRepository.findByIsActiveTrueAndIsFeaturedTrue();
    }
    
    public long countActive() {
        return projectRepository.countByIsActiveTrue();
    }
    
    public long countByCategory(ProjectCategory category) {
        return projectRepository.countByCategoryAndIsActiveTrue(category);
    }
    
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }
    
    @Transactional
    public Project save(Project project) {
        log.info("Saving project: {}", project.getProjectName());
        
        // Generate SEO metadata if not provided
        if (project.getSeoMetadata() == null) {
            project.setSeoMetadata(seoService.generateProjectSeoMetadata(project));
        }
        
        return projectRepository.save(project);
    }
}