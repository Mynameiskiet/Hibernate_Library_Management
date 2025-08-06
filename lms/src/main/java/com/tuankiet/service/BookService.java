package com.tuankiet.service;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.request.BookCreateRequest;
import com.tuankiet.dto.request.BookUpdateRequest;
import com.tuankiet.dto.response.BookResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface BookService {
    
    BookResponse create(BookCreateRequest request);
    
    BookResponse getById(Long id);
    
    BookResponse update(Long id, BookUpdateRequest request);
    
    void delete(Long id);
    
    Page<BookResponse> search(String title, String category, Boolean available, 
                             Long authorId, LocalDateTime createdFrom, LocalDateTime createdTo,
                             PageRequest pageRequest);
    
    List<BookResponse> listByAuthor(Long authorId, int limit);
    
    List<BookResponse> topBorrowed(int limit);
    
    void changeAvailability(Long id, boolean available);
    
    List<BookResponse> findOverdueByDays(int days);
    
    BookResponse getCached(Long id);
    
    void bulkImport(List<BookCreateRequest> requests, int batchSize);
    
    void bulkUpdateAvailability(List<Long> bookIds, boolean available, int batchSize);
}
