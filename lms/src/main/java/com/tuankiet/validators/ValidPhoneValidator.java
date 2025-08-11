package com.tuankiet.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link ValidPhone} annotation.
 * Validates phone numbers against a common regex pattern.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
public class ValidPhoneValidator implements ConstraintValidator<ValidPhone, String> {

    // Regex for common phone number formats (e.g., 123-456-7890, (123) 456-7890, 123.456.7890, 123 456 7890)
    // Allows optional country code, spaces, hyphens, dots, and parentheses.
    private static final String PHONE_REGEX = "^(\\+\\d{1,3}[- ]?)?\\d{3}[- .]?\\d{3}[- .]?\\d{4}$";

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return true; // Null or empty phone numbers are considered valid (optional field)
        }
        return phoneNumber.matches(PHONE_REGEX);
    }
}
