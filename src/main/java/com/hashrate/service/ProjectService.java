package com.hashrate.service;

import com.hashrate.model.Project;
import com.hashrate.model.Project.ProjectCategory;
import com.hashrate.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProjectService {
    
	private static final Logger log = LoggerFactory.getLogger(ProjectService.class);
    
    private final ProjectRepository projectRepository;
    private final SeoService seoService;
    
    @Cacheable(value = "projects", key = "'all-active'")
    public List<Project> findAllActive() {
        log.debug("Finding all active projects");
        return projectRepository.findAllActiveOrderByDisplayOrderAndDate();
    }
    
    @Cacheable(value = "projects", key = "'featured'")
    public List<Project> findFeatured() {
        log.debug("Finding featured projects");
        return projectRepository.findByIsActiveTrueAndIsFeaturedTrue();
    }
    
    @Cacheable(value = "projects", key = "#category")
    public List<Project> findByCategory(ProjectCategory category) {
        log.debug("Finding projects by category: {}", category);
        return projectRepository.findByCategoryAndIsActiveTrue(category);
    }
    
    public Page<Project> findAllActive(Pageable pageable) {
        log.debug("Finding all active projects with pagination");
        return projectRepository.findByIsActiveTrue(pageable);
    }
    
    public Page<Project> findByCategory(ProjectCategory category, Pageable pageable) {
        log.debug("Finding projects by category with pagination: {}", category);
        return projectRepository.findByCategoryAndIsActiveTrue(category, pageable);
    }
    
    public Page<Project> searchProjects(String keyword, Pageable pageable) {
        log.debug("Searching projects with keyword: {}", keyword);
        return projectRepository.searchProjects(keyword, pageable);
    }
    
    @Cacheable(value = "projects", key = "'top-featured-' + #limit")
    public List<Project> findTopFeatured(int limit) {
        log.debug("Finding top {} featured projects", limit);
        return projectRepository.findTopFeaturedProjects(limit);
    }
    
    @Cacheable(value = "projects", key = "'clients'")
    public List<String> findAllClients() {
        log.debug("Finding all active client names");
        return projectRepository.findAllActiveClientNames();
    }
    
    public List<Project> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Finding projects between {} and {}", startDate, endDate);
        return projectRepository.findByCompletionDateBetweenAndIsActiveTrue(startDate, endDate);
    }
    
    @Transactional
    @CacheEvict(value = "projects", allEntries = true)
    public Project save(Project project) {
        log.info("Saving project: {}", project.getProjectName());
        
        // Generate SEO metadata if not provided
        if (project.getSeoMetadata() == null) {
            project.setSeoMetadata(seoService.generateProjectSeoMetadata(project));
        }
        
        return projectRepository.save(project);
    }
    
    @Transactional
    @CacheEvict(value = "projects", allEntries = true)
    public Project update(Long id, Project project) {
        log.info("Updating project with id: {}", id);
        
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        
        // Update fields
        existingProject.setClientName(project.getClientName());
        existingProject.setClientLogoUrl(project.getClientLogoUrl());
        existingProject.setProjectName(project.getProjectName());
        existingProject.setDescription(project.getDescription());
        existingProject.setCategory(project.getCategory());
        existingProject.setLocation(project.getLocation());
        existingProject.setServicesProvided(project.getServicesProvided());
        existingProject.setProjectDuration(project.getProjectDuration());
        existingProject.setCompletionDate(project.getCompletionDate());
        existingProject.setFeatured(project.isFeatured());
        existingProject.setActive(project.isActive());
        existingProject.setDisplayOrder(project.getDisplayOrder());
        
        // Update SEO metadata
        if (project.getSeoMetadata() != null) {
            existingProject.setSeoMetadata(project.getSeoMetadata());
        }
        
        return projectRepository.save(existingProject);
    }
    
    @Transactional
    @CacheEvict(value = "projects", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting project with id: {}", id);
        projectRepository.deleteById(id);
    }
    
    public Map<ProjectCategory, Long> getProjectCountByCategory() {
        log.debug("Getting project count by category");
        return projectRepository.findByIsActiveTrue()
                .stream()
                .collect(Collectors.groupingBy(Project::getCategory, Collectors.counting()));
    }
    
    public long countActive() {
        return projectRepository.countByIsActiveTrue();
    }
    
    public long countClients() {
        return projectRepository.countDistinctClients();
    }
}