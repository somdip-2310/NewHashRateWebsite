package com.hashrate.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String clientName;
    
    @Column(name = "client_logo_url")
    private String clientLogoUrl;
    
    @Column(nullable = false)
    private String projectName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectCategory category;
    
    @Column(nullable = false)
    private String location;
    
    @ElementCollection
    @CollectionTable(name = "project_services", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "service")
    private List<String> servicesProvided = new ArrayList<>();
    
    @Column(name = "project_duration")
    private String projectDuration;
    
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    
    @Column(name = "is_featured")
    private boolean isFeatured = false;
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
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
    
    public enum ProjectCategory {
        AIRPORT_METRO,
        BFSI,
        GOVERNMENT,
        OIL_GAS_MFG,
        DATACENTER,
        COMMERCIAL
    }

    // Constructors
    public Project() {}

    public Project(Long id, String clientName, String clientLogoUrl, String projectName, 
                   String description, ProjectCategory category, String location, 
                   List<String> servicesProvided, String projectDuration, 
                   LocalDateTime completionDate, boolean isFeatured, boolean isActive, 
                   Integer displayOrder, LocalDateTime createdAt, LocalDateTime updatedAt, 
                   SeoMetadata seoMetadata) {
        this.id = id;
        this.clientName = clientName;
        this.clientLogoUrl = clientLogoUrl;
        this.projectName = projectName;
        this.description = description;
        this.category = category;
        this.location = location;
        this.servicesProvided = servicesProvided;
        this.projectDuration = projectDuration;
        this.completionDate = completionDate;
        this.isFeatured = isFeatured;
        this.isActive = isActive;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLogoUrl() {
        return clientLogoUrl;
    }

    public void setClientLogoUrl(String clientLogoUrl) {
        this.clientLogoUrl = clientLogoUrl;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectCategory getCategory() {
        return category;
    }

    public void setCategory(ProjectCategory category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getServicesProvided() {
        return servicesProvided;
    }

    public void setServicesProvided(List<String> servicesProvided) {
        this.servicesProvided = servicesProvided;
    }

    public String getProjectDuration() {
        return projectDuration;
    }

    public void setProjectDuration(String projectDuration) {
        this.projectDuration = projectDuration;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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