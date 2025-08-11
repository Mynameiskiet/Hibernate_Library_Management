package com.tuankiet.dto.request;

import com.tuankiet.validators.ValidBorrowingDates;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
* DTO for creating a new Borrowing record.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@ValidBorrowingDates
public class CreateBorrowingRequest {

  @NotNull(message = "Book ID cannot be null")
  private UUID bookId;

  @NotNull(message = "Member ID cannot be null")
  private UUID memberId;

  @NotNull(message = "Borrow date cannot be null")
  @FutureOrPresent(message = "Borrow date cannot be in the past")
  private LocalDate borrowDate;

  @NotNull(message = "Due date cannot be null")
  private LocalDate dueDate;

  public CreateBorrowingRequest() {
  }

  public CreateBorrowingRequest(UUID bookId, UUID memberId, LocalDate borrowDate, LocalDate dueDate) {
      this.bookId = bookId;
      this.memberId = memberId;
      this.borrowDate = borrowDate;
      this.dueDate = dueDate;
  }

  public UUID getBookId() {
      return bookId;
  }

  public void setBookId(UUID bookId) {
      this.bookId = bookId;
  }

  public UUID getMemberId() {
      return memberId;
  }

  public void setMemberId(UUID memberId) {
      this.memberId = memberId;
  }

  public LocalDate getBorrowDate() {
      return borrowDate;
  }

  public void setBorrowDate(LocalDate borrowDate) {
      this.borrowDate = borrowDate;
  }

  public LocalDate getDueDate() {
      return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
      this.dueDate = dueDate;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CreateBorrowingRequest that = (CreateBorrowingRequest) o;
      return Objects.equals(bookId, that.bookId) &&
             Objects.equals(memberId, that.memberId) &&
             Objects.equals(borrowDate, that.borrowDate) &&
             Objects.equals(dueDate, that.dueDate);
  }

  @Override
  public int hashCode() {
      return Objects.hash(bookId, memberId, borrowDate, dueDate);
  }

  @Override
  public String toString() {
      return "CreateBorrowingRequest{" +
             "bookId=" + bookId +
             ", memberId=" + memberId +
             ", borrowDate=" + borrowDate +
             ", dueDate=" + dueDate +
             '}';
  }
}
