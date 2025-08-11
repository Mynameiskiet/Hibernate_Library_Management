package com.tuankiet.dto.response;

import java.util.Objects;
import java.util.UUID;

/**
* DTO for book borrowing statistics.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class BookBorrowingStats {
  private UUID bookId;
  private String bookTitle;
  private String bookIsbn;
  private long totalBorrows;
  private long currentBorrows;
  private long overdueBorrows;

  public BookBorrowingStats() {
  }

  public BookBorrowingStats(UUID bookId, String bookTitle, String bookIsbn, long totalBorrows, long currentBorrows, long overdueBorrows) {
      this.bookId = bookId;
      this.bookTitle = bookTitle;
      this.bookIsbn = bookIsbn;
      this.totalBorrows = totalBorrows;
      this.currentBorrows = currentBorrows;
      this.overdueBorrows = overdueBorrows;
  }

  public UUID getBookId() {
      return bookId;
  }

  public void setBookId(UUID bookId) {
      this.bookId = bookId;
  }

  public String getBookTitle() {
      return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
      this.bookTitle = bookTitle;
  }

  public String getBookIsbn() {
      return bookIsbn;
  }

  public void setBookIsbn(String bookIsbn) {
      this.bookIsbn = bookIsbn;
  }

  public long getTotalBorrows() {
      return totalBorrows;
  }

  public void setTotalBorrows(long totalBorrows) {
      this.totalBorrows = totalBorrows;
  }

  public long getCurrentBorrows() {
      return currentBorrows;
  }

  public void setCurrentBorrows(long currentBorrows) {
      this.currentBorrows = currentBorrows;
  }

  public long getOverdueBorrows() {
      return overdueBorrows;
  }

  public void setOverdueBorrows(long overdueBorrows) {
      this.overdueBorrows = overdueBorrows;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      BookBorrowingStats that = (BookBorrowingStats) o;
      return totalBorrows == that.totalBorrows &&
             currentBorrows == that.currentBorrows &&
             overdueBorrows == that.overdueBorrows &&
             Objects.equals(bookId, that.bookId) &&
             Objects.equals(bookTitle, that.bookTitle) &&
             Objects.equals(bookIsbn, that.bookIsbn);
  }

  @Override
  public int hashCode() {
      return Objects.hash(bookId, bookTitle, bookIsbn, totalBorrows, currentBorrows, overdueBorrows);
  }

  @Override
  public String toString() {
      return "BookBorrowingStats{" +
             "bookId=" + bookId +
             ", bookTitle='" + bookTitle + '\'' +
             ", bookIsbn='" + bookIsbn + '\'' +
             ", totalBorrows=" + totalBorrows +
             ", currentBorrows=" + currentBorrows +
             ", overdueBorrows=" + overdueBorrows +
             '}';
  }
}
