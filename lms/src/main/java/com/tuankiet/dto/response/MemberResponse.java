package com.tuankiet.dto.response;

import java.time.LocalDateTime;

public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdDate;
    private Long version;
    private int activeBorrowingsCount;
    
    // Constructors
    public MemberResponse() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public int getActiveBorrowingsCount() { return activeBorrowingsCount; }
    public void setActiveBorrowingsCount(int activeBorrowingsCount) { this.activeBorrowingsCount = activeBorrowingsCount; }
}
