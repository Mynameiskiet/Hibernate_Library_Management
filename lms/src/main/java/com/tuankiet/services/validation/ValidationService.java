package com.tuankiet.services.validation;

import com.tuankiet.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for performing validation on DTOs using Jakarta Bean Validation.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    private final Validator validator;

    @Autowired
    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    /**
     * Validates the given object. If validation fails, a ValidationException is thrown.
     * 
     * @param <T> The type of the object to validate.
     * @param object The object to validate.
     * @throws ValidationException if validation errors are found.
     */
    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.toList());
            logger.warn("Validation failed for object of type {}: {}", object.getClass().getSimpleName(), errors);
            throw new ValidationException("Validation failed for " + object.getClass().getSimpleName(), errors);
        }
        logger.debug("Validation successful for object of type {}", object.getClass().getSimpleName());
    }
}
