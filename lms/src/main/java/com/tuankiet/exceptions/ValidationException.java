package com.tuankiet.exceptions;

import java.util.List;

/**
* Exception thrown when validation fails for an entity or DTO.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class ValidationException extends RuntimeException {

  private final List<String> errors;

  public ValidationException(String message, List<String> errors) {
      super(message);
      this.errors = errors;
  }

  public ValidationException(List<String> errors) {
      super("Validation failed for the provided data.");
      this.errors = errors;
  }

  public List<String> getErrors() {
      return errors;
  }
}
