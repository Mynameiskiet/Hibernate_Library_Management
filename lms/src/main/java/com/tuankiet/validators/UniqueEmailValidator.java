package com.tuankiet.validators;

import com.tuankiet.dto.request.UpdateMemberRequest;
import com.tuankiet.repositories.MemberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator for the {@link UniqueEmail} annotation.
 * Checks if an email address is unique in the database.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        // Spring will automatically inject dependencies
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true; // @NotBlank or @Email should handle null/empty
        }

        // For create operations, simply check if email exists
        return !memberRepository.existsByEmail(email);
    }
}
