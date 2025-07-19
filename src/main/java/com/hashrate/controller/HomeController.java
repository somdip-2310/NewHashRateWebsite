package com.hashrate.controller;

import com.hashrate.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final ProductService productService;
    private final SolutionService solutionService;
    private final ServiceManagementService serviceManagementService;
    private final ProjectService projectService;
    private final CareerService careerService;

    @Autowired
    public HomeController(ProductService productService, 
                         SolutionService solutionService, 
                         ServiceManagementService serviceManagementService, 
                         ProjectService projectService, 
                         CareerService careerService) {
        this.productService = productService;
        this.solutionService = solutionService;
        this.serviceManagementService = serviceManagementService;
        this.projectService = projectService;
        this.careerService = careerService;
    }
    @GetMapping("/")
    public String index(Model model) {
        log.debug("Loading home page");
        
        // Add featured content
        model.addAttribute("featuredSolutions", solutionService.findTopFeatured(3));
        model.addAttribute("featuredServices", serviceManagementService.findTopFeatured(6));
        model.addAttribute("featuredProjects", projectService.findTopFeatured(4));
        model.addAttribute("urgentCareers", careerService.findUrgent());
        
        // Add statistics
        model.addAttribute("projectCount", projectService.countActive());
        model.addAttribute("clientCount", projectService.countClients());
        model.addAttribute("serviceCount", serviceManagementService.countActive());
        model.addAttribute("solutionCount", solutionService.countActive());
        
        // SEO
        model.addAttribute("pageTitle", "Hash Rate Communications - Technology Simplified | DCIM, iBMS, PLC & SCADA Solutions");
        model.addAttribute("pageDescription", "Leading provider of Datacenter Infrastructure Management (DCIM), Intelligent Building Management System (iBMS), PLC & SCADA solutions. Trusted by 70+ clients nationwide.");
        model.addAttribute("pageKeywords", "DCIM, iBMS, PLC, SCADA, datacenter, building management system, automation, Hash Rate Communications");
        
        return "home/index";
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        log.debug("Loading about page");
        
        // Add company information
        model.addAttribute("projectCount", projectService.countActive());
        model.addAttribute("clientCount", projectService.countClients());
        model.addAttribute("yearsInBusiness", 15);
        model.addAttribute("teamSize", 50);
        
        // SEO
        model.addAttribute("pageTitle", "About Us - Hash Rate Communications | Industry Leaders in DCIM & Automation");
        model.addAttribute("pageDescription", "Learn about Hash Rate Communications, a Kiboko Group company specializing in DCIM, iBMS, PLC & SCADA solutions with 15+ years of experience.");
        
        return "about/about";
    }
    
    @GetMapping("/sitemap.xml")
    public String sitemap() {
        return "sitemap";
    }
    
    @GetMapping("/robots.txt")
    public String robots() {
        return "robots";
    }
}