package com.technokratos.agona.validation;

import com.technokratos.agona.dto.request.SignUpRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, SignUpRequest> {

    @Override
    public boolean isValid(SignUpRequest req, ConstraintValidatorContext ctx) {
        if (req.password() == null || req.confirmPassword() == null) {
            return false;
        }
        return req.password().equals(req.confirmPassword());
    }
}
