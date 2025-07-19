package com.hashrate.service;

import com.hashrate.model.Product;
import com.hashrate.model.Product.ProductCategory;
import com.hashrate.repository.ProductRepository;
import com.hashrate.util.SeoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ProductService {
    
    private final ProductRepository productRepository;
    private final SeoUtils seoUtils;
    private final SeoService seoService;
    
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    
    @Cacheable(value = "products", key = "#slug")
    public Optional<Product> findBySlug(String slug) {
        log.debug("Finding product by slug: {}", slug);
        return productRepository.findBySlug(slug);
    }
    
    @Cacheable(value = "products", key = "'all-active'")
    public List<Product> findAllActive() {
        log.debug("Finding all active products");
        return productRepository.findAllActiveOrderByDisplayOrder();
    }
    
    @Cacheable(value = "products", key = "#category")
    public List<Product> findByCategory(ProductCategory category) {
        log.debug("Finding products by category: {}", category);
        return productRepository.findByCategoryAndIsActiveTrue(category);
    }
    
    public Page<Product> findAllActive(Pageable pageable) {
        log.debug("Finding all active products with pagination");
        return productRepository.findByIsActiveTrue(pageable);
    }
    
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        log.debug("Searching products with keyword: {}", keyword);
        return productRepository.searchProducts(keyword, pageable);
    }
    
    @Cacheable(value = "product-categories")
    public List<ProductCategory> findAllCategories() {
        log.debug("Finding all active product categories");
        return productRepository.findAllActiveCategories();
    }
    
    @Transactional
    public Product save(Product product) {
        log.info("Saving product: {}", product.getName());
        
        // Generate slug if not provided
        if (product.getSlug() == null || product.getSlug().isEmpty()) {
            product.setSlug(seoUtils.generateSlug(product.getName()));
        }
        
        // Generate SEO metadata if not provided
        if (product.getSeoMetadata() == null) {
            product.setSeoMetadata(seoService.generateProductSeoMetadata(product));
        }
        
        return productRepository.save(product);
    }
    
    @Transactional
    public Product update(Long id, Product product) {
        log.info("Updating product with id: {}", id);
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Update fields
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setShortDescription(product.getShortDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setThumbnailUrl(product.getThumbnailUrl());
        existingProduct.setFeatures(product.getFeatures());
        existingProduct.setSpecifications(product.getSpecifications());
        existingProduct.setDisplayOrder(product.getDisplayOrder());
        existingProduct.setActive(product.isActive());
        
        // Update SEO metadata
        if (product.getSeoMetadata() != null) {
            existingProduct.setSeoMetadata(product.getSeoMetadata());
        }
        
        return productRepository.save(existingProduct);
    }
    
    @Transactional
    public void delete(Long id) {
        log.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }
    
    public long countActive() {
        return productRepository.countByIsActiveTrue();
    }
    
    public long countByCategory(ProductCategory category) {
        return productRepository.countByCategoryAndIsActiveTrue(category);
    }
}