package com.hashrate.controller;

import com.hashrate.dto.ContactDTO;
import com.hashrate.model.Contact;
import com.hashrate.service.ContactService;
import com.hashrate.service.ServiceManagementService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
public class ContactController {
    
	private static final Logger log = LoggerFactory.getLogger(ContactController.class);
    
    private final ContactService contactService;
    
    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }
    
    @GetMapping
    public String contact(Model model) {
        log.debug("Loading contact page");
        
        model.addAttribute("contactForm", new ContactDTO());
        
        // SEO
        model.addAttribute("pageTitle", "Contact Us - Hash Rate Communications");
        model.addAttribute("pageDescription", "Get in touch with Hash Rate Communications for your technology needs. Multiple locations across India.");
        
        return "contact/contact";
    }
    
    @PostMapping
    public String submitContact(@Valid @ModelAttribute("contactForm") ContactDTO contactDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            log.warn("Contact form validation errors: {}", bindingResult.getAllErrors());
            
            // SEO
            model.addAttribute("pageTitle", "Contact Us - Hash Rate Communications");
            model.addAttribute("pageDescription", "Get in touch with Hash Rate Communications for your technology needs. Multiple locations across India.");
            
            return "contact/contact";
        }
        
        try {
            Contact contact = contactService.saveContact(contactDTO);
            log.info("Contact form submitted successfully: {}", contact.getEmail());
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Thank you for contacting us! We'll get back to you within 24 hours.");
            
            return "redirect:/contact/success";
            
        } catch (Exception e) {
            log.error("Error processing contact form", e);
            
            model.addAttribute("errorMessage", 
                "Sorry, there was an error processing your request. Please try again.");
            model.addAttribute("pageTitle", "Contact Us - Hash Rate Communications");
            model.addAttribute("pageDescription", "Get in touch with Hash Rate Communications for your technology needs. Multiple locations across India.");
            
            return "contact/contact";
        }
    }
    
    @GetMapping("/success")
    public String contactSuccess(Model model) {
        log.debug("Showing contact success page");
        
        // SEO
        model.addAttribute("pageTitle", "Message Sent Successfully | Hash Rate Communications");
        model.addAttribute("pageDescription", "Your message has been sent successfully. Our team will respond within 24 hours.");
        
        return "contact/success";
    }
}