package com.tuankiet.repositories;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.search.BookSearchCriteria;
import com.tuankiet.entities.Book;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Book entity operations.
 * Extends BaseRepository to inherit common CRUD operations.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
public interface BookRepository extends BaseRepository<Book> {
    
    /**
     * Find a book by its ISBN.
     * 
     * @param isbn the ISBN to search for
     * @return Optional containing the book if found, empty otherwise
     */
    Optional<Book> findByIsbn(String isbn);
    
    /**
     * Check if a book exists with the given ISBN.
     * 
     * @param isbn the ISBN to check
     * @return true if a book exists with the given ISBN, false otherwise
     */
    boolean existsByIsbn(String isbn);
    
    /**
     * Search books based on criteria with pagination and sorting.
     * 
     * @param criteria the search criteria
     * @param pageRequest the pagination and sorting information
     * @return a page of books matching the criteria
     */
    Page<Book> searchBooks(BookSearchCriteria criteria, PageRequest pageRequest);
    
    /**
     * Count the number of borrowed copies for a specific book.
     * 
     * @param bookId the ID of the book
     * @return the number of currently borrowed copies
     */
    long countBorrowedCopies(UUID bookId);
}
