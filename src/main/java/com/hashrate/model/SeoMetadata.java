package com.hashrate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
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

    // Constructors
    public SeoMetadata() {}

    public SeoMetadata(String metaTitle, String metaDescription, String metaKeywords, 
                       String ogTitle, String ogDescription, String ogImage, String twitterCard, 
                       String twitterTitle, String twitterDescription, String twitterImage, 
                       String canonicalUrl, String robots, String schemaMarkup) {
        this.metaTitle = metaTitle;
        this.metaDescription = metaDescription;
        this.metaKeywords = metaKeywords;
        this.ogTitle = ogTitle;
        this.ogDescription = ogDescription;
        this.ogImage = ogImage;
        this.twitterCard = twitterCard;
        this.twitterTitle = twitterTitle;
        this.twitterDescription = twitterDescription;
        this.twitterImage = twitterImage;
        this.canonicalUrl = canonicalUrl;
        this.robots = robots;
        this.schemaMarkup = schemaMarkup;
    }

    // Builder pattern implementation
    public static SeoMetadataBuilder builder() {
        return new SeoMetadataBuilder();
    }

    public static class SeoMetadataBuilder {
        private String metaTitle;
        private String metaDescription;
        private String metaKeywords;
        private String ogTitle;
        private String ogDescription;
        private String ogImage;
        private String twitterCard = "summary_large_image";
        private String twitterTitle;
        private String twitterDescription;
        private String twitterImage;
        private String canonicalUrl;
        private String robots = "index, follow";
        private String schemaMarkup;

        public SeoMetadataBuilder metaTitle(String metaTitle) {
            this.metaTitle = metaTitle;
            return this;
        }

        public SeoMetadataBuilder metaDescription(String metaDescription) {
            this.metaDescription = metaDescription;
            return this;
        }

        public SeoMetadataBuilder metaKeywords(String metaKeywords) {
            this.metaKeywords = metaKeywords;
            return this;
        }

        public SeoMetadataBuilder ogTitle(String ogTitle) {
            this.ogTitle = ogTitle;
            return this;
        }

        public SeoMetadataBuilder ogDescription(String ogDescription) {
            this.ogDescription = ogDescription;
            return this;
        }

        public SeoMetadataBuilder ogImage(String ogImage) {
            this.ogImage = ogImage;
            return this;
        }

        public SeoMetadataBuilder twitterCard(String twitterCard) {
            this.twitterCard = twitterCard;
            return this;
        }

        public SeoMetadataBuilder twitterTitle(String twitterTitle) {
            this.twitterTitle = twitterTitle;
            return this;
        }

        public SeoMetadataBuilder twitterDescription(String twitterDescription) {
            this.twitterDescription = twitterDescription;
            return this;
        }

        public SeoMetadataBuilder twitterImage(String twitterImage) {
            this.twitterImage = twitterImage;
            return this;
        }

        public SeoMetadataBuilder canonicalUrl(String canonicalUrl) {
            this.canonicalUrl = canonicalUrl;
            return this;
        }

        public SeoMetadataBuilder robots(String robots) {
            this.robots = robots;
            return this;
        }

        public SeoMetadataBuilder schemaMarkup(String schemaMarkup) {
            this.schemaMarkup = schemaMarkup;
            return this;
        }

        public SeoMetadata build() {
            SeoMetadata seoMetadata = new SeoMetadata();
            seoMetadata.metaTitle = this.metaTitle;
            seoMetadata.metaDescription = this.metaDescription;
            seoMetadata.metaKeywords = this.metaKeywords;
            seoMetadata.ogTitle = this.ogTitle;
            seoMetadata.ogDescription = this.ogDescription;
            seoMetadata.ogImage = this.ogImage;
            seoMetadata.twitterCard = this.twitterCard;
            seoMetadata.twitterTitle = this.twitterTitle;
            seoMetadata.twitterDescription = this.twitterDescription;
            seoMetadata.twitterImage = this.twitterImage;
            seoMetadata.canonicalUrl = this.canonicalUrl;
            seoMetadata.robots = this.robots;
            seoMetadata.schemaMarkup = this.schemaMarkup;
            return seoMetadata;
        }
    }

    // Getters and Setters
    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getOgImage() {
        return ogImage;
    }

    public void setOgImage(String ogImage) {
        this.ogImage = ogImage;
    }

    public String getTwitterCard() {
        return twitterCard;
    }

    public void setTwitterCard(String twitterCard) {
        this.twitterCard = twitterCard;
    }

    public String getTwitterTitle() {
        return twitterTitle;
    }

    public void setTwitterTitle(String twitterTitle) {
        this.twitterTitle = twitterTitle;
    }

    public String getTwitterDescription() {
        return twitterDescription;
    }

    public void setTwitterDescription(String twitterDescription) {
        this.twitterDescription = twitterDescription;
    }

    public String getTwitterImage() {
        return twitterImage;
    }

    public void setTwitterImage(String twitterImage) {
        this.twitterImage = twitterImage;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public String getRobots() {
        return robots;
    }

    public void setRobots(String robots) {
        this.robots = robots;
    }

    public String getSchemaMarkup() {
        return schemaMarkup;
    }

    public void setSchemaMarkup(String schemaMarkup) {
        this.schemaMarkup = schemaMarkup;
    }
}