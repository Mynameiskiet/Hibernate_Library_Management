package com.tuankiet.repository;

import com.tuankiet.entities.Author;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    
    Author save(Author author);
    
    Optional<Author> findById(Long id);
    
    List<Author> findAll();
    
    void delete(Author author);
    
    boolean existsById(Long id);
    
    List<Author> findByIds(List<Long> ids);
}
