package com.tuankiet.dto.response;

import com.tuankiet.enums.BorrowingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
* DTO for Borrowing response.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class BorrowingResponse {
  private UUID id;
  private BookResponse book;
  private MemberResponse member;
  private LocalDate borrowDate;
  private LocalDate returnDate;
  private LocalDate dueDate;
  private BorrowingStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public BorrowingResponse() {
  }

  public BorrowingResponse(UUID id, BookResponse book, MemberResponse member, LocalDate borrowDate, LocalDate returnDate, LocalDate dueDate, BorrowingStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
      this.id = id;
      this.book = book;
      this.member = member;
      this.borrowDate = borrowDate;
      this.returnDate = returnDate;
      this.dueDate = dueDate;
      this.status = status;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
  }

  public UUID getId() {
      return id;
  }

  public void setId(UUID id) {
      this.id = id;
  }

  public BookResponse getBook() {
      return book;
  }

  public void setBook(BookResponse book) {
      this.book = book;
  }

  public MemberResponse getMember() {
      return member;
  }

  public void setMember(MemberResponse member) {
      this.member = member;
  }

  public LocalDate getBorrowDate() {
      return borrowDate;
  }

  public void setBorrowDate(LocalDate borrowDate) {
      this.borrowDate = borrowDate;
  }

  public LocalDate getReturnDate() {
      return returnDate;
  }

  public void setReturnDate(LocalDate returnDate) {
      this.returnDate = returnDate;
  }

  public LocalDate getDueDate() {
      return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
      this.dueDate = dueDate;
  }

  public BorrowingStatus getStatus() {
      return status;
  }

  public void setStatus(BorrowingStatus status) {
      this.status = status;
  }

  public LocalDateTime getCreatedAt() {
      return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
      return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      BorrowingResponse that = (BorrowingResponse) o;
      return Objects.equals(id, that.id) &&
             Objects.equals(book, that.book) &&
             Objects.equals(member, that.member) &&
             Objects.equals(borrowDate, that.borrowDate) &&
             Objects.equals(returnDate, that.returnDate) &&
             Objects.equals(dueDate, that.dueDate) &&
             status == that.status &&
             Objects.equals(createdAt, that.createdAt) &&
             Objects.equals(updatedAt, that.updatedAt);
  }

  @Override
  public int hashCode() {
      return Objects.hash(id, book, member, borrowDate, returnDate, dueDate, status, createdAt, updatedAt);
  }

  @Override
  public String toString() {
      return "BorrowingResponse{" +
             "id=" + id +
             ", book=" + (book != null ? book.getTitle() : "N/A") +
             ", member=" + (member != null ? member.getFirstName() + " " + member.getLastName() : "N/A") +
             ", borrowDate=" + borrowDate +
             ", returnDate=" + returnDate +
             ", dueDate=" + dueDate +
             ", status=" + status +
             ", createdAt=" + createdAt +
             ", updatedAt=" + updatedAt +
             '}';
  }
}
