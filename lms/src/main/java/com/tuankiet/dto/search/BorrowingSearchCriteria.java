package com.tuankiet.dto.search;

import com.tuankiet.enums.BorrowingStatus;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
* DTO for searching Borrowing records.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class BorrowingSearchCriteria {
  private UUID bookId;
  private UUID memberId;
  private LocalDate borrowDateFrom;
  private LocalDate borrowDateTo;
  private LocalDate dueDateFrom;
  private LocalDate dueDateTo;
  private BorrowingStatus status;

  public BorrowingSearchCriteria() {
  }

  public BorrowingSearchCriteria(UUID bookId, UUID memberId, LocalDate borrowDateFrom, LocalDate borrowDateTo, LocalDate dueDateFrom, LocalDate dueDateTo, BorrowingStatus status) {
      this.bookId = bookId;
      this.memberId = memberId;
      this.borrowDateFrom = borrowDateFrom;
      this.borrowDateTo = borrowDateTo;
      this.dueDateFrom = dueDateFrom;
      this.dueDateTo = dueDateTo;
      this.status = status;
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

  public LocalDate getBorrowDateFrom() {
      return borrowDateFrom;
  }

  public void setBorrowDateFrom(LocalDate borrowDateFrom) {
      this.borrowDateFrom = borrowDateFrom;
  }

  public LocalDate getBorrowDateTo() {
      return borrowDateTo;
  }

  public void setBorrowDateTo(LocalDate borrowDateTo) {
      this.borrowDateTo = borrowDateTo;
  }

  public LocalDate getDueDateFrom() {
      return dueDateFrom;
  }

  public void setDueDateFrom(LocalDate dueDateFrom) {
      this.dueDateFrom = dueDateFrom;
  }

  public LocalDate getDueDateTo() {
      return dueDateTo;
  }

  public void setDueDateTo(LocalDate dueDateTo) {
      this.dueDateTo = dueDateTo;
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
      BorrowingSearchCriteria that = (BorrowingSearchCriteria) o;
      return Objects.equals(bookId, that.bookId) &&
             Objects.equals(memberId, that.memberId) &&
             Objects.equals(borrowDateFrom, that.borrowDateFrom) &&
             Objects.equals(borrowDateTo, that.borrowDateTo) &&
             Objects.equals(dueDateFrom, that.dueDateFrom) &&
             Objects.equals(dueDateTo, that.dueDateTo) &&
             status == that.status;
  }

  @Override
  public int hashCode() {
      return Objects.hash(bookId, memberId, borrowDateFrom, borrowDateTo, dueDateFrom, dueDateTo, status);
  }

  @Override
  public String toString() {
      return "BorrowingSearchCriteria{" +
             "bookId=" + bookId +
             ", memberId=" + memberId +
             ", borrowDateFrom=" + borrowDateFrom +
             ", borrowDateTo=" + borrowDateTo +
             ", dueDateFrom=" + dueDateFrom +
             ", dueDateTo=" + dueDateTo +
             ", status=" + status +
             '}';
  }
}
