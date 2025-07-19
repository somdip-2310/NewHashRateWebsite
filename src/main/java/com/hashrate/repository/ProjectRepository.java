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
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true ORDER BY p.displayOrder ASC, p.completionDate DESC")
    List<Project> findAllActiveOrderByDisplayOrderAndDate();
    
    List<Project> findByIsActiveTrueAndIsFeaturedTrue();
    
    List<Project> findByCategoryAndIsActiveTrue(ProjectCategory category);
    
    Page<Project> findByIsActiveTrue(Pageable pageable);
    
    Page<Project> findByCategoryAndIsActiveTrue(ProjectCategory category, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND " +
           "(LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.clientName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Project> searchProjects(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND p.isFeatured = true " +
           "ORDER BY p.displayOrder ASC, p.completionDate DESC LIMIT :limit")
    List<Project> findTopFeaturedProjects(@Param("limit") int limit);
    
    @Query("SELECT DISTINCT p.clientName FROM Project p WHERE p.isActive = true ORDER BY p.clientName")
    List<String> findAllActiveClientNames();
    
    List<Project> findByCompletionDateBetweenAndIsActiveTrue(LocalDateTime startDate, LocalDateTime endDate);
    
    long countByIsActiveTrue();
    
    @Query("SELECT COUNT(DISTINCT p.clientName) FROM Project p WHERE p.isActive = true")
    long countDistinctClients();
    
    long countByCategoryAndIsActiveTrue(ProjectCategory category);
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND p.completionDate >= :fromDate ORDER BY p.completionDate DESC")
    List<Project> findRecentProjects(@Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND p.clientName = :clientName ORDER BY p.completionDate DESC")
    List<Project> findByClientNameAndIsActiveTrue(@Param("clientName") String clientName);
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND p.location LIKE %:location% ORDER BY p.completionDate DESC")
    List<Project> findByLocationContainingAndIsActiveTrue(@Param("location") String location);
    
    @Query("SELECT DISTINCT p.location FROM Project p WHERE p.isActive = true AND p.location IS NOT NULL ORDER BY p.location")
    List<String> findDistinctLocations();
    
    @Query("SELECT COUNT(p) FROM Project p WHERE p.isActive = true AND p.completionDate >= :fromDate")
    long countCompletedProjectsSince(@Param("fromDate") LocalDateTime fromDate);
}