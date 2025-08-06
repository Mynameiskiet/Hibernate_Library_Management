package com.tuankiet.service.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.request.BookCreateRequest;
import com.tuankiet.dto.request.BookUpdateRequest;
import com.tuankiet.dto.response.AuthorResponse;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.entities.Author;
import com.tuankiet.entities.Book;
import com.tuankiet.exception.BusinessRuleViolationException;
import com.tuankiet.exception.NotFoundException;
import com.tuankiet.repository.AuthorRepository;
import com.tuankiet.repository.BookRepository;
import com.tuankiet.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Override
    public BookResponse create(BookCreateRequest request) {
        String correlationId = MDC.get("correlationId");
        logger.info("Creating book with title: {} [correlationId: {}]", request.getTitle(), correlationId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Book book = new Book(request.getTitle(), request.getCategory());
            
            // Add authors if provided
            if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
                Set<Author> authors = request.getAuthorIds().stream()
                    .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new NotFoundException("Author not found with id: " + authorId)))
                    .collect(Collectors.toSet());
                
                authors.forEach(book::addAuthor);
            }
            
            Book savedBook = bookRepository.save(book);
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Book created successfully with id: {} [correlationId: {}, duration: {}ms]", 
                       savedBook.getId(), correlationId, duration);
            
            return convertToResponse(savedBook);
            
        } catch (Exception e) {
            logger.error("Error creating book [correlationId: {}]", correlationId, e);
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        String correlationId = MDC.get("correlationId");
        logger.debug("Fetching book with id: {} [correlationId: {}]", id, correlationId);
        
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        
        return convertToResponse(book);
    }
    
    @Override
    public BookResponse update(Long id, BookUpdateRequest request) {
        String correlationId = MDC.get("correlationId");
        logger.info("Updating book with id: {} [correlationId: {}]", id, correlationId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
            
            // Check version for optimistic locking
            if (!book.getVersion().equals(request.getVersion())) {
                throw new BusinessRuleViolationException("Book has been modified by another user. Please refresh and try again.");
            }
            
            book.setTitle(request.getTitle());
            book.setCategory(request.getCategory());
            
            if (request.getAvailable() != null) {
                book.setAvailable(request.getAvailable());
            }
            
            // Update authors if provided
            if (request.getAuthorIds() != null) {
                // Clear existing authors
                book.getAuthors().clear();
                
                // Add new authors
                Set<Author> authors = request.getAuthorIds().stream()
                    .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new NotFoundException("Author not found with id: " + authorId)))
                    .collect(Collectors.toSet());
                
                authors.forEach(book::addAuthor);
            }
            
            Book updatedBook = bookRepository.save(book);
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Book updated successfully with id: {} [correlationId: {}, duration: {}ms]", 
                       id, correlationId, duration);
            
            return convertToResponse(updatedBook);
            
        } catch (Exception e) {
            logger.error("Error updating book with id: {} [correlationId: {}]", id, correlationId, e);
            throw e;
        }
    }
    
    @Override
    public void delete(Long id) {
        String correlationId = MDC.get("correlationId");
        logger.info("Deleting book with id: {} [correlationId: {}]", id, correlationId);
        
        try {
            Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
            
            // Business Rule R1: Cannot delete book if there are active borrowings
            if (bookRepository.hasActiveBorrowings(id)) {
                logger.warn("Cannot delete book with id: {} - has active borrowings [correlationId: {}]", id, correlationId);
                throw new BusinessRuleViolationException("Cannot delete book with active borrowings");
            }
            
            bookRepository.delete(book);
            
            logger.info("Book deleted successfully with id: {} [correlationId: {}]", id, correlationId);
            
        } catch (Exception e) {
            logger.error("Error deleting book with id: {} [correlationId: {}]", id, correlationId, e);
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> search(String title, String category, Boolean available, 
                                   Long authorId, LocalDateTime createdFrom, LocalDateTime createdTo,
                                   PageRequest pageRequest) {
        String correlationId = MDC.get("correlationId");
        logger.debug("Searching books [correlationId: {}]", correlationId);
        
        Page<Book> bookPage = bookRepository.search(title, category, available, authorId, 
                                                   createdFrom, createdTo, pageRequest);
        
        List<BookResponse> content = bookPage.getContent().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
        
        return new Page<>(content, bookPage.getTotalElements(), 
                         bookPage.getPage(), bookPage.getSize());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> listByAuthor(Long authorId, int limit) {
        String correlationId = MDC.get("correlationId");
        logger.debug("Listing books by author: {} [correlationId: {}]", authorId, correlationId);
        
        List<Book> books = bookRepository.findByAuthor(authorId, limit);
        return books.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> topBorrowed(int limit) {
        String correlationId = MDC.get("correlationId");
        logger.debug("Fetching top {} borrowed books [correlationId: {}]", limit, correlationId);
        
        List<Book> books = bookRepository.findTopBorrowed(limit);
        return books.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    public void changeAvailability(Long id, boolean available) {
        String correlationId = MDC.get("correlationId");
        logger.info("Changing availability for book id: {} to {} [correlationId: {}]", id, available, correlationId);
        
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        
        book.setAvailable(available);
        bookRepository.save(book);
        
        logger.info("Book availability changed successfully for id: {} [correlationId: {}]", id, correlationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findOverdueByDays(int days) {
        String correlationId = MDC.get("correlationId");
        logger.debug("Finding overdue books by {} days [correlationId: {}]", days, correlationId);
        
        List<Book> books = bookRepository.findOverdueByDays(days);
        return books.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public BookResponse getCached(Long id) {
        // This method demonstrates second-level cache usage
        return getById(id);
    }
    
    @Override
    public void bulkImport(List<BookCreateRequest> requests, int batchSize) {
        String correlationId = MDC.get("correlationId");
        logger.info("Starting bulk import of {} books with batch size {} [correlationId: {}]", 
                   requests.size(), batchSize, correlationId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            List<Book> books = requests.stream()
                .map(request -> new Book(request.getTitle(), request.getCategory()))
                .collect(Collectors.toList());
            
            bookRepository.bulkSave(books);
            
            long duration = System.currentTimeMillis() - startTime;
            long throughput = requests.size() * 1000 / duration; // records per second
            
            logger.info("Bulk import completed: {} books imported in {}ms, throughput: {} books/sec [correlationId: {}]", 
                       requests.size(), duration, throughput, correlationId);
            
        } catch (Exception e) {
            logger.error("Error during bulk import [correlationId: {}]", correlationId, e);
            throw e;
        }
    }
    
    @Override
    public void bulkUpdateAvailability(List<Long> bookIds, boolean available, int batchSize) {
        String correlationId = MDC.get("correlationId");
        logger.info("Starting bulk update availability for {} books [correlationId: {}]", 
                   bookIds.size(), correlationId);
        
        long startTime = System.currentTimeMillis();
        
        try {
            bookRepository.bulkUpdateAvailability(bookIds, available);
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Bulk update availability completed: {} books updated in {}ms [correlationId: {}]", 
                       bookIds.size(), duration, correlationId);
            
        } catch (Exception e) {
            logger.error("Error during bulk update availability [correlationId: {}]", correlationId, e);
            throw e;
        }
    }
    
    private BookResponse convertToResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setCategory(book.getCategory());
        response.setAvailable(book.getAvailable());
        response.setCreatedDate(book.getCreatedDate());
        response.setVersion(book.getVersion());
        
        Set<AuthorResponse> authorResponses = book.getAuthors().stream()
            .map(author -> new AuthorResponse(author.getId(), author.getName(), author.getBirthYear()))
            .collect(Collectors.toSet());
        response.setAuthors(authorResponses);
        
        return response;
    }
}
