package com.tuankiet.exceptions;

/**
* Exception thrown when an attempt is made to create an entity that already exists
* based on a unique constraint (e.g., email, ISBN).
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class DuplicateEntityException extends RuntimeException {

  public DuplicateEntityException(String entityName, String fieldName, String value) {
      super(String.format("A %s with %s '%s' already exists.", entityName, fieldName, value));
  }

  public DuplicateEntityException(String message) {
      super(message);
  }
}
