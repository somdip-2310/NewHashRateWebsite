package com.hashrate.repository;

import com.hashrate.model.Solution;
import com.hashrate.model.Solution.SolutionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    
    Optional<Solution> findBySlug(String slug);
    
    List<Solution> findByIsActiveTrue();
    
    List<Solution> findByIsActiveTrueAndIsFeaturedTrue();
    
    List<Solution> findByTypeAndIsActiveTrue(SolutionType type);
    
    Page<Solution> findByIsActiveTrue(Pageable pageable);
    
    @Query("SELECT s FROM Solution s WHERE s.isActive = true AND " +
           "(LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Solution> searchSolutions(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT s FROM Solution s WHERE s.isActive = true ORDER BY s.displayOrder ASC, s.id DESC")
    List<Solution> findAllActiveOrderByDisplayOrder();
    
    @Query("SELECT s FROM Solution s WHERE s.isActive = true AND s.isFeatured = true " +
           "ORDER BY s.displayOrder ASC LIMIT :limit")
    List<Solution> findTopFeaturedSolutions(@Param("limit") int limit);
    
    long countByIsActiveTrue();
    
    long countByTypeAndIsActiveTrue(SolutionType type);
}