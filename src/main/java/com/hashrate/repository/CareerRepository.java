package com.hashrate.repository;

import com.hashrate.model.Career;
import com.hashrate.model.Career.Department;
import com.hashrate.model.Career.ExperienceLevel;
import com.hashrate.model.Career.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {
    
    Optional<Career> findByJobId(String jobId);
    
    List<Career> findByIsActiveTrue();
    
    List<Career> findByIsActiveTrueAndIsUrgentTrue();
    
    List<Career> findByDepartmentAndIsActiveTrue(Department department);
    
    List<Career> findByJobTypeAndIsActiveTrue(JobType jobType);
    
    List<Career> findByExperienceLevelAndIsActiveTrue(ExperienceLevel experienceLevel);
    
    Page<Career> findByIsActiveTrue(Pageable pageable);
    
    @Query("SELECT c FROM Career c WHERE c.isActive = true AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Career> searchCareers(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT c FROM Career c WHERE c.isActive = true AND c.location = :location")
    List<Career> findByLocation(@Param("location") String location);
    
    @Query("SELECT c FROM Career c WHERE c.isActive = true AND " +
           "c.minExperience <= :experience AND c.maxExperience >= :experience")
    List<Career> findByExperienceRange(@Param("experience") Integer experience);
    
    List<Career> findByIsActiveTrueAndClosingDateAfter(LocalDateTime date);
    
    @Query("SELECT DISTINCT c.location FROM Career c WHERE c.isActive = true")
    List<String> findAllActiveLocations();
    
    @Query("SELECT c FROM Career c WHERE c.isActive = true ORDER BY c.isUrgent DESC, c.postedDate DESC")
    Page<Career> findAllActiveOrderByUrgencyAndDate(Pageable pageable);
    
    long countByIsActiveTrue();
    
    long countByDepartmentAndIsActiveTrue(Department department);
    
    long countByIsActiveTrueAndIsUrgentTrue();
}