package com.hashrate.controller;

import com.hashrate.model.Solution;
import com.hashrate.service.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/solutions")
@RequiredArgsConstructor
@Slf4j
public class SolutionController {
    
	private static final Logger log = LoggerFactory.getLogger(SolutionController.class);
	
	
    private final SolutionService solutionService;
    
    @GetMapping
    public String index(Model model) {
        log.debug("Loading solutions page");
        
        List<Solution> solutions = solutionService.findAllActive();
        model.addAttribute("solutions", solutions);
        
        // SEO
        model.addAttribute("pageTitle", "Solutions - Central Dashboard, Asset Tracking, CPM | Hash Rate Communications");
        model.addAttribute("pageDescription", "Innovative technology solutions including Centralized Dashboard, Real-time Asset Tracking, Chiller Plant Management, and more.");
        
        return "solutions/solutions-list";
    }
    
    @GetMapping("/central-dashboard")
    public String centralDashboard(Model model) {
        log.debug("Loading central dashboard solution page");
        
        Solution solution = solutionService.findBySlug("central-dashboard")
                .orElseGet(() -> createDefaultSolution("central-dashboard"));
        
        model.addAttribute("solution", solution);
        
        // SEO
        model.addAttribute("pageTitle", "Centralized Dashboard & Control - Real-time Business Intelligence | Hash Rate Communications");
        model.addAttribute("pageDescription", "Give leaders real-time insights for confident, risk-informed decisions. Our centralized dashboard provides comprehensive business intelligence and automated reporting.");
        
        return "solutions/central-dashboard";
    }
    
    @GetMapping("/asset-tracking")
    public String assetTracking(Model model) {
        log.debug("Loading asset tracking solution page");
        
        Solution solution = solutionService.findBySlug("asset-tracking")
                .orElseGet(() -> createDefaultSolution("asset-tracking"));
        
        model.addAttribute("solution", solution);
        
        // SEO
        model.addAttribute("pageTitle", "Real-time Asset Tracking - RFID Server Management | Hash Rate Communications");
        model.addAttribute("pageDescription", "Advanced RFID-based asset tracking solution for data centers. Monitor server locations, manage inventory, and streamline audits with real-time tracking.");
        
        return "solutions/asset-tracking";
    }
    
    @GetMapping("/chiller-plant-management")
    public String chillerPlantManagement(Model model) {
        log.debug("Loading chiller plant management solution page");
        
        Solution solution = solutionService.findBySlug("chiller-plant-management")
                .orElseGet(() -> createDefaultSolution("chiller-plant-management"));
        
        model.addAttribute("solution", solution);
        
        // SEO
        model.addAttribute("pageTitle", "Chiller Plant Management System - Energy Efficiency & Control | Hash Rate Communications");
        model.addAttribute("pageDescription", "Controls & monitors chiller plant operations in real-time. Optimize energy efficiency, reduce costs, and ensure reliable cooling performance.");
        
        return "solutions/cpm";
    }
    
    @GetMapping("/{slug}")
    public String solutionDetail(@PathVariable String slug, Model model) {
        log.debug("Loading solution detail for slug: {}", slug);
        
        Solution solution = solutionService.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Solution not found"));
        
        model.addAttribute("solution", solution);
        
        // Add related solutions
        List<Solution> relatedSolutions = solutionService.findAllActive()
                .stream()
                .filter(s -> !s.getId().equals(solution.getId()))
                .limit(3)
                .toList();
        model.addAttribute("relatedSolutions", relatedSolutions);
        
        // SEO from solution metadata
        if (solution.getSeoMetadata() != null) {
            model.addAttribute("pageTitle", solution.getSeoMetadata().getMetaTitle());
            model.addAttribute("pageDescription", solution.getSeoMetadata().getMetaDescription());
            model.addAttribute("pageKeywords", solution.getSeoMetadata().getMetaKeywords());
        }
        
        return "solutions/detail";
    }
    
    private Solution createDefaultSolution(String slug) {
        // Create a default solution object for display
        // In production, this should redirect to 404
        return Solution.builder()
                .slug(slug)
                .title("Solution")
                .description("Solution description")
                .build();
    }
}