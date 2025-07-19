package com.hashrate.repository;

import com.hashrate.model.Project;
import com.hashrate.model.Project.ProjectCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByIsActiveTrue();
    
    List<Project> findByIsActiveTrueAndIsFeaturedTrue();
    
    List<Project> findByCategoryAndIsActiveTrue(ProjectCategory category);
    
    Page<Project> findByIsActiveTrue(Pageable pageable);
    
    Page<Project> findByCategoryAndIsActiveTrue(ProjectCategory category, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND " +
           "(LOWER(p.clientName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Project> searchProjects(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true ORDER BY p.displayOrder ASC, p.completionDate DESC")
    List<Project> findAllActiveOrderByDisplayOrderAndDate();
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND p.isFeatured = true " +
           "ORDER BY p.completionDate DESC LIMIT :limit")
    List<Project> findTopFeaturedProjects(@Param("limit") int limit);
    
    List<Project> findByCompletionDateBetweenAndIsActiveTrue(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT DISTINCT p.clientName FROM Project p WHERE p.isActive = true ORDER BY p.clientName")
    List<String> findAllActiveClientNames();
    
    @Query("SELECT COUNT(DISTINCT p.clientName) FROM Project p WHERE p.isActive = true")
    long countDistinctClients();
    
    long countByIsActiveTrue();
    
    long countByCategoryAndIsActiveTrue(ProjectCategory category);
}