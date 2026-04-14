package com.technokratos.agona.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Password must be 8-24 characters, contain at least 1 digit and 1 special symbol (!@#$%^&*)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
