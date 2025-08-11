package com.tuankiet.services;

import com.tuankiet.dto.response.BookBorrowingStats;
import com.tuankiet.dto.response.MemberBorrowingStats;
import com.tuankiet.dto.response.BorrowingResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
* Service interface for generating various reports.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface ReportService {

  /**
   * Retrieves a list of all currently borrowed books.
   * @return A list of BorrowingResponse DTOs for currently borrowed books.
   */
  List<BorrowingResponse> getCurrentlyBorrowedBooks();

  /**
   * Retrieves a list of all overdue borrowing records.
   * @return A list of BorrowingResponse DTOs for overdue books.
   */
  List<BorrowingResponse> getOverdueBooks();

  /**
   * Retrieves borrowing history for a specific member.
   * @param memberId The ID of the member.
   * @return A list of BorrowingResponse DTOs for the member's borrowing history.
   */
  List<BorrowingResponse> getMemberBorrowingHistory(UUID memberId);

  /**
   * Retrieves borrowing history for a specific book.
   * @param bookId The ID of the book.
   * @return A list of BorrowingResponse DTOs for the book's borrowing history.
   */
  List<BorrowingResponse> getBookBorrowingHistory(UUID bookId);

  /**
   * Retrieves statistics for a specific book, including total borrows, current borrows, and overdue borrows.
   * @param bookId The ID of the book.
   * @return BookBorrowingStats DTO.
   */
  BookBorrowingStats getBookBorrowingStatistics(UUID bookId);

  /**
   * Retrieves statistics for a specific member, including total borrows, current borrows, and overdue borrows.
   * @param memberId The ID of the member.
   * @return MemberBorrowingStats DTO.
   */
  MemberBorrowingStats getMemberBorrowingStatistics(UUID memberId);

  /**
   * Retrieves a list of books borrowed within a specific date range.
   * @param startDate The start date of the range.
   * @param endDate The end date of the range.
   * @return A list of BorrowingResponse DTOs for books borrowed within the range.
   */
  List<BorrowingResponse> getBooksBorrowedInDateRange(LocalDate startDate, LocalDate endDate);
}
