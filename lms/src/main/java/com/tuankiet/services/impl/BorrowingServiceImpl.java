package com.tuankiet.services.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.request.CreateBorrowingRequest;
import com.tuankiet.dto.request.UpdateBorrowingRequest;
import com.tuankiet.dto.response.AuthorResponse;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.dto.response.BorrowingResponse;
import com.tuankiet.dto.response.MemberResponse;
import com.tuankiet.dto.search.BorrowingSearchCriteria;
import com.tuankiet.entities.Book;
import com.tuankiet.entities.Borrowing;
import com.tuankiet.entities.Member;
import com.tuankiet.enums.BorrowingStatus;
import com.tuankiet.exceptions.BusinessRuleViolationException;
import com.tuankiet.exceptions.EntityNotFoundException;
import com.tuankiet.repositories.BookRepository;
import com.tuankiet.repositories.BorrowingRepository;
import com.tuankiet.repositories.MemberRepository;
import com.tuankiet.services.BorrowingService;
import com.tuankiet.services.validation.ValidationService;
import com.tuankiet.utils.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
* Implementation of the BorrowingService interface.
* Handles business logic for Borrowing entities.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
@Service
public class BorrowingServiceImpl extends BaseServiceImpl<Borrowing, BorrowingResponse, CreateBorrowingRequest, UpdateBorrowingRequest, BorrowingSearchCriteria> implements BorrowingService {

  private static final Logger logger = LoggerFactory.getLogger(BorrowingServiceImpl.class);

  private final BorrowingRepository borrowingRepository;
  private final BookRepository bookRepository;
  private final MemberRepository memberRepository;

  @Autowired
  public BorrowingServiceImpl(BorrowingRepository borrowingRepository, BookRepository bookRepository, MemberRepository memberRepository, ValidationService validationService, MapperUtil mapperUtil) {
      super(borrowingRepository, validationService, mapperUtil);
      this.borrowingRepository = borrowingRepository;
      this.bookRepository = bookRepository;
      this.memberRepository = memberRepository;
  }

  @Override
  @Transactional
  public BorrowingResponse create(CreateBorrowingRequest createRequest) {
      logger.info("Attempting to create new borrowing record for book ID: {} and member ID: {}", createRequest.getBookId(), createRequest.getMemberId());
      validationService.validate(createRequest);

      Book book = bookRepository.findById(createRequest.getBookId())
              .orElseThrow(() -> new EntityNotFoundException("Book", createRequest.getBookId()));
      Member member = memberRepository.findById(createRequest.getMemberId())
              .orElseThrow(() -> new EntityNotFoundException("Member", createRequest.getMemberId()));

      if (book.getAvailableCopies() <= 0) {
          throw new BusinessRuleViolationException("Book '" + book.getTitle() + "' has no available copies.");
      }

      // Check if the member already has an active borrowing for this specific book
      List<Borrowing> activeBorrowings = borrowingRepository.findActiveBorrowings(book, member);
      if (!activeBorrowings.isEmpty()) {
          throw new BusinessRuleViolationException("Member '" + member.getFirstName() + " " + member.getLastName() + "' already has an active borrowing for book '" + book.getTitle() + "'.");
      }

      Borrowing borrowing = mapperUtil.map(createRequest, Borrowing.class);
      borrowing.setBook(book);
      borrowing.setMember(member);
      borrowing.setStatus(BorrowingStatus.BORROWED);

      Borrowing savedBorrowing = borrowingRepository.save(borrowing);

      // Decrement available copies of the book
      book.setAvailableCopies(book.getAvailableCopies() - 1);
      bookRepository.save(book);

      logger.info("Successfully created borrowing record with ID: {}", savedBorrowing.getId());
      return mapBorrowingToResponse(savedBorrowing);
  }

  @Override
  @Transactional(readOnly = true)
  public BorrowingResponse getById(UUID id) {
      logger.debug("Retrieving borrowing record with ID: {}", id);
      Borrowing borrowing = borrowingRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Borrowing", id));
      return mapBorrowingToResponse(borrowing);
  }

  @Override
  @Transactional
  public BorrowingResponse update(UpdateBorrowingRequest updateRequest) {
      logger.info("Attempting to update borrowing record with ID: {}", updateRequest.getId());
      validationService.validate(updateRequest);

      Borrowing existingBorrowing = borrowingRepository.findById(updateRequest.getId())
              .orElseThrow(() -> new EntityNotFoundException("Borrowing", updateRequest.getId()));

      Book book = bookRepository.findById(updateRequest.getBookId())
              .orElseThrow(() -> new EntityNotFoundException("Book", updateRequest.getBookId()));
      Member member = memberRepository.findById(updateRequest.getMemberId())
              .orElseThrow(() -> new EntityNotFoundException("Member", updateRequest.getMemberId()));

      // Handle status changes and update book copies accordingly
      if (existingBorrowing.getStatus() == BorrowingStatus.BORROWED && updateRequest.getStatus() == BorrowingStatus.RETURNED) {
          // Book is being returned
          book.setAvailableCopies(book.getAvailableCopies() + 1);
          bookRepository.save(book);
          updateRequest.setReturnDate(LocalDate.now()); // Set return date to now
          logger.info("Book ID {} returned. Available copies incremented.", book.getId());
      } else if (existingBorrowing.getStatus() == BorrowingStatus.BORROWED && updateRequest.getStatus() == BorrowingStatus.LOST) {
          // Book is being marked as lost
          // No increment to available copies as it's lost
          logger.warn("Book ID {} marked as lost. Available copies not incremented.", book.getId());
      } else if ((existingBorrowing.getStatus() == BorrowingStatus.RETURNED || existingBorrowing.getStatus() == BorrowingStatus.LOST) && updateRequest.getStatus() == BorrowingStatus.BORROWED) {
          // Attempting to change status back to BORROWED from RETURNED/LOST
          throw new BusinessRuleViolationException("Cannot change borrowing status back to BORROWED from RETURNED or LOST.");
      }

      mapperUtil.map(updateRequest, existingBorrowing);
      existingBorrowing.setBook(book);
      existingBorrowing.setMember(member);

      Borrowing updatedBorrowing = borrowingRepository.save(existingBorrowing);
      logger.info("Successfully updated borrowing record with ID: {}", updatedBorrowing.getId());
      return mapBorrowingToResponse(updatedBorrowing);
  }

  @Override
  @Transactional
  public boolean delete(UUID id) {
      logger.info("Attempting to delete borrowing record with ID: {}", id);
      Borrowing existingBorrowing = borrowingRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException("Borrowing", id));

      // If the book was borrowed and not returned/lost, increment available copies before deleting
      if (existingBorrowing.getStatus() == BorrowingStatus.BORROWED || existingBorrowing.getStatus() == BorrowingStatus.OVERDUE) {
          Book book = existingBorrowing.getBook();
          book.setAvailableCopies(book.getAvailableCopies() + 1);
          bookRepository.save(book);
          logger.info("Book ID {} available copies incremented due to borrowing record deletion.", book.getId());
      }

      boolean deleted = borrowingRepository.deleteById(id);
      if (deleted) {
          logger.info("Successfully deleted borrowing record with ID: {}", id);
      } else {
          logger.warn("Failed to delete borrowing record: ID {} not found.", id);
      }
      return deleted;
  }

  @Override
  @Transactional
  public BorrowingResponse returnBook(UUID borrowingId) {
      logger.info("Attempting to return book for borrowing ID: {}", borrowingId);
      Borrowing borrowing = borrowingRepository.findById(borrowingId)
              .orElseThrow(() -> new EntityNotFoundException("Borrowing", borrowingId));

      if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
          throw new BusinessRuleViolationException("Book for borrowing ID " + borrowingId + " has already been returned.");
      }
      if (borrowing.getStatus() == BorrowingStatus.LOST) {
          throw new BusinessRuleViolationException("Book for borrowing ID " + borrowingId + " was marked as lost and cannot be returned.");
      }

      borrowing.setReturnDate(LocalDate.now());
      borrowing.setStatus(BorrowingStatus.RETURNED);
      Borrowing updatedBorrowing = borrowingRepository.save(borrowing);

      // Increment available copies of the book
      Book book = updatedBorrowing.getBook();
      book.setAvailableCopies(book.getAvailableCopies() + 1);
      bookRepository.save(book);

      logger.info("Book for borrowing ID {} successfully returned. Book available copies incremented.", borrowingId);
      return mapBorrowingToResponse(updatedBorrowing);
  }

  @Override
  @Transactional
  public BorrowingResponse markAsLost(UUID borrowingId) {
      logger.info("Attempting to mark borrowing ID {} as lost.", borrowingId);
      Borrowing borrowing = borrowingRepository.findById(borrowingId)
              .orElseThrow(() -> new EntityNotFoundException("Borrowing", borrowingId));

      if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
          throw new BusinessRuleViolationException("Book for borrowing ID " + borrowingId + " has already been returned and cannot be marked as lost.");
      }
      if (borrowing.getStatus() == BorrowingStatus.LOST) {
          throw new BusinessRuleViolationException("Book for borrowing ID " + borrowingId + " has already been marked as lost.");
      }

      borrowing.setStatus(BorrowingStatus.LOST);
      Borrowing updatedBorrowing = borrowingRepository.save(borrowing);

      // Do NOT increment available copies as the book is lost
      logger.warn("Book for borrowing ID {} marked as lost. Available copies not incremented.", borrowingId);
      return mapBorrowingToResponse(updatedBorrowing);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BorrowingResponse> getAll() {
      logger.debug("Retrieving all borrowing records.");
      return borrowingRepository.findAll().stream()
              .map(this::mapBorrowingToResponse)
              .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<BorrowingResponse> search(BorrowingSearchCriteria criteria, PageRequest pageRequest) {
      logger.debug("Searching borrowing records with criteria: {} and page request: {}", criteria, pageRequest);
      validationService.validate(pageRequest);
      Page<Borrowing> borrowingPage = borrowingRepository.searchBorrowings(criteria, pageRequest);
      List<BorrowingResponse> content = borrowingPage.getContent().stream()
              .map(this::mapBorrowingToResponse)
              .collect(Collectors.toList());
      return new Page<>(content, borrowingPage.getTotalElements(), borrowingPage.getCurrentPage(), borrowingPage.getPageSize());
  }

  private BorrowingResponse mapBorrowingToResponse(Borrowing borrowing) {
      BorrowingResponse response = mapperUtil.map(borrowing, BorrowingResponse.class);
      if (borrowing.getBook() != null) {
          BookResponse bookResponse = mapperUtil.map(borrowing.getBook(), BookResponse.class);
          if (borrowing.getBook().getAuthor() != null) {
              bookResponse.setAuthor(mapperUtil.map(borrowing.getBook().getAuthor(), AuthorResponse.class));
          }
          response.setBook(bookResponse);
      }
      if (borrowing.getMember() != null) {
          response.setMember(mapperUtil.map(borrowing.getMember(), MemberResponse.class));
      }
      return response;
  }
}
