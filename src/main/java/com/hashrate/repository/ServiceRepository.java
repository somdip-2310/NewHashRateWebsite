package com.hashrate.repository;

import com.hashrate.model.Service;
import com.hashrate.model.Service.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    Optional<Service> findBySlug(String slug);
    
    List<Service> findByIsActiveTrue();
    
    List<Service> findByIsActiveTrueAndIsFeaturedTrue();
    
    List<Service> findByCategoryAndIsActiveTrue(ServiceCategory category);
    
    Page<Service> findByIsActiveTrue(Pageable pageable);
    
    Page<Service> findByCategoryAndIsActiveTrue(ServiceCategory category, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true AND " +
           "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Service> searchServices(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true ORDER BY s.displayOrder ASC, s.id DESC")
    List<Service> findAllActiveOrderByDisplayOrder();
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true AND s.isFeatured = true " +
           "ORDER BY s.displayOrder ASC LIMIT :limit")
    List<Service> findTopFeaturedServices(@Param("limit") int limit);
    
    @Query("SELECT DISTINCT s.category FROM Service s WHERE s.isActive = true")
    List<ServiceCategory> findAllActiveCategories();
    
    long countByIsActiveTrue();
}