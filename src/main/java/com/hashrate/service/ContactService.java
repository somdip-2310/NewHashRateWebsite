package com.hashrate.service;

import com.hashrate.dto.ContactFormDTO;
import com.hashrate.model.Contact;
import com.hashrate.model.Contact.ContactStatus;
import com.hashrate.model.Contact.Industry;
import com.hashrate.repository.ContactRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContactService {
    
    private final ContactRepository contactRepository;
    private final EmailService emailService;
    
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);
    
    @Transactional
    public Contact submitContactForm(ContactFormDTO contactFormDTO, HttpServletRequest request) {
        log.info("Processing contact form submission from: {}", contactFormDTO.getEmail());
        
        // Check for spam (honeypot field)
        if (contactFormDTO.getHoneypot() != null && !contactFormDTO.getHoneypot().isEmpty()) {
            log.warn("Potential spam detected from: {}", contactFormDTO.getEmail());
            throw new RuntimeException("Invalid submission");
        }
        
        // Check for duplicate submissions
        if (isDuplicateSubmission(contactFormDTO.getEmail())) {
            log.warn("Duplicate submission detected from: {}", contactFormDTO.getEmail());
            throw new RuntimeException("Please wait before submitting again");
        }
        
        // Create contact entity
        Contact contact = Contact.builder()
                .firstName(contactFormDTO.getFirstName())
                .lastName(contactFormDTO.getLastName())
                .email(contactFormDTO.getEmail())
                .phone(contactFormDTO.getPhone())
                .company(contactFormDTO.getCompany())
                .industry(Industry.valueOf(contactFormDTO.getIndustry()))
                .message(contactFormDTO.getMessage())
                .projectDetails(contactFormDTO.getProjectDetails())
                .status(ContactStatus.NEW)
                .ipAddress(getClientIpAddress(request))
                .userAgent(request.getHeader("User-Agent"))
                .build();
        
        // Save contact
        contact = contactRepository.save(contact);
        
        // Send email notifications
        emailService.sendContactFormNotification(contact);
        emailService.sendContactFormConfirmation(contact);
        
        log.info("Contact form processed successfully with id: {}", contact.getId());
        
        return contact;
    }
    
    private boolean isDuplicateSubmission(String email) {
        // Check if same email submitted in last 5 minutes
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        return contactRepository.existsByEmailAndCreatedAtAfter(email, fiveMinutesAgo);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null) {
            return xForwardedForHeader.split(",")[0];
        }
        
        String xRealIpHeader = request.getHeader("X-Real-IP");
        if (xRealIpHeader != null) {
            return xRealIpHeader;
        }
        
        return request.getRemoteAddr();
    }
    
    public Page<Contact> findAll(Pageable pageable) {
        log.debug("Finding all contacts with pagination");
        return contactRepository.findAll(pageable);
    }
    
    public Page<Contact> findByStatus(ContactStatus status, Pageable pageable) {
        log.debug("Finding contacts by status: {}", status);
        return contactRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }
    
    public Page<Contact> searchContacts(String keyword, Pageable pageable) {
        log.debug("Searching contacts with keyword: {}", keyword);
        return contactRepository.searchContacts(keyword, pageable);
    }
    
    public List<Contact> findUnassigned() {
        log.debug("Finding unassigned contacts");
        return contactRepository.findUnassignedContacts();
    }
    
    @Transactional
    public Contact updateStatus(Long id, ContactStatus status, String notes) {
        log.info("Updating contact {} status to: {}", id, status);
        
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        
        contact.setStatus(status);
        contact.setNotes(notes);
        
        if (status == ContactStatus.RESPONDED) {
            contact.setRespondedAt(LocalDateTime.now());
        }
        
        return contactRepository.save(contact);
    }
    
    @Transactional
    public Contact assignTo(Long id, String assignedTo) {
        log.info("Assigning contact {} to: {}", id, assignedTo);
        
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        
        contact.setAssignedTo(assignedTo);
        contact.setStatus(ContactStatus.IN_PROGRESS);
        
        return contactRepository.save(contact);
    }
    
    public Map<ContactStatus, Long> getStatusStatistics() {
        log.debug("Getting contact status statistics");
        return contactRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Contact::getStatus, Collectors.counting()));
    }
    
    public List<Object[]> getContactStatsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Getting contact statistics between {} and {}", startDate, endDate);
        return contactRepository.getContactStatsByDateRange(startDate, endDate);
    }
    
    public List<Object[]> getIndustryStatistics() {
        log.debug("Getting contact industry statistics");
        return contactRepository.countByIndustryGrouped();
    }
    
    @Transactional
    public void cleanupOldContacts() {
        log.info("Cleaning up old spam contacts");
        
        // Delete spam contacts older than 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Contact> oldSpamContacts = contactRepository.findByStatusAndCreatedAtBefore(
                ContactStatus.SPAM, thirtyDaysAgo);
        
        contactRepository.deleteAll(oldSpamContacts);
        
        log.info("Deleted {} old spam contacts", oldSpamContacts.size());
    }
}