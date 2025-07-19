package com.hashrate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
@Slf4j
public class BlogController {
    
    @GetMapping
    public String index(Model model) {
        log.debug("Loading blog page");
        
        // For now, redirect to home or show coming soon
        // In future, implement blog functionality
        
        // SEO
        model.addAttribute("pageTitle", "Blog - Latest Updates & Insights | Hash Rate Communications");
        model.addAttribute("pageDescription", "Stay updated with the latest trends in DCIM, iBMS, PLC & SCADA technologies. Expert insights and industry news from Hash Rate Communications.");
        
        return "blog/coming-soon";
    }
}