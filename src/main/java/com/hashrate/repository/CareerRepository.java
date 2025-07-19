package com.hashrate.repository;

import com.hashrate.model.Career;
import com.hashrate.model.Career.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends JpaRepository<Career, Long> {
    
    Page<Career> findByIsActiveTrueAndExpiryDateAfter(LocalDateTime currentDate, Pageable pageable);
    
    Page<Career> findByDepartmentAndIsActiveTrueAndExpiryDateAfter(Department department, LocalDateTime currentDate, Pageable pageable);
    
    Page<Career> findByLocationAndIsActiveTrueAndExpiryDateAfter(String location, LocalDateTime currentDate, Pageable pageable);
    
    List<Career> findByIsActiveTrueAndExpiryDateAfter(LocalDateTime currentDate);
    
    Optional<Career> findByJobIdAndIsActiveTrueAndExpiryDateAfter(String jobId, LocalDateTime currentDate);
    
    @Query("SELECT DISTINCT c.location FROM Career c WHERE c.isActive = true AND c.expiryDate > :currentDate ORDER BY c.location")
    List<String> findDistinctLocations();
    
    long countByIsActiveTrueAndExpiryDateAfter(LocalDateTime currentDate);
    
    long countByIsUrgentTrueAndIsActiveTrueAndExpiryDateAfter(LocalDateTime currentDate);
    
    long countByDepartmentAndIsActiveTrueAndExpiryDateAfter(Department department, LocalDateTime currentDate);
}