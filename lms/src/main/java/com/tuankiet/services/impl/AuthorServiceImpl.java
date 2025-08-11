package com.tuankiet.services.impl;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.request.CreateAuthorRequest;
import com.tuankiet.dto.request.UpdateAuthorRequest;
import com.tuankiet.dto.response.AuthorResponse;
import com.tuankiet.dto.search.AuthorSearchCriteria;
import com.tuankiet.entities.Author;
import com.tuankiet.exceptions.BusinessRuleViolationException;
import com.tuankiet.exceptions.DuplicateEntityException;
import com.tuankiet.exceptions.EntityNotFoundException;
import com.tuankiet.repositories.AuthorRepository;
import com.tuankiet.repositories.BookRepository;
import com.tuankiet.services.AuthorService;
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
 * Implementation of the AuthorService interface.
 * Handles business logic for Author entities.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class AuthorServiceImpl extends BaseServiceImpl<Author, AuthorResponse, CreateAuthorRequest, UpdateAuthorRequest, AuthorSearchCriteria> implements AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository, ValidationService validationService, MapperUtil mapperUtil) {
        super(authorRepository, validationService, mapperUtil);
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public AuthorResponse create(CreateAuthorRequest createRequest) {
        logger.info("Attempting to create new author: {} {}", createRequest.getFirstName(), createRequest.getLastName());
        validationService.validate(createRequest);

        if (authorRepository.existsByFirstNameAndLastName(createRequest.getFirstName(), createRequest.getLastName())) {
            throw new DuplicateEntityException("Author", "name", createRequest.getFirstName() + " " + createRequest.getLastName());
        }

        Author author = mapperUtil.map(createRequest, Author.class);
        Author savedAuthor = authorRepository.save(author);
        logger.info("Successfully created author with ID: {}", savedAuthor.getId());
        return mapperUtil.map(savedAuthor, AuthorResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getById(UUID id) {
        logger.debug("Retrieving author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author", id));
        return mapperUtil.map(author, AuthorResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getByFirstNameAndLastName(String firstName, String lastName) {
        logger.debug("Retrieving author with name: {} {}", firstName, lastName);
        Author author = authorRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException("Author with name " + firstName + " " + lastName + " not found."));
        return mapperUtil.map(author, AuthorResponse.class);
    }

    @Override
    @Transactional
    public AuthorResponse update(UpdateAuthorRequest updateRequest) {
        logger.info("Attempting to update author with ID: {}", updateRequest.getId());
        validationService.validate(updateRequest);

        Author existingAuthor = authorRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Author", updateRequest.getId()));

        // Check for name uniqueness if it's changed and not the current author
        if (!existingAuthor.getFirstName().equalsIgnoreCase(updateRequest.getFirstName()) ||
            !existingAuthor.getLastName().equalsIgnoreCase(updateRequest.getLastName())) {
            if (authorRepository.existsByFirstNameAndLastName(updateRequest.getFirstName(), updateRequest.getLastName())) {
                throw new DuplicateEntityException("Author", "name", updateRequest.getFirstName() + " " + updateRequest.getLastName());
            }
        }

        mapperUtil.map(updateRequest, existingAuthor);
        Author updatedAuthor = authorRepository.save(existingAuthor);
        logger.info("Successfully updated author with ID: {}", updatedAuthor.getId());
        return mapperUtil.map(updatedAuthor, AuthorResponse.class);
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {
        logger.info("Attempting to delete author with ID: {}", id);
        
        // Check if there are any books associated with this author
        // This is a simplified check - in a real implementation, you'd have a proper method
        try {
            // Simple check by trying to find books with this author
            // This would need to be implemented properly in BookRepository
            logger.warn("Author deletion check for associated books not fully implemented");
        } catch (Exception e) {
            logger.warn("Could not check for associated books: {}", e.getMessage());
        }

        boolean deleted = authorRepository.deleteById(id);
        if (deleted) {
            logger.info("Successfully deleted author with ID: {}", id);
        } else {
            logger.warn("Failed to delete author: ID {} not found.", id);
        }
        return deleted;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> getAll() {
        logger.debug("Retrieving all authors.");
        return authorRepository.findAll().stream()
                .map(author -> mapperUtil.map(author, AuthorResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorResponse> search(AuthorSearchCriteria criteria, PageRequest pageRequest) {
        logger.debug("Searching authors with criteria: {} and page request: {}", criteria, pageRequest);
        validationService.validate(pageRequest);
        Page<Author> authorPage = authorRepository.searchAuthors(criteria, pageRequest);
        List<AuthorResponse> content = authorPage.getContent().stream()
                .map(author -> mapperUtil.map(author, AuthorResponse.class))
                .collect(Collectors.toList());
        return new Page<>(content, authorPage.getTotalElements(), authorPage.getCurrentPage(), authorPage.getPageSize());
    }
}
