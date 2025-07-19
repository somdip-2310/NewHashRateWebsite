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
    
    public String generateSitemapIndex(List<String> sitemapUrls) {
        StringWriter writer = new StringWriter();
        
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        
        for (String sitemapUrl : sitemapUrls) {
            writer.write("  <sitemap>\n");
            writer.write("    <loc>" + escapeXml(baseUrl + sitemapUrl) + "</loc>\n");
            writer.write("    <lastmod>" + LocalDateTime.now().format(DATE_FORMATTER) + "</lastmod>\n");
            writer.write("  </sitemap>\n");
        }
        
        writer.write("</sitemapindex>");
        
        return writer.toString();
    }
    
    private String escapeXml(String text) {
        if (text == null) {
            return "";
        }
        
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
    
    public List<SitemapEntry> generateDefaultEntries() {
        List<SitemapEntry> entries = new ArrayList<>();
        
        // Static pages
        entries.add(new SitemapEntry("/", LocalDateTime.now(), "daily", 1.0));
        entries.add(new SitemapEntry("/products", LocalDateTime.now(), "weekly", 0.9));
        entries.add(new SitemapEntry("/solutions", LocalDateTime.now(), "weekly", 0.9));
        entries.add(new SitemapEntry("/services", LocalDateTime.now(), "weekly", 0.8));
        entries.add(new SitemapEntry("/projects", LocalDateTime.now(), "monthly", 0.7));
        entries.add(new SitemapEntry("/careers", LocalDateTime.now(), "weekly", 0.8));
        entries.add(new SitemapEntry("/contact", LocalDateTime.now(), "monthly", 0.6));
        entries.add(new SitemapEntry("/about", LocalDateTime.now(), "monthly", 0.6));
        
        return entries;
    }
    
    public static class SitemapEntry {
        private final String path;
        private final LocalDateTime lastModified;
        private final String changeFrequency;
        private final Double priority;
        
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