package com.hashrate.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class SitemapGenerator {
    
    @Value("${app.base-url:https://www.hashrate.in}")
    private String baseUrl;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public String generateSitemap(List<SitemapEntry> entries) {
        StringWriter writer = new StringWriter();
        
        // XML declaration and namespace
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        
        // Add entries
        for (SitemapEntry entry : entries) {
            writer.write("  <url>\n");
            writer.write("    <loc>" + escapeXml(baseUrl + entry.getPath()) + "</loc>\n");
            
            if (entry.getLastModified() != null) {
                writer.write("    <lastmod>" + entry.getLastModified().format(DATE_FORMATTER) + "</lastmod>\n");
            }
            
            if (entry.getChangeFrequency() != null) {
                writer.write("    <changefreq>" + entry.getChangeFrequency() + "</changefreq>\n");
            }
            
            if (entry.getPriority() != null) {
            	writer.write("    <priority>" + entry.getPriority() + "</priority>\n");
            }
            
            writer.write("  </url>\n");
        }
        
        writer.write("</urlset>");
        return writer.toString();
    }

    public List<SitemapEntry> generateDefaultEntries() {
        List<SitemapEntry> entries = new ArrayList<>();
        
        // Add static pages
        entries.add(new SitemapEntry("/", LocalDateTime.now(), "daily", 1.0));
        entries.add(new SitemapEntry("/about", LocalDateTime.now(), "monthly", 0.8));
        entries.add(new SitemapEntry("/products", LocalDateTime.now(), "weekly", 0.9));
        entries.add(new SitemapEntry("/solutions", LocalDateTime.now(), "weekly", 0.9));
        entries.add(new SitemapEntry("/services", LocalDateTime.now(), "weekly", 0.9));
        entries.add(new SitemapEntry("/projects", LocalDateTime.now(), "weekly", 0.8));
        entries.add(new SitemapEntry("/careers", LocalDateTime.now(), "daily", 0.7));
        entries.add(new SitemapEntry("/contact", LocalDateTime.now(), "monthly", 0.6));
        
        return entries;
    }

    private String escapeXml(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
               .replace("<", "&lt;")
               .replace(">", "&gt;")
               .replace("\"", "&quot;")
               .replace("'", "&apos;");
    }

    public static class SitemapEntry {
        private String path;
        private LocalDateTime lastModified;
        private String changeFrequency;
        private Double priority;
        
        public SitemapEntry(String path, LocalDateTime lastModified, String changeFrequency, Double priority) {
            this.path = path;
            this.lastModified = lastModified;
            this.changeFrequency = changeFrequency;
            this.priority = priority;
        }
        
        // Getters
        public String getPath() { return path; }
        public LocalDateTime getLastModified() { return lastModified; }
        public String getChangeFrequency() { return changeFrequency; }
        public Double getPriority() { return priority; }
    }
}