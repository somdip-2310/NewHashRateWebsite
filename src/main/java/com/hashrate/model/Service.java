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
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "short_description", length = 500)
    private String shortDescription;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;
    
    @Column(name = "icon_class")
    private String iconClass;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @ElementCollection
    @CollectionTable(name = "service_deliverables", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "deliverable")
    private List<String> deliverables = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "service_technologies", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "technology")
    private List<String> technologies = new ArrayList<>();
    
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
    
    public enum ServiceCategory {
        PROJECT_MANAGEMENT,
        BMS_OPERATIONS,
        DCIM,
        BATTERY_MONITORING,
        HARDWARE_SUPPLY,
        SOFTWARE_SOLUTIONS,
        MAINTENANCE_SUPPORT
    }
}