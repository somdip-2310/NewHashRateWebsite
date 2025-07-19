package com.hashrate.service;

import com.hashrate.model.*;
import com.hashrate.util.SeoUtils;
import com.hashrate.util.SitemapGenerator;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SeoService {
    
    private static final Logger log = LoggerFactory.getLogger(SeoService.class);
    
    private final SeoUtils seoUtils;
    private final SitemapGenerator sitemapGenerator;
    private final ProductService productService;
    private final SolutionService solutionService;
    private final ServiceManagementService serviceManagementService;
    private final ProjectService projectService;
    private final CareerService careerService;
    
    @Value("${app.base-url:https://www.hashrate.in}")
    private String baseUrl;
    
    @Value("${app.company.name:Hash Rate Communications}")
    private String companyName;

    @Autowired
    public SeoService(SeoUtils seoUtils,
                     SitemapGenerator sitemapGenerator,
                     ProductService productService,
                     SolutionService solutionService,
                     ServiceManagementService serviceManagementService,
                     ProjectService projectService,
                     CareerService careerService) {
        this.seoUtils = seoUtils;
        this.sitemapGenerator = sitemapGenerator;
        this.productService = productService;
        this.solutionService = solutionService;
        this.serviceManagementService = serviceManagementService;
        this.projectService = projectService;
        this.careerService = careerService;
    }
    
    public SeoMetadata generateProductSeoMetadata(Product product) {
        String title = product.getName() + " - " + companyName;
        String description = seoUtils.generateMetaDescription(product.getShortDescription(), 160);
        
        return seoUtils.generateDefaultSeoMetadata(title, description, product.getImageUrl());
    }
    
    public SeoMetadata generateSolutionSeoMetadata(Solution solution) {
        String title = solution.getTitle() + " Solution - " + companyName;
        String description = seoUtils.generateMetaDescription(solution.getShortDescription(), 160);
        
        return seoUtils.generateDefaultSeoMetadata(title, description, solution.getImageUrl());
    }
    
    public SeoMetadata generateServiceSeoMetadata(com.hashrate.model.Service service) {
        String title = service.getName() + " Service - " + companyName;
        String description = seoUtils.generateMetaDescription(service.getShortDescription(), 160);
        
        return seoUtils.generateDefaultSeoMetadata(title, description, service.getImageUrl());
    }
    
    public SeoMetadata generateProjectSeoMetadata(Project project) {
        String title = project.getProjectName() + " - " + project.getClientName() + " | " + companyName;
        String description = seoUtils.generateMetaDescription(project.getDescription(), 160);
        
        return seoUtils.generateDefaultSeoMetadata(title, description, project.getClientLogoUrl());
    }
    
    public SeoMetadata generateCareerSeoMetadata(Career career) {
        String title = career.getTitle() + " - Careers at " + companyName;
        String description = career.getTitle() + " position available at " + companyName + 
                           " in " + career.getLocation() + ". " + 
                           career.getExperienceLevel() + " level, " + career.getJobType() + ".";
        
        return seoUtils.generateDefaultSeoMetadata(title, description, null);
    }
    
    @Cacheable(value = "sitemap", key = "'main'")
    public String generateSitemap() {
        log.info("Generating main sitemap");
        
        List<SitemapGenerator.SitemapEntry> entries = new ArrayList<>();
        
        // Add static pages
        entries.addAll(sitemapGenerator.generateDefaultEntries());
        
        // Add dynamic pages
        entries.addAll(generateProductSitemapEntries());
        entries.addAll(generateSolutionSitemapEntries());
        entries.addAll(generateServiceSitemapEntries());
        entries.addAll(generateCareerSitemapEntries());
        
        return sitemapGenerator.generateSitemap(entries);
    }
    
    private List<SitemapGenerator.SitemapEntry> generateProductSitemapEntries() {
        List<SitemapGenerator.SitemapEntry> entries = new ArrayList<>();
        
        productService.findAllActive().forEach(product -> {
            entries.add(new SitemapGenerator.SitemapEntry(
                    "/products/" + product.getSlug(),
                    product.getUpdatedAt(),
                    "weekly",
                    0.8
            ));
        });
        
        return entries;
    }
    
    private List<SitemapGenerator.SitemapEntry> generateSolutionSitemapEntries() {
        List<SitemapGenerator.SitemapEntry> entries = new ArrayList<>();
        
        solutionService.findAllActive().forEach(solution -> {
            entries.add(new SitemapGenerator.SitemapEntry(
                    "/solutions/" + solution.getSlug(),
                    solution.getUpdatedAt(),
                    "weekly",
                    0.9
            ));
        });
        
        return entries;
    }
    
    private List<SitemapGenerator.SitemapEntry> generateServiceSitemapEntries() {
        List<SitemapGenerator.SitemapEntry> entries = new ArrayList<>();
        
        serviceManagementService.findAllActive().forEach(service -> {
            entries.add(new SitemapGenerator.SitemapEntry(
                    "/services/" + service.getSlug(),
                    service.getUpdatedAt(),
                    "weekly",
                    0.8
            ));
        });
        
        return entries;
    }
    
    private List<SitemapGenerator.SitemapEntry> generateCareerSitemapEntries() {
        List<SitemapGenerator.SitemapEntry> entries = new ArrayList<>();
        
        careerService.findActiveNotExpired().forEach(career -> {
            entries.add(new SitemapGenerator.SitemapEntry(
                    "/careers/" + career.getJobId(),
                    career.getUpdatedAt(),
                    "daily",
                    0.7
            ));
        });
        
        return entries;
    }
    
    public String generateRobotsTxt() {
        return """
                User-agent: *
                Allow: /
                Disallow: /admin/
                Disallow: /api/
                Disallow: /uploads/
                Disallow: /*.pdf$
                
                Sitemap: %s/sitemap.xml
                """.formatted(baseUrl);
    }
    
    public String generateStructuredData(String type, Object data) {
        return seoUtils.generateSchemaMarkup(type, data);
    }
}