package com.hashrate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}