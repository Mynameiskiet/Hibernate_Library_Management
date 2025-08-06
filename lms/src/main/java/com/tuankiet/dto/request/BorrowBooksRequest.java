package com.tuankiet.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class BorrowBooksRequest {
    
    @NotNull(message = "Member ID is required")
    private Long memberId;
    
    @NotEmpty(message = "At least one book must be selected")
    private Set<Long> bookIds;
    
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
    
    // Constructors
    public BorrowBooksRequest() {}
    
    public BorrowBooksRequest(Long memberId, Set<Long> bookIds, LocalDate dueDate) {
        this.memberId = memberId;
        this.bookIds = bookIds;
        this.dueDate = dueDate;
    }
    
    // Getters and Setters
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    
    public Set<Long> getBookIds() { return bookIds; }
    public void setBookIds(Set<Long> bookIds) { this.bookIds = bookIds; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
