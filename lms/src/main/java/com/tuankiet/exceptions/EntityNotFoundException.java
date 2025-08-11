package com.tuankiet.exceptions;

import java.util.UUID;

/**
* Exception thrown when an entity is not found in the database.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String entityName, UUID id) {
      super(String.format("%s with ID %s not found.", entityName, id));
  }

  public EntityNotFoundException(String message) {
      super(message);
  }
}
