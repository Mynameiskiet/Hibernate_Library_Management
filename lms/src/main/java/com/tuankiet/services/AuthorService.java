package com.tuankiet.services;

import com.tuankiet.dto.request.CreateAuthorRequest;
import com.tuankiet.dto.request.UpdateAuthorRequest;
import com.tuankiet.dto.response.AuthorResponse;
import com.tuankiet.dto.search.AuthorSearchCriteria;
import com.tuankiet.entities.Author;

/**
* Service interface for managing Author entities.
* Extends BaseService for common operations.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public interface AuthorService extends BaseService<Author, AuthorResponse, CreateAuthorRequest, UpdateAuthorRequest, AuthorSearchCriteria> {

  /**
   * Finds an author by their first and last name.
   * @param firstName The first name of the author.
   * @param lastName The last name of the author.
   * @return The response DTO of the found author.
   * @throws com.tuankiet.exceptions.EntityNotFoundException if the author is not found.
   */
  AuthorResponse getByFirstNameAndLastName(String firstName, String lastName);
}
