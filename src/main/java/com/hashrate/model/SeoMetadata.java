package com.hashrate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeoMetadata {
    
    @Column(name = "meta_title")
    private String metaTitle;
    
    @Column(name = "meta_description", length = 500)
    private String metaDescription;
    
    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;
    
    @Column(name = "og_title")
    private String ogTitle;
    
    @Column(name = "og_description", length = 500)
    private String ogDescription;
    
    @Column(name = "og_image")
    private String ogImage;
    
    @Column(name = "twitter_card")
    private String twitterCard = "summary_large_image";
    
    @Column(name = "twitter_title")
    private String twitterTitle;
    
    @Column(name = "twitter_description", length = 500)
    private String twitterDescription;
    
    @Column(name = "twitter_image")
    private String twitterImage;
    
    @Column(name = "canonical_url")
    private String canonicalUrl;
    
    @Column(name = "robots")
    private String robots = "index, follow";
    
    @Column(name = "schema_markup", columnDefinition = "TEXT")
    private String schemaMarkup;
}