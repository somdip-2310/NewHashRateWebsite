package com.hashrate.controller;

import com.hashrate.dto.ContactFormDTO;
import com.hashrate.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contact")
@RequiredArgsConstructor
@Slf4j
public class ContactController {
    
    private final ContactService contactService;
    
    @GetMapping
    public String contact(Model model) {
        log.debug("Loading contact page");
        
        model.addAttribute("contactForm", new ContactFormDTO());
        
        // SEO
        model.addAttribute("pageTitle", "Contact Us - Get in Touch | Hash Rate Communications");
        model.addAttribute("pageDescription", "Contact Hash Rate Communications for DCIM, iBMS, PLC & SCADA solutions. Located in Navi Mumbai, serving clients across India. Call +91 9137455975.");
        
        return "contact/contact";
    }
    
    @PostMapping
    public String submitContact(@Valid @ModelAttribute("contactForm") ContactFormDTO contactForm,
                               BindingResult bindingResult,
                               HttpServletRequest request,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        log.debug("Processing contact form submission");
        
        if (bindingResult.hasErrors()) {
            return "contact/contact";
        }
        
        try {
            contactService.submitContactForm(contactForm, request);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Thank you for contacting us! We will get back to you within 24 hours.");
            return "redirect:/contact/success";
        } catch (Exception e) {
            log.error("Error processing contact form", e);
            model.addAttribute("errorMessage", 
                    "There was an error processing your request. Please try again later.");
            return "contact/contact";
        }
    }
    
    @GetMapping("/success")
    public String contactSuccess(Model model) {
        log.debug("Showing contact success page");
        
        // SEO
        model.addAttribute("pageTitle", "Thank You - Message Sent Successfully | Hash Rate Communications");
        model.addAttribute("pageDescription", "Your message has been sent successfully. Our team will review it and get back to you within 24 hours.");
        
        return "contact/success";
    }
}