package com.tuankiet.dto.request;

import com.tuankiet.enums.BorrowingStatus;
import com.tuankiet.validators.ValidBorrowingDates;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
* DTO for updating an existing Borrowing record.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@ValidBorrowingDates
public class UpdateBorrowingRequest {

  @NotNull(message = "Borrowing ID cannot be null")
  private UUID id;

  @NotNull(message = "Book ID cannot be null")
  private UUID bookId;

  @NotNull(message = "Member ID cannot be null")
  private UUID memberId;

  @NotNull(message = "Borrow date cannot be null")
  @FutureOrPresent(message = "Borrow date cannot be in the past")
  private LocalDate borrowDate;

  private LocalDate returnDate;

  @NotNull(message = "Due date cannot be null")
  private LocalDate dueDate;

  @NotNull(message = "Status cannot be null")
  private BorrowingStatus status;

  public UpdateBorrowingRequest() {
  }

  public UpdateBorrowingRequest(UUID id, UUID bookId, UUID memberId, LocalDate borrowDate, LocalDate returnDate, LocalDate dueDate, BorrowingStatus status) {
      this.id = id;
      this.bookId = bookId;
      this.memberId = memberId;
      this.borrowDate = borrowDate;
      this.returnDate = returnDate;
      this.dueDate = dueDate;
      this.status = status;
  }

  public UUID getId() {
      return id;
  }

  public void setId(UUID id) {
      this.id = id;
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

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      UpdateBorrowingRequest that = (UpdateBorrowingRequest) o;
      return Objects.equals(id, that.id) &&
             Objects.equals(bookId, that.bookId) &&
             Objects.equals(memberId, that.memberId) &&
             Objects.equals(borrowDate, that.borrowDate) &&
             Objects.equals(returnDate, that.returnDate) &&
             Objects.equals(dueDate, that.dueDate) &&
             status == that.status;
  }

  @Override
  public int hashCode() {
      return Objects.hash(id, bookId, memberId, borrowDate, returnDate, dueDate, status);
  }

  @Override
  public String toString() {
      return "UpdateBorrowingRequest{" +
             "id=" + id +
             ", bookId=" + bookId +
             ", memberId=" + memberId +
             ", borrowDate=" + borrowDate +
             ", returnDate=" + returnDate +
             ", dueDate=" + dueDate +
             ", status=" + status +
             '}';
  }
}
