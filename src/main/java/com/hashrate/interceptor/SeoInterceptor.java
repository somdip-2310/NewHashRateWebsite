package com.hashrate.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SeoInterceptor implements HandlerInterceptor {
    
    @Value("${app.seo.default-title:Hash Rate Communications - Technology Simplified}")
    private String defaultTitle;
    
    @Value("${app.seo.default-description:Leading provider of DCIM, iBMS, PLC & SCADA solutions}")
    private String defaultDescription;
    
    @Value("${app.seo.default-keywords:DCIM,iBMS,PLC,SCADA,datacenter,software}")
    private String defaultKeywords;
    
    @Value("${app.base-url:https://www.hashrate.in}")
    private String baseUrl;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        // Add SEO-friendly headers
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // Add canonical URL header
        String canonicalUrl = baseUrl + request.getRequestURI();
        response.setHeader("Link", "<" + canonicalUrl + ">; rel=\"canonical\"");
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                          ModelAndView modelAndView) throws Exception {
        
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
            // Add default SEO data if not already present
            if (!modelAndView.getModel().containsKey("pageTitle")) {
                modelAndView.addObject("pageTitle", defaultTitle);
            }
            
            if (!modelAndView.getModel().containsKey("pageDescription")) {
                modelAndView.addObject("pageDescription", defaultDescription);
            }
            
            if (!modelAndView.getModel().containsKey("pageKeywords")) {
                modelAndView.addObject("pageKeywords", defaultKeywords);
            }
            
            if (!modelAndView.getModel().containsKey("canonicalUrl")) {
                modelAndView.addObject("canonicalUrl", baseUrl + request.getRequestURI());
            }
            
            // Add base URL for use in templates
            modelAndView.addObject("baseUrl", baseUrl);
            
            // Add current year for copyright
            modelAndView.addObject("currentYear", java.time.Year.now().getValue());
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, 
                               Exception ex) throws Exception {
        // Log any SEO-related issues
        if (ex != null) {
            // Log error for monitoring
        }
    }
}