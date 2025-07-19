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
@Table(name = "solutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}