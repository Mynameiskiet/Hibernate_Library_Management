package com.tuankiet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class BookUpdateRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private Boolean available;
    
    private Set<Long> authorIds;
    
    @NotNull(message = "Version is required for optimistic locking")
    private Long version;
    
    // Constructors
    public BookUpdateRequest() {}
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    
    public Set<Long> getAuthorIds() { return authorIds; }
    public void setAuthorIds(Set<Long> authorIds) { this.authorIds = authorIds; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
