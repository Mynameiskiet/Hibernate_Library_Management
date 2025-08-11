package com.tuankiet.exceptions;

/**
* Exception thrown when a business rule is violated.
* 
* @author congdinh2008
* @version 1.0.0
* @since 1.0.0
*/
public class BusinessRuleViolationException extends RuntimeException {

  public BusinessRuleViolationException(String message) {
      super(message);
  }

  public BusinessRuleViolationException(String message, Throwable cause) {
      super(message, cause);
  }
}
