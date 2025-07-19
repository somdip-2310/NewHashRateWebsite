package com.hashrate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String firstName;
    
    @NotBlank
    @Column(nullable = false)
    private String lastName;
    
    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;
    
    @NotBlank
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String company;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Industry industry;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "project_details", columnDefinition = "TEXT")
    private String projectDetails;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_status")
    private ContactStatus status = ContactStatus.NEW;
    
    @Column(name = "assigned_to")
    private String assignedTo;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
    
    public enum Industry {
        OIL_GAS,
        STEEL_PLANT,
        FACTORY,
        CONSTRUCTION,
        SOLAR_PLANT,
        FOOD_INDUSTRY,
        AGRICULTURE,
        SHIP_INDUSTRY,
        LEATHER_INDUSTRY,
        NUCLEAR_PLANT,
        BEER_FACTORY,
        MINING_INDUSTRY,
        CAR_INDUSTRY,
        PLASTIC_INDUSTRY,
        DATACENTER,
        BFSI,
        GOVERNMENT,
        OTHER
    }
    
    public enum ContactStatus {
        NEW,
        IN_PROGRESS,
        RESPONDED,
        CLOSED,
        SPAM
    }
}