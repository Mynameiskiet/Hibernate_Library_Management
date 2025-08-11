package com.tuankiet.repositories;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.search.BorrowingSearchCriteria;
import com.tuankiet.entities.Borrowing;
import com.tuankiet.entities.Book;
import com.tuankiet.entities.Member;
import com.tuankiet.enums.BorrowingStatus;

import java.util.List;
import java.util.UUID;

/**
* Repository interface for Borrowing entities.
* Extends BaseRepository for common CRUD operations.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface BorrowingRepository extends BaseRepository<Borrowing> {

  /**
   * Finds all borrowing records for a specific book.
   * @param bookId The ID of the book.
   * @return A list of borrowing records for the book.
   */
  List<Borrowing> findByBookId(UUID bookId);

  /**
   * Finds all borrowing records for a specific member.
   * @param memberId The ID of the member.
   * @return A list of borrowing records for the member.
   */
  List<Borrowing> findByMemberId(UUID memberId);

  /**
   * Finds all borrowing records with a specific status.
   * @param status The status of the borrowing record.
   * @return A list of borrowing records with the given status.
   */
  List<Borrowing> findByStatus(BorrowingStatus status);

  /**
   * Finds active borrowing records (not returned) for a specific book and member.
   * @param book The book involved in the borrowing.
   * @param member The member involved in the borrowing.
   * @return A list of active borrowing records.
   */
  List<Borrowing> findActiveBorrowings(Book book, Member member);

  /**
   * Counts the number of borrowed copies for a specific book.
   * @param bookId The ID of the book.
   * @return The count of borrowed copies.
   */
  long countBorrowedCopies(UUID bookId);

  /**
   * Retrieves a paginated list of borrowing records based on search criteria.
   * @param criteria The search criteria for borrowing records.
   * @param pageRequest The pagination and sorting information.
   * @return A Page object containing the requested borrowing records.
   */
  Page<Borrowing> searchBorrowings(BorrowingSearchCriteria criteria, PageRequest pageRequest);
}
