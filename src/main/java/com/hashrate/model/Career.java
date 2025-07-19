package com.hashrate.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "careers")
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

    // Constructors
    public Career() {}

    public Career(Long id, String jobId, String title, Department department, String location, 
                  JobType jobType, ExperienceLevel experienceLevel, Integer minExperience, 
                  Integer maxExperience, String description, List<String> responsibilities, 
                  List<String> requirements, List<String> skills, String salaryRange, 
                  boolean isActive, boolean isUrgent, LocalDateTime postedDate, 
                  LocalDateTime closingDate, LocalDateTime createdAt, LocalDateTime updatedAt, 
                  SeoMetadata seoMetadata) {
        this.id = id;
        this.jobId = jobId;
        this.title = title;
        this.department = department;
        this.location = location;
        this.jobType = jobType;
        this.experienceLevel = experienceLevel;
        this.minExperience = minExperience;
        this.maxExperience = maxExperience;
        this.description = description;
        this.responsibilities = responsibilities;
        this.requirements = requirements;
        this.skills = skills;
        this.salaryRange = salaryRange;
        this.isActive = isActive;
        this.isUrgent = isUrgent;
        this.postedDate = postedDate;
        this.closingDate = closingDate;
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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public Integer getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(Integer minExperience) {
        this.minExperience = minExperience;
    }

    public Integer getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(Integer maxExperience) {
        this.maxExperience = maxExperience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDateTime closingDate) {
        this.closingDate = closingDate;
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