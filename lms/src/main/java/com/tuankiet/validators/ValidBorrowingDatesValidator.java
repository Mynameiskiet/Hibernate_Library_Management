package com.tuankiet.validators;

import java.time.LocalDate;

import com.tuankiet.dto.request.CreateBorrowingRequest;
import com.tuankiet.dto.request.UpdateBorrowingRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link ValidBorrowingDates} annotation.
 * Checks if the due date is on or after the borrow date.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
public class ValidBorrowingDatesValidator implements ConstraintValidator<ValidBorrowingDates, Object> {

    @Override
    public void initialize(ValidBorrowingDates constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        LocalDate borrowDate = null;
        LocalDate dueDate = null;

        if (obj instanceof CreateBorrowingRequest createRequest) {
            borrowDate = createRequest.getBorrowDate();
            dueDate = createRequest.getDueDate();
        } else if (obj instanceof UpdateBorrowingRequest updateRequest) {
            borrowDate = updateRequest.getBorrowDate();
            dueDate = updateRequest.getDueDate();
        } else {
            // Should not happen if annotation is applied correctly
            return false;
        }

        if (borrowDate == null || dueDate == null) {
            // Null checks are handled by @NotNull annotations
            return true;
        }

        boolean isValid = !dueDate.isBefore(borrowDate);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("dueDate")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
