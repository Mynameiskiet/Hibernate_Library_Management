package com.tuankiet.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Exception handler for CLI operations.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class CLIExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CLIExceptionHandler.class);

    /**
     * Handle exception with proper logging and user-friendly message.
     * 
     * @param e the exception to handle
     */
    public void handleException(Exception e) {
        logger.error("CLI Exception occurred", e);
        
        if (e instanceof ValidationException) {
            System.out.println("❌ Validation Error: " + e.getMessage());
        } else if (e instanceof EntityNotFoundException) {
            System.out.println("❌ Not Found: " + e.getMessage());
        } else if (e instanceof DuplicateEntityException) {
            System.out.println("❌ Duplicate Entry: " + e.getMessage());
        } else if (e instanceof BusinessRuleViolationException) {
            System.out.println("❌ Business Rule Violation: " + e.getMessage());
        } else {
            System.out.println("❌ An unexpected error occurred: " + e.getMessage());
            System.out.println("Please check the logs for more details.");
        }
    }

    /**
     * Static method for backward compatibility.
     * 
     * @param e the exception to handle
     */
    public static void handle(Exception e) {
        Logger staticLogger = LoggerFactory.getLogger(CLIExceptionHandler.class);
        staticLogger.error("Static CLI Exception occurred", e);
        
        if (e instanceof ValidationException) {
            System.out.println("❌ Validation Error: " + e.getMessage());
        } else if (e instanceof EntityNotFoundException) {
            System.out.println("❌ Not Found: " + e.getMessage());
        } else if (e instanceof DuplicateEntityException) {
            System.out.println("❌ Duplicate Entry: " + e.getMessage());
        } else if (e instanceof BusinessRuleViolationException) {
            System.out.println("❌ Business Rule Violation: " + e.getMessage());
        } else {
            System.out.println("❌ An unexpected error occurred: " + e.getMessage());
            System.out.println("Please check the logs for more details.");
        }
    }
}
