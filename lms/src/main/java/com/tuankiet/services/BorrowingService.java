package com.tuankiet.services;

import com.tuankiet.dto.request.CreateBorrowingRequest;
import com.tuankiet.dto.request.UpdateBorrowingRequest;
import com.tuankiet.dto.response.BorrowingResponse;
import com.tuankiet.dto.search.BorrowingSearchCriteria;
import com.tuankiet.entities.Borrowing;

import java.util.UUID;

/**
* Service interface for managing Borrowing entities.
* Extends BaseService for common operations.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface BorrowingService extends BaseService<Borrowing, BorrowingResponse, CreateBorrowingRequest, UpdateBorrowingRequest, BorrowingSearchCriteria> {

  /**
   * Records a book return.
   * @param borrowingId The ID of the borrowing record to mark as returned.
   * @return The updated borrowing response DTO.
   * @throws com.tuankiet.exceptions.EntityNotFoundException if the borrowing record is not found.
   * @throws com.tuankiet.exceptions.BusinessRuleViolationException if the book is already returned or lost.
   */
  BorrowingResponse returnBook(UUID borrowingId);

  /**
   * Marks a borrowed book as lost.
   * @param borrowingId The ID of the borrowing record to mark as lost.
   * @return The updated borrowing response DTO.
   * @throws com.tuankiet.exceptions.EntityNotFoundException if the borrowing record is not found.
   * @throws com.tuankiet.exceptions.BusinessRuleViolationException if the book is already returned or lost.
   */
  BorrowingResponse markAsLost(UUID borrowingId);
}
