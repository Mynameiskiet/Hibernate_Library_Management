package com.tuankiet.repository;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.entities.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    
    Book save(Book book);
    
    Optional<Book> findById(Long id);
    
    List<Book> findAll();
    
    void delete(Book book);
    
    boolean existsById(Long id);
    
    Page<Book> search(String title, String category, Boolean available, 
                     Long authorId, LocalDateTime createdFrom, LocalDateTime createdTo,
                     PageRequest pageRequest);
    
    List<Book> findByAuthor(Long authorId, int limit);
    
    List<Book> findTopBorrowed(int limit);
    
    List<Book> findOverdueByDays(int days);
    
    boolean hasActiveBorrowings(Long bookId);
    
    void bulkUpdateAvailability(List<Long> bookIds, boolean available);
    
    void bulkSave(List<Book> books);
}
