package com.hashrate.controller;

import com.hashrate.model.Service;
import com.hashrate.service.ProjectService;
import com.hashrate.service.ServiceManagementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/services")
public class ServiceController {
    
	private static final Logger log = LoggerFactory.getLogger(ServiceController.class);
	
	private final ServiceManagementService serviceManagementService;
	
	@Autowired
    public ServiceController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }
    
    @GetMapping
    public String index(Model model) {
        log.debug("Loading services page");
        
        List<Service> services = serviceManagementService.findAllActive();
        model.addAttribute("services", services);
        
        // Group services by category
        model.addAttribute("categories", serviceManagementService.findAllCategories());
        
        // SEO
        model.addAttribute("pageTitle", "Services - Project Management, DCIM, BMS Operations | Hash Rate Communications");
        model.addAttribute("pageDescription", "End-to-end services including project management, BMS operations, DCIM implementation, hardware supply, and maintenance support across India.");
        
        return "services/services";
    }
    
    @GetMapping("/{slug}")
    public String serviceDetail(@PathVariable String slug, Model model) {
        log.debug("Loading service detail for slug: {}", slug);
        
        Service service = serviceManagementService.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        
        model.addAttribute("service", service);
        
        // Add related services
        List<Service> relatedServices = serviceManagementService.findByCategory(service.getCategory())
                .stream()
                .filter(s -> !s.getId().equals(service.getId()))
                .limit(3)
                .toList();
        model.addAttribute("relatedServices", relatedServices);
        
        // SEO from service metadata
        if (service.getSeoMetadata() != null) {
            model.addAttribute("pageTitle", service.getSeoMetadata().getMetaTitle());
            model.addAttribute("pageDescription", service.getSeoMetadata().getMetaDescription());
            model.addAttribute("pageKeywords", service.getSeoMetadata().getMetaKeywords());
        }
        
        return "services/detail";
    }
}