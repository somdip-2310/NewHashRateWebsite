package com.hashrate.controller;

import com.hashrate.model.Project;
import com.hashrate.model.Project.ProjectCategory;
import com.hashrate.service.ProductService;
import com.hashrate.service.ProjectService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    
	private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
	
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    @GetMapping
    public String index(@RequestParam(required = false) ProjectCategory category,
                       @PageableDefault(size = 20) Pageable pageable,
                       Model model) {
        log.debug("Loading projects page with category: {}", category);
        
        Page<Project> projectsPage;
        if (category != null) {
            projectsPage = projectService.findByCategory(category, pageable);
            model.addAttribute("selectedCategory", category);
        } else {
            projectsPage = projectService.findAllActive(pageable);
        }
        
        model.addAttribute("projects", projectsPage.getContent());
        model.addAttribute("page", projectsPage);
        model.addAttribute("categories", ProjectCategory.values());
        model.addAttribute("clients", projectService.findAllClients());
        
        // SEO
        model.addAttribute("pageTitle", "Our Projects - 70+ Successful Implementations | Hash Rate Communications");
        model.addAttribute("pageDescription", "Explore our portfolio of successful projects across airports, metro, BFSI, government, oil & gas, and datacenter sectors. Trusted by leading organizations nationwide.");
        
        return "projects/projects";
    }
    
    @GetMapping("/clients")
    public String clients(Model model) {
        log.debug("Loading clients page");
        
        List<String> clients = projectService.findAllClients();
        model.addAttribute("clients", clients);
        model.addAttribute("clientCount", clients.size());
        model.addAttribute("projectCount", projectService.countActive());
        
        // SEO
        model.addAttribute("pageTitle", "Our Clients - Trusted by 70+ Leading Organizations | Hash Rate Communications");
        model.addAttribute("pageDescription", "Our prestigious client list includes major airports, metro projects, banks, government organizations, and Fortune 500 companies across India.");
        
        return "projects/clients";
    }
}