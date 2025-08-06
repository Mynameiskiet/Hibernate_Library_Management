package com.tuankiet.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public class BookCreateRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private Set<Long> authorIds;
    
    // Constructors
    public BookCreateRequest() {}
    
    public BookCreateRequest(String title, String category, Set<Long> authorIds) {
        this.title = title;
        this.category = category;
        this.authorIds = authorIds;
    }
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Set<Long> getAuthorIds() { return authorIds; }
    public void setAuthorIds(Set<Long> authorIds) { this.authorIds = authorIds; }
}
