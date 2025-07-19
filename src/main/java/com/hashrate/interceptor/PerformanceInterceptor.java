package com.hashrate.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.TimeUnit;

@Component
public class PerformanceInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceInterceptor.class);
    
    private static final String START_TIME_ATTRIBUTE = "startTime";
    private static final long SLOW_REQUEST_THRESHOLD_MS = 1000; // 1 second
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        // Record start time
        request.setAttribute(START_TIME_ATTRIBUTE, System.nanoTime());
        
        // Add performance headers
        response.setHeader("X-Response-Time", "0");
        
        // Enable compression
        response.setHeader("Content-Encoding", "gzip");
        
        // Cache control for static resources
        String uri = request.getRequestURI();
        if (isStaticResource(uri)) {
            // Cache static resources for 1 year
            response.setHeader("Cache-Control", "public, max-age=31536000, immutable");
        } else if (isApiEndpoint(uri)) {
            // No cache for API endpoints
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
        } else {
            // Cache HTML pages for 1 hour
            response.setHeader("Cache-Control", "public, max-age=3600");
        }
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                          ModelAndView modelAndView) throws Exception {
        
        // Calculate response time
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime != null) {
            long endTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            
            // Update response header
            response.setHeader("X-Response-Time", duration + "ms");
            
            // Add to model for display in footer (optional)
            if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
                modelAndView.addObject("responseTime", duration);
            }
            
            // Log slow requests
            if (duration > SLOW_REQUEST_THRESHOLD_MS) {
                logger.warn("Slow request detected: {} took {}ms", request.getRequestURI(), duration);
            }
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, 
                               Exception ex) throws Exception {
        
        // Final logging
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime != null) {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            
            if (ex != null) {
                logger.error("Request {} failed after {}ms", request.getRequestURI(), duration, ex);
            } else {
                logger.debug("Request {} completed in {}ms", request.getRequestURI(), duration);
            }
        }
        
        // Clean up
        request.removeAttribute(START_TIME_ATTRIBUTE);
    }
    
    private boolean isStaticResource(String uri) {
        return uri.matches(".*\\.(css|js|jpg|jpeg|png|gif|webp|svg|ico|woff|woff2|ttf|eot)$");
    }
    
    private boolean isApiEndpoint(String uri) {
        return uri.startsWith("/api/");
    }
}