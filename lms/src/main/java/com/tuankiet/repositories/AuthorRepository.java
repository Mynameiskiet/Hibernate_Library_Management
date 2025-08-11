package com.tuankiet.repositories;

import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.search.AuthorSearchCriteria;
import com.tuankiet.entities.Author;

import java.util.Optional;

/**
* Repository interface for Author entities.
* Extends BaseRepository for common CRUD operations.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface AuthorRepository extends BaseRepository<Author> {

  /**
   * Finds an author by their first and last name.
   * @param firstName The first name of the author.
   * @param lastName The last name of the author.
   * @return An Optional containing the author if found, or empty otherwise.
   */
  Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);

  /**
   * Checks if an author with the given first and last name exists.
   * @param firstName The first name to check.
   * @param lastName The last name to check.
   * @return True if an author with the names exists, false otherwise.
   */
  boolean existsByFirstNameAndLastName(String firstName, String lastName);

  /**
   * Retrieves a paginated list of authors based on search criteria.
   * @param criteria The search criteria for authors.
   * @param pageRequest The pagination and sorting information.
   * @return A Page object containing the requested authors.
   */
  Page<Author> searchAuthors(AuthorSearchCriteria criteria, PageRequest pageRequest);
}
