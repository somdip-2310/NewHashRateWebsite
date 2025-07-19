package com.hashrate.util;

import com.hashrate.model.SeoMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class SeoUtils {
    
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final int MAX_SLUG_LENGTH = 50;
    
    public String generateSlug(String input) {
        if (!StringUtils.hasText(input)) {
            return "";
        }
        
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = slug.toLowerCase(Locale.ENGLISH);
        slug = slug.replaceAll("-{2,}", "-");
        slug = slug.replaceAll("^-|-$", "");
        
        if (slug.length() > MAX_SLUG_LENGTH) {
            slug = slug.substring(0, MAX_SLUG_LENGTH);
            slug = slug.replaceAll("-$", "");
        }
        
        return slug;
    }
    
    public String generateMetaDescription(String content, int maxLength) {
        if (!StringUtils.hasText(content)) {
            return "";
        }
        
        // Remove HTML tags
        String plainText = content.replaceAll("<[^>]*>", "");
        // Remove extra whitespaces
        plainText = plainText.replaceAll("\\s+", " ").trim();
        
        if (plainText.length() <= maxLength) {
            return plainText;
        }
        
        // Cut at the last complete word
        int lastSpace = plainText.lastIndexOf(' ', maxLength - 3);
        if (lastSpace > 0) {
            return plainText.substring(0, lastSpace) + "...";
        }
        
        return plainText.substring(0, maxLength - 3) + "...";
    }
    
    public SeoMetadata generateDefaultSeoMetadata(String title, String description, String imageUrl) {
        return SeoMetadata.builder()
                .metaTitle(truncateTitle(title, 60))
                .metaDescription(generateMetaDescription(description, 160))
                .metaKeywords(generateKeywords(title, description))
                .ogTitle(truncateTitle(title, 60))
                .ogDescription(generateMetaDescription(description, 160))
                .ogImage(imageUrl)
                .twitterCard("summary_large_image")
                .twitterTitle(truncateTitle(title, 60))
                .twitterDescription(generateMetaDescription(description, 160))
                .twitterImage(imageUrl)
                .robots("index, follow")
                .build();
    }
    
    private String truncateTitle(String title, int maxLength) {
        if (!StringUtils.hasText(title)) {
            return "";
        }
        
        if (title.length() <= maxLength) {
            return title;
        }
        
        return title.substring(0, maxLength - 3) + "...";
    }
    
    private String generateKeywords(String title, String description) {
        // Simple keyword generation - in production, use more sophisticated NLP
        String combined = (title + " " + description).toLowerCase();
        String[] words = combined.split("\\s+");
        
        // Remove common words and return top keywords
        StringBuilder keywords = new StringBuilder();
        int count = 0;
        for (String word : words) {
            if (word.length() > 3 && !isCommonWord(word) && count < 10) {
                if (keywords.length() > 0) {
                    keywords.append(", ");
                }
                keywords.append(word);
                count++;
            }
        }
        
        return keywords.toString();
    }
    
    private boolean isCommonWord(String word) {
        String[] commonWords = {"the", "and", "for", "with", "from", "this", "that", "have", "will", "your", "their"};
        for (String common : commonWords) {
            if (word.equalsIgnoreCase(common)) {
                return true;
            }
        }
        return false;
    }
    
    public String generateCanonicalUrl(String baseUrl, String path) {
        if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl + path;
    }
    
    public String generateSchemaMarkup(String type, Object data) {
        // Generate JSON-LD schema markup based on type
        // This is a simplified version - expand based on your needs
        return switch (type) {
            case "Product" -> generateProductSchema(data);
            case "Service" -> generateServiceSchema(data);
            case "Organization" -> generateOrganizationSchema();
            default -> "";
        };
    }
    
    private String generateProductSchema(Object data) {
        // Implement product schema generation
        return """
            {
              "@context": "https://schema.org",
              "@type": "Product",
              "name": "",
              "description": "",
              "brand": {
                "@type": "Brand",
                "name": "Hash Rate Communications"
              }
            }
            """;
    }
    
    private String generateServiceSchema(Object data) {
        // Implement service schema generation
        return """
            {
              "@context": "https://schema.org",
              "@type": "Service",
              "name": "",
              "description": "",
              "provider": {
                "@type": "Organization",
                "name": "Hash Rate Communications"
              }
            }
            """;
    }
    
    private String generateOrganizationSchema() {
        return """
            {
              "@context": "https://schema.org",
              "@type": "Organization",
              "name": "Hash Rate Communications",
              "url": "https://www.hashrate.in",
              "logo": "https://www.hashrate.in/images/logo.png",
              "contactPoint": {
                "@type": "ContactPoint",
                "telephone": "+91-9137455975",
                "contactType": "customer service"
              }
            }
            """;
    }
}