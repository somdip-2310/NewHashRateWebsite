package com.hashrate.controller;

import com.hashrate.dto.CareerApplicationDTO;
import com.hashrate.model.Career;
import com.hashrate.model.Career.Department;
import com.hashrate.service.CareerService;
import com.hashrate.service.ContactService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/careers")
@RequiredArgsConstructor
@Slf4j
public class CareerController {
    
	private static final Logger log = LoggerFactory.getLogger(CareerController.class);
	
    private final CareerService careerService;
    
    @Autowired
    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }
    
    @GetMapping
    public String index(@RequestParam(required = false) Department department,
                       @RequestParam(required = false) String location,
                       @PageableDefault(size = 10) Pageable pageable,
                       Model model) {
        log.debug("Loading careers page");
        
        Page<Career> careersPage = careerService.findAllActive(pageable);
        List<String> locations = careerService.findAllLocations();
        
        model.addAttribute("careers", careersPage.getContent());
        model.addAttribute("page", careersPage);
        model.addAttribute("departments", Department.values());
        model.addAttribute("locations", locations);
        model.addAttribute("urgentCount", careerService.countUrgent());
        
        // SEO
        model.addAttribute("pageTitle", "Careers - Join Our Team | Hash Rate Communications");
        model.addAttribute("pageDescription", "Build a future you believe in. Explore career opportunities in DCIM, iBMS, PLC & SCADA, Software Development, and more at Hash Rate Communications.");
        
        return "career/careers";
    }
    
    @GetMapping("/{jobId}")
    public String careerDetail(@PathVariable String jobId, Model model) {
        log.debug("Loading career detail for job ID: {}", jobId);
        
        Career career = careerService.findByJobId(jobId)
                .orElseThrow(() -> new RuntimeException("Career not found"));
        
        model.addAttribute("career", career);
        model.addAttribute("applicationForm", new CareerApplicationDTO());
        
        // Add related careers
        List<Career> relatedCareers = careerService.findByDepartment(career.getDepartment())
                .stream()
                .filter(c -> !c.getId().equals(career.getId()))
                .limit(3)
                .toList();
        model.addAttribute("relatedCareers", relatedCareers);
        
        // SEO from career metadata
        if (career.getSeoMetadata() != null) {
            model.addAttribute("pageTitle", career.getSeoMetadata().getMetaTitle());
            model.addAttribute("pageDescription", career.getSeoMetadata().getMetaDescription());
        }
        
        return "career/detail";
    }
    
    @PostMapping("/{jobId}/apply")
    public String applyForCareer(@PathVariable String jobId,
                                @Valid @ModelAttribute CareerApplicationDTO applicationForm,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        log.debug("Processing career application for job ID: {}", jobId);
        
        if (bindingResult.hasErrors()) {
            Career career = careerService.findByJobId(jobId)
                    .orElseThrow(() -> new RuntimeException("Career not found"));
            model.addAttribute("career", career);
            return "career/detail";
        }
        
        try {
            careerService.applyForCareer(jobId, applicationForm);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Thank you for your application! We will review it and get back to you soon.");
            return "redirect:/careers/" + jobId + "/success";
        } catch (IOException e) {
            log.error("Error processing career application", e);
            redirectAttributes.addFlashAttribute("errorMessage", 
                    "There was an error processing your application. Please try again.");
            return "redirect:/careers/" + jobId;
        }
    }
    
    @GetMapping("/{jobId}/success")
    public String applicationSuccess(@PathVariable String jobId, Model model) {
        log.debug("Showing application success page for job ID: {}", jobId);
        
        Career career = careerService.findByJobId(jobId)
                .orElseThrow(() -> new RuntimeException("Career not found"));
        
        model.addAttribute("career", career);
        
        // SEO
        model.addAttribute("pageTitle", "Application Submitted Successfully | Hash Rate Communications");
        model.addAttribute("pageDescription", "Your job application has been submitted successfully. We will review it and get back to you soon.");
        
        return "career/success";
    }
}