package com.hashrate.controller;

import com.hashrate.dto.TicketDTO;
import com.hashrate.model.Ticket;
import com.hashrate.model.Ticket.TicketPriority;
import com.hashrate.model.Ticket.TicketType;
import com.hashrate.repository.TicketRepository;
import com.hashrate.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    
    private static final Logger log = LoggerFactory.getLogger(TicketController.class);
    
    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    
    @Autowired
    public TicketController(TicketRepository ticketRepository, EmailService emailService) {
        this.ticketRepository = ticketRepository;
        this.emailService = emailService;
    }
    
    @GetMapping("/raise")
    public String raiseTicket(Model model) {
        log.debug("Loading raise ticket page");
        
        model.addAttribute("ticketForm", new TicketDTO());
        model.addAttribute("ticketTypes", TicketType.values());
        model.addAttribute("priorities", TicketPriority.values());
        
        // SEO
        model.addAttribute("pageTitle", "Raise a Support Ticket | Hash Rate Communications");
        model.addAttribute("pageDescription", "Submit a support ticket for technical assistance, sales inquiries, or general information. Our team responds within 24 hours.");
        
        return "tickets/raise";
    }
    
    @PostMapping("/raise")
    public String submitTicket(@Valid @ModelAttribute("ticketForm") TicketDTO ticketForm,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        log.debug("Processing ticket submission");
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("ticketTypes", TicketType.values());
            model.addAttribute("priorities", TicketPriority.values());
            return "tickets/raise";
        }
        
        try {
            // Create ticket
            Ticket ticket = new Ticket();
            ticket.setTicketNumber(generateTicketNumber());
            ticket.setCustomerName(ticketForm.getCustomerName());
            ticket.setCustomerEmail(ticketForm.getCustomerEmail());
            ticket.setCustomerPhone(ticketForm.getCustomerPhone());
            ticket.setSubject(ticketForm.getSubject());
            ticket.setDescription(ticketForm.getDescription());
            ticket.setType(ticketForm.getType());
            ticket.setPriority(ticketForm.getPriority());
            
            ticket = ticketRepository.save(ticket);
            
            // Send email notifications
            emailService.sendTicketCreatedEmail(ticket);
            
            redirectAttributes.addFlashAttribute("ticketNumber", ticket.getTicketNumber());
            return "redirect:/tickets/success";
            
        } catch (Exception e) {
            log.error("Error creating ticket", e);
            model.addAttribute("errorMessage", "There was an error creating your ticket. Please try again.");
            model.addAttribute("ticketTypes", TicketType.values());
            model.addAttribute("priorities", TicketPriority.values());
            return "tickets/raise";
        }
    }
    
    @GetMapping("/success")
    public String ticketSuccess(Model model) {
        log.debug("Showing ticket success page");
        
        // SEO
        model.addAttribute("pageTitle", "Ticket Created Successfully | Hash Rate Communications");
        model.addAttribute("pageDescription", "Your support ticket has been created successfully. Our team will respond within 24 hours.");
        
        return "tickets/success";
    }
    
    @GetMapping("/track")
    public String trackTicket(@RequestParam(required = false) String ticketNumber, Model model) {
        log.debug("Loading track ticket page");
        
        if (ticketNumber != null && !ticketNumber.isEmpty()) {
            ticketRepository.findByTicketNumber(ticketNumber)
                    .ifPresent(ticket -> model.addAttribute("ticket", ticket));
        }
        
        // SEO
        model.addAttribute("pageTitle", "Track Your Support Ticket | Hash Rate Communications");
        model.addAttribute("pageDescription", "Track the status of your support ticket using your ticket number. Get real-time updates on your request.");
        
        return "tickets/track";
    }
    
    private String generateTicketNumber() {
        return "TKT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}