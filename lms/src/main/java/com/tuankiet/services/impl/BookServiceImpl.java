package com.tuankiet.services.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.request.CreateBookRequest;
import com.tuankiet.dto.request.UpdateBookRequest;
import com.tuankiet.dto.response.AuthorResponse;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.dto.search.BookSearchCriteria;
import com.tuankiet.entities.Author;
import com.tuankiet.entities.Book;
import com.tuankiet.exceptions.BusinessRuleViolationException;
import com.tuankiet.exceptions.DuplicateEntityException;
import com.tuankiet.exceptions.EntityNotFoundException;
import com.tuankiet.repositories.AuthorRepository;
import com.tuankiet.repositories.BookRepository;
import com.tuankiet.services.BookService;
import com.tuankiet.services.validation.ValidationService;
import com.tuankiet.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
* Implementation of the BookService interface.
* Handles business logic for Book entities.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Service
public class BookServiceImpl extends BaseServiceImpl<Book, BookResponse, CreateBookRequest, UpdateBookRequest, BookSearchCriteria> implements BookService {

  private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;

  @Autowired
  public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, ValidationService validationService, MapperUtil mapperUtil) {
      super(bookRepository, validationService, mapperUtil);
      this.bookRepository = bookRepository;
      this.authorRepository = authorRepository;
  }

  @Override
  @Transactional
  public BookResponse create(CreateBookRequest createRequest) {
      logger.info("Attempting to create new book with ISBN: {}", createRequest.getIsbn());
      validationService.validate(createRequest);

      if (bookRepository.existsByIsbn(createRequest.getIsbn())) {
          throw new DuplicateEntityException("Book", "ISBN", createRequest.getIsbn());
      }

      Author author = authorRepository.findById(createRequest.getAuthorId())
              .orElseThrow(() -> new EntityNotFoundException("Author", createRequest.getAuthorId()));

      Book book = mapperUtil.map(createRequest, Book.class);
      book.setAuthor(author);
      book.setAvailableCopies(book.getTotalCopies()); // Initialize available copies

      Book savedBook = bookRepository.save(book);
      logger.info("Successfully created book with ID: {}", savedBook.getId());
      return mapBookToResponse(savedBook);
  }

  @Override
  @Transactional(readOnly = true)
  public BookResponse getById(UUID id) {
      logger.debug("Retrieving book with ID: {}", id);
      Book book = bookRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Book", id));
      return mapBookToResponse(book);
  }

  @Override
  @Transactional(readOnly = true)
  public BookResponse getByIsbn(String isbn) {
      logger.debug("Retrieving book with ISBN: {}", isbn);
      Book book = bookRepository.findByIsbn(isbn)
              .orElseThrow(() -> new EntityNotFoundException("Book with ISBN " + isbn + " not found."));
      return mapBookToResponse(book);
  }

  @Override
  @Transactional
  public BookResponse update(UpdateBookRequest updateRequest) {
      logger.info("Attempting to update book with ID: {}", updateRequest.getId());
      validationService.validate(updateRequest);

      Book existingBook = bookRepository.findById(updateRequest.getId())
              .orElseThrow(() -> new EntityNotFoundException("Book", updateRequest.getId()));

      // Check for ISBN uniqueness if it's changed
      if (!existingBook.getIsbn().equals(updateRequest.getIsbn()) && bookRepository.existsByIsbn(updateRequest.getIsbn())) {
          throw new DuplicateEntityException("Book", "ISBN", updateRequest.getIsbn());
      }

      Author author = authorRepository.findById(updateRequest.getAuthorId())
              .orElseThrow(() -> new EntityNotFoundException("Author", updateRequest.getAuthorId()));

      // Validate totalCopies and availableCopies consistency
      if (updateRequest.getTotalCopies() < existingBook.getTotalCopies() - existingBook.getAvailableCopies()) {
          throw new BusinessRuleViolationException("New total copies cannot be less than currently borrowed copies.");
      }
      if (updateRequest.getAvailableCopies() > updateRequest.getTotalCopies()) {
          throw new BusinessRuleViolationException("Available copies cannot exceed total copies.");
      }
      if (updateRequest.getAvailableCopies() < 0) {
          throw new BusinessRuleViolationException("Available copies cannot be negative.");
      }

      mapperUtil.map(updateRequest, existingBook);
      existingBook.setAuthor(author);

      Book updatedBook = bookRepository.save(existingBook);
      logger.info("Successfully updated book with ID: {}", updatedBook.getId());
      return mapBookToResponse(updatedBook);
  }

  @Override
  @Transactional
  public boolean delete(UUID id) {
      logger.info("Attempting to delete book with ID: {}", id);
      // Before deleting, check if there are any active borrowings for this book
      long borrowedCopies = bookRepository.countBorrowedCopies(id);
      if (borrowedCopies > 0) {
          throw new BusinessRuleViolationException("Cannot delete book with ID " + id + " as there are " + borrowedCopies + " active borrowings.");
      }
      boolean deleted = bookRepository.deleteById(id);
      if (deleted) {
          logger.info("Successfully deleted book with ID: {}", id);
      } else {
          logger.warn("Failed to delete book: ID {} not found.", id);
      }
      return deleted;
  }

  @Override
  @Transactional
  public void decrementAvailableCopies(UUID bookId, int quantity) {
      logger.debug("Decrementing {} available copies for book ID: {}", quantity, bookId);
      Book book = bookRepository.findById(bookId)
              .orElseThrow(() -> new EntityNotFoundException("Book", bookId));

      if (book.getAvailableCopies() < quantity) {
          throw new BusinessRuleViolationException("Not enough available copies for book '" + book.getTitle() + "'. Available: " + book.getAvailableCopies() + ", Requested: " + quantity);
      }
      book.setAvailableCopies(book.getAvailableCopies() - quantity);
      bookRepository.save(book);
      logger.info("Decremented available copies for book ID {}. New available: {}", bookId, book.getAvailableCopies());
  }

  @Override
  @Transactional
  public void incrementAvailableCopies(UUID bookId, int quantity) {
      logger.debug("Incrementing {} available copies for book ID: {}", quantity, bookId);
      Book book = bookRepository.findById(bookId)
              .orElseThrow(() -> new EntityNotFoundException("Book", bookId));

      if (book.getAvailableCopies() + quantity > book.getTotalCopies()) {
          throw new BusinessRuleViolationException("Cannot increment available copies beyond total copies for book '" + book.getTitle() + "'. Total: " + book.getTotalCopies() + ", Current Available: " + book.getAvailableCopies() + ", Increment: " + quantity);
      }
      book.setAvailableCopies(book.getAvailableCopies() + quantity);
      bookRepository.save(book);
      logger.info("Incremented available copies for book ID {}. New available: {}", bookId, book.getAvailableCopies());
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookResponse> getAll() {
      logger.debug("Retrieving all books.");
      return bookRepository.findAll().stream()
              .map(this::mapBookToResponse)
              .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<BookResponse> search(BookSearchCriteria criteria, PageRequest pageRequest) {
      logger.debug("Searching books with criteria: {} and page request: {}", criteria, pageRequest);
      validationService.validate(pageRequest);
      Page<Book> bookPage = bookRepository.searchBooks(criteria, pageRequest);
      List<BookResponse> content = bookPage.getContent().stream()
              .map(this::mapBookToResponse)
              .collect(Collectors.toList());
      return new Page<>(content, bookPage.getTotalElements(), bookPage.getCurrentPage(), bookPage.getPageSize());
  }

  private BookResponse mapBookToResponse(Book book) {
      BookResponse response = mapperUtil.map(book, BookResponse.class);
      if (book.getAuthor() != null) {
          response.setAuthor(mapperUtil.map(book.getAuthor(), AuthorResponse.class));
      }
      return response;
  }
}
