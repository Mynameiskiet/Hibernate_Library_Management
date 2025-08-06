package com.tuankiet.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public class BookResponse {
    private Long id;
    private String title;
    private String category;
    private Boolean available;
    private LocalDateTime createdDate;
    private Long version;
    private Set<AuthorResponse> authors;
    
    // Constructors
    public BookResponse() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public Set<AuthorResponse> getAuthors() { return authors; }
    public void setAuthors(Set<AuthorResponse> authors) { this.authors = authors; }
}
