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
@Table(name = "careers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Career {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String jobId;
    
    @Column(nullable = false)
    private String title;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;
    
    @Column(nullable = false)
    private String location;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;
    
    @Column(name = "min_experience")
    private Integer minExperience;
    
    @Column(name = "max_experience")
    private Integer maxExperience;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "career_responsibilities", joinColumns = @JoinColumn(name = "career_id"))
    @Column(name = "responsibility", columnDefinition = "TEXT")
    private List<String> responsibilities = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "career_requirements", joinColumns = @JoinColumn(name = "career_id"))
    @Column(name = "requirement")
    private List<String> requirements = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "career_skills", joinColumns = @JoinColumn(name = "career_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();
    
    @Column(name = "salary_range")
    private String salaryRange;
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @Column(name = "is_urgent")
    private boolean isUrgent = false;
    
    @Column(name = "posted_date")
    private LocalDateTime postedDate;
    
    @Column(name = "closing_date")
    private LocalDateTime closingDate;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Embedded
    private SeoMetadata seoMetadata;
    
    public enum Department {
        DCIM,
        IBMS,
        PLC_SCADA,
        SOFTWARE,
        HR_ADMIN,
        FINANCE,
        OPERATIONS
    }
    
    public enum JobType {
        FULL_TIME,
        PART_TIME,
        CONTRACT,
        INTERNSHIP
    }
    
    public enum ExperienceLevel {
        FRESHER,
        JUNIOR,
        MID_LEVEL,
        SENIOR,
        LEAD,
        MANAGER
    }
}