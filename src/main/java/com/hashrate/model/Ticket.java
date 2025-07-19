package com.hashrate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String ticketNumber;
    
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
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;
    
    @Column(nullable = false)
    private String subject;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "ticket_attachments", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "attachment_url")
    private List<String> attachments = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;
    
    @Column(name = "assigned_to")
    private String assignedTo;
    
    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;
    
    @Column(name = "customer_feedback", columnDefinition = "TEXT")
    private String customerFeedback;
    
    @Column(name = "rating")
    private Integer rating;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    
    public enum TicketType {
        SALES,
        SUPPORT,
        INFORMATION,
        FEEDBACK,
        COMPLAINT,
        GENERAL
    }
    
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }
    
    public enum TicketStatus {
        OPEN,
        IN_PROGRESS,
        PENDING_CUSTOMER,
        RESOLVED,
        CLOSED,
        REOPENED
    }
}