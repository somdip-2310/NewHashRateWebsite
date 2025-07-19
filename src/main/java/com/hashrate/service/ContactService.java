package com.hashrate.service;

import com.hashrate.controller.HomeController;
import com.hashrate.dto.ContactFormDTO;
import com.hashrate.model.Contact;
import com.hashrate.repository.ContactRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ContactService {
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;
    private final EmailService emailService;

    @Autowired
    public ContactService(ContactRepository contactRepository, EmailService emailService) {
        this.contactRepository = contactRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Contact submitContactForm(ContactFormDTO contactForm, HttpServletRequest request) {
        log.info("Processing contact form submission from: {}", contactForm.getEmail());

        // Convert DTO to Entity
        Contact contact = new Contact();
        contact.setFirstName(contactForm.getFirstName());
        contact.setLastName(contactForm.getLastName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhone(contactForm.getPhone());
        contact.setCompanyName(contactForm.getCompanyName());
        contact.setSubject(contactForm.getSubject());
        contact.setMessage(contactForm.getMessage());
        contact.setContactType(Contact.ContactType.valueOf(contactForm.getContactType()));
        contact.setStatus(Contact.ContactStatus.NEW);

        // Save contact
        Contact savedContact = contactRepository.save(contact);

        // Send notification emails
        try {
            emailService.sendContactFormNotification(savedContact);
            emailService.sendContactFormConfirmation(savedContact);
            log.info("Contact form emails sent successfully for: {}", savedContact.getEmail());
        } catch (Exception e) {
            log.error("Failed to send contact form emails", e);
            // Don't fail the whole operation if emails fail
        }

        return savedContact;
    }

    public List<Contact> findAllContacts() {
        log.debug("Finding all contacts");
        return contactRepository.findAllOrderByCreatedAtDesc();
    }

    public List<Contact> findContactsByStatus(Contact.ContactStatus status) {
        log.debug("Finding contacts by status: {}", status);
        return contactRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id);
    }

    @Transactional
    public Contact updateContactStatus(Long id, Contact.ContactStatus status, String responseNotes) {
        log.info("Updating contact status for id: {} to status: {}", id, status);

        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

        contact.setStatus(status);
        contact.setResponseNotes(responseNotes);

        if (status == Contact.ContactStatus.RESPONDED) {
            contact.setRespondedAt(LocalDateTime.now());
        }

        return contactRepository.save(contact);
    }

    public long countNewContacts() {
        return contactRepository.countByStatus(Contact.ContactStatus.NEW);
    }

    public long countTotalContacts() {
        return contactRepository.count();
    }

    public List<Contact> findRecentContacts(int limit) {
        log.debug("Finding {} recent contacts", limit);
        return contactRepository.findTopNOrderByCreatedAtDesc(limit);
    }
}