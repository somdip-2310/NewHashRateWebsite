package com.hashrate.repository;

import com.hashrate.model.Product;
import com.hashrate.model.Product.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySlug(String slug);
    
    List<Product> findByIsActiveTrue();
    
    List<Product> findByCategoryAndIsActiveTrue(ProductCategory category);
    
    Page<Product> findByIsActiveTrue(Pageable pageable);
    
    Page<Product> findByCategoryAndIsActiveTrue(ProductCategory category, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.displayOrder ASC, p.id DESC")
    List<Product> findAllActiveOrderByDisplayOrder();
    
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isActive = true")
    List<ProductCategory> findAllActiveCategories();
    
    long countByIsActiveTrue();
    
    long countByCategoryAndIsActiveTrue(ProductCategory category);
}