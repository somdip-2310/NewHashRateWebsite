package com.hashrate.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solutions")
public class Solution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "short_description", length = 500)
    private String shortDescription;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SolutionType type;
    
    @Column(name = "icon_class")
    private String iconClass;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "banner_image_url")
    private String bannerImageUrl;
    
    @ElementCollection
    @CollectionTable(name = "solution_features", joinColumns = @JoinColumn(name = "solution_id"))
    @Column(name = "feature")
    private List<String> features = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "solution_benefits", joinColumns = @JoinColumn(name = "solution_id"))
    @Column(name = "benefit")
    private List<String> benefits = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "solution_use_cases", joinColumns = @JoinColumn(name = "solution_id"))
    @Column(name = "use_case", columnDefinition = "TEXT")
    private List<String> useCases = new ArrayList<>();
    
    @Column(name = "architecture_details", columnDefinition = "TEXT")
    private String architectureDetails;
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @Column(name = "is_featured")
    private boolean isFeatured = false;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Embedded
    private SeoMetadata seoMetadata;
    
    public enum SolutionType {
        CENTRAL_DASHBOARD,
        ASSET_TRACKING,
        CHILLER_PLANT_MANAGEMENT,
        PLC_SCADA,
        DCIM,
        BMS
    }

    // Constructors
    public Solution() {}

    public Solution(Long id, String slug, String title, String description, 
                    String shortDescription, SolutionType type, String iconClass, 
                    String imageUrl, String bannerImageUrl, List<String> features, 
                    List<String> benefits, List<String> useCases, String architectureDetails, 
                    boolean isActive, boolean isFeatured, Integer displayOrder, 
                    LocalDateTime createdAt, LocalDateTime updatedAt, SeoMetadata seoMetadata) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.shortDescription = shortDescription;
        this.type = type;
        this.iconClass = iconClass;
        this.imageUrl = imageUrl;
        this.bannerImageUrl = bannerImageUrl;
        this.features = features;
        this.benefits = benefits;
        this.useCases = useCases;
        this.architectureDetails = architectureDetails;
        this.isActive = isActive;
        this.isFeatured = isFeatured;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.seoMetadata = seoMetadata;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public SolutionType getType() {
        return type;
    }

    public void setType(SolutionType type) {
        this.type = type;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

    public List<String> getUseCases() {
        return useCases;
    }

    public void setUseCases(List<String> useCases) {
        this.useCases = useCases;
    }

    public String getArchitectureDetails() {
        return architectureDetails;
    }

    public void setArchitectureDetails(String architectureDetails) {
        this.architectureDetails = architectureDetails;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SeoMetadata getSeoMetadata() {
        return seoMetadata;
    }

    public void setSeoMetadata(SeoMetadata seoMetadata) {
        this.seoMetadata = seoMetadata;
    }
}