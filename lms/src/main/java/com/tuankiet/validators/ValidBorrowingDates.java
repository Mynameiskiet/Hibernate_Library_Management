package com.tuankiet.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation to ensure borrowing dates are valid.
 * Specifically, ensures that the due date is not before the borrow date.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = ValidBorrowingDatesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBorrowingDates {
    String message() default "Due date must be on or after borrow date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
