package com.hashrate.service;

import com.hashrate.dto.CareerApplicationDTO;
import com.hashrate.model.Career;
import com.hashrate.model.Contact;
import com.hashrate.model.Ticket;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    
    @Value("${app.email.from:noreply@hashrate.in}")
    private String fromEmail;
    
    @Value("${app.email.admin:admin@hashrate.in}")
    private String adminEmail;
    
    @Value("${app.email.hr:hr@hashrate.in}")
    private String hrEmail;
    
    @Value("${app.email.support:support@hashrate.in}")
    private String supportEmail;
    
    @Value("${app.company.name:Hash Rate Communications}")
    private String companyName;
    
    @Value("${app.company.website:https://www.hashrate.in}")
    private String companyWebsite;

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    public void sendContactFormNotification(Contact contact) {
        try {
            log.info("Sending contact form notification for: {}", contact.getEmail());
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("contact", contact);
            variables.put("companyName", companyName);
            variables.put("companyWebsite", companyWebsite);
            
            String htmlContent = processTemplate("email/contact-notification", variables);
            
            sendHtmlEmail(
                    adminEmail,
                    "New Contact Form Submission - " + contact.getFirstName() + " " + contact.getLastName(),
                    htmlContent
            );
            
            log.info("Contact form notification sent successfully");
        } catch (Exception e) {
            log.error("Failed to send contact form notification", e);
        }
    }
    
    public void sendContactFormConfirmation(Contact contact) {
        try {
            log.info("Sending contact form confirmation to: {}", contact.getEmail());
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("contact", contact);
            variables.put("companyName", companyName);
            variables.put("companyWebsite", companyWebsite);
            
            String htmlContent = processTemplate("email/contact-confirmation", variables);
            
            sendHtmlEmail(
                    contact.getEmail(),
                    "Thank you for contacting " + companyName,
                    htmlContent
            );
            
            log.info("Contact form confirmation sent successfully");
        } catch (Exception e) {
            log.error("Failed to send contact form confirmation", e);
        }
    }
    
    public void sendCareerApplicationEmail(Career career, CareerApplicationDTO application, String resumePath) {
        try {
            log.info("Sending career application email for job: {}", career.getTitle());
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("career", career);
            variables.put("application", application);
            variables.put("companyName", companyName);
            variables.put("companyWebsite", companyWebsite);
            
            String htmlContent = processTemplate("email/career-application", variables);
            
            // Send email with attachment
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            
            helper.setFrom(fromEmail);
            helper.setTo(hrEmail);
            helper.setSubject("New Job Application - " + career.getTitle() + " - " + application.getFirstName() + " " + application.getLastName());
            helper.setText(htmlContent, true);
            
            // Attach resume
            FileSystemResource file = new FileSystemResource(new File(resumePath));
            helper.addAttachment("Resume_" + application.getFirstName() + "_" + application.getLastName() + ".pdf", file);
            
            mailSender.send(message);
            
            log.info("Career application email sent successfully");
            
            // Send confirmation to applicant
            sendCareerApplicationConfirmation(career, application);
            
        } catch (Exception e) {
            log.error("Failed to send career application email", e);
        }
    }
    
    private void sendCareerApplicationConfirmation(Career career, CareerApplicationDTO application) {
        try {
            log.info("Sending career application confirmation to: {}", application.getEmail());
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("career", career);
            variables.put("application", application);
            variables.put("companyName", companyName);
            variables.put("companyWebsite", companyWebsite);
            
            String htmlContent = processTemplate("email/career-application-confirmation", variables);
            
            sendHtmlEmail(
                    application.getEmail(),
                    "Application Received - " + career.getTitle() + " at " + companyName,
                    htmlContent
            );
            
            log.info("Career application confirmation sent successfully");
        } catch (Exception e) {
            log.error("Failed to send career application confirmation", e);
        }
    }
    
    public void sendTicketCreatedEmail(Ticket ticket) {
        try {
            log.info("Sending ticket created email for ticket: {}", ticket.getTicketNumber());
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("ticket", ticket);
            variables.put("companyName", companyName);
            variables.put("companyWebsite", companyWebsite);
            
            String htmlContent = processTemplate("email/ticket-created", variables);
            
            // Send to support team
            sendHtmlEmail(
                    supportEmail,
                    "New Ticket Created - " + ticket.getTicketNumber() + " - " + ticket.getSubject(),
                    htmlContent
            );
            
            // Send confirmation to customer
            sendTicketConfirmation(ticket);
            
            log.info("Ticket created email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send ticket created email", e);
        }
    }
    
    private void sendTicketConfirmation(Ticket ticket) {
        try {
            log.info("Sending ticket confirmation to: {}", ticket.getCustomerEmail());
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("ticket", ticket);
            variables.put("companyName", companyName);
            variables.put("companyWebsite", companyWebsite);
            
            String htmlContent = processTemplate("email/ticket-confirmation", variables);
            
            sendHtmlEmail(
                    ticket.getCustomerEmail(),
                    "Ticket Created - " + ticket.getTicketNumber() + " - " + companyName,
                    htmlContent
            );
            
            log.info("Ticket confirmation sent successfully");
        } catch (Exception e) {
            log.error("Failed to send ticket confirmation", e);
        }
    }
    
    public void sendTicketUpdateEmail(Ticket ticket) {
        try {
            log.info("Sending ticket update email for ticket: {}", ticket.getTicketNumber());
            
            Map<String, Object> variables = new HashMap<>();
            variables.put("ticket", ticket);
            variables.put("companyName", companyName);
            variables.put("companyWebsite", companyWebsite);
            
            String htmlContent = processTemplate("email/ticket-update", variables);
            
            sendHtmlEmail(
                    ticket.getCustomerEmail(),
                    "Ticket Update - " + ticket.getTicketNumber() + " - " + companyName,
                    htmlContent
            );
            
            log.info("Ticket update email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send ticket update email", e);
        }
    }
    
    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        mailSender.send(message);
    }
    
    private void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        
        mailSender.send(message);
    }
    
    private String processTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
    
    // Test email functionality
    public void sendTestEmail(String to) {
        try {
            log.info("Sending test email to: {}", to);
            
            sendSimpleEmail(
                    to,
                    "Test Email from " + companyName,
                    "This is a test email from " + companyName + " website.\n\n" +
                    "If you received this email, your email configuration is working correctly."
            );
            
            log.info("Test email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send test email", e);
            throw new RuntimeException("Failed to send test email", e);
        }
    }
}