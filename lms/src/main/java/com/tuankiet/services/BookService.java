package com.tuankiet.services;

import com.tuankiet.dto.request.CreateBookRequest;
import com.tuankiet.dto.request.UpdateBookRequest;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.dto.search.BookSearchCriteria;
import com.tuankiet.entities.Book;

import java.util.UUID;

/**
* Service interface for managing Book entities.
* Extends BaseService for common operations.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface BookService extends BaseService<Book, BookResponse, CreateBookRequest, UpdateBookRequest, BookSearchCriteria> {

  /**
   * Finds a book by its ISBN.
   * @param isbn The ISBN of the book.
   * @return The response DTO of the found book.
   * @throws com.tuankiet.exceptions.EntityNotFoundException if the book is not found.
   */
  BookResponse getByIsbn(String isbn);

  /**
   * Decrements the available copies of a book.
   * @param bookId The ID of the book.
   * @param quantity The number of copies to decrement.
   * @throws com.tuankiet.exceptions.BusinessRuleViolationException if not enough copies are available.
   */
  void decrementAvailableCopies(UUID bookId, int quantity);

  /**
   * Increments the available copies of a book.
   * @param bookId The ID of the book.
   * @param quantity The number of copies to increment.
   * @throws com.tuankiet.exceptions.BusinessRuleViolationException if incrementing exceeds total copies.
   */
  void incrementAvailableCopies(UUID bookId, int quantity);
}
