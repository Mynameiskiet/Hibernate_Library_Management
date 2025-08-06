package com.tuankiet.dto.response;

import com.tuankiet.entities.BorrowingStatus;
import java.time.LocalDate;

public class BorrowingResponse {
    private Long id;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowingStatus status;
    private Long version;
    private MemberResponse member;
    private BookResponse book;
    private boolean overdue;
    
    // Constructors
    public BorrowingResponse() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public BorrowingStatus getStatus() { return status; }
    public void setStatus(BorrowingStatus status) { this.status = status; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public MemberResponse getMember() { return member; }
    public void setMember(MemberResponse member) { this.member = member; }
    
    public BookResponse getBook() { return book; }
    public void setBook(BookResponse book) { this.book = book; }
    
    public boolean isOverdue() { return overdue; }
    public void setOverdue(boolean overdue) { this.overdue = overdue; }
}
