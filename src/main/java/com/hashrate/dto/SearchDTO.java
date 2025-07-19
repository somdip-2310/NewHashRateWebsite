package com.hashrate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDTO {
    
    private String query;
    private String category;
    private List<String> filters;
    private String sortBy;
    private String sortDirection;
    private Integer page;
    private Integer size;
    
    // Search results
    private List<SearchResultItem> results;
    private Long totalResults;
    private Integer totalPages;
    private Integer currentPage;
    
    // Facets for filtering
    private Map<String, List<FacetItem>> facets;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SearchResultItem {
        private String type; // product, solution, service, etc.
        private Long id;
        private String title;
        private String description;
        private String url;
        private String imageUrl;
        private Double relevanceScore;
        private Map<String, Object> additionalData;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FacetItem {
        private String label;
        private String value;
        private Long count;
        private Boolean selected;
    }
}