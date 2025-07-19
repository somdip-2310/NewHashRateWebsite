package com.hashrate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SearchDTO {
    
    @NotBlank(message = "Search query is required")
    @Size(min = 2, max = 100, message = "Search query must be between 2 and 100 characters")
    private String query;
    
    private String category;
    private String sortBy = "relevance";
    private Integer page = 0;
    private Integer size = 10;

    // Constructors
    public SearchDTO() {}

    public SearchDTO(String query, String category, String sortBy, Integer page, Integer size) {
        this.query = query;
        this.category = category;
        this.sortBy = sortBy;
        this.page = page;
        this.size = size;
    }

    // Getters and Setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}