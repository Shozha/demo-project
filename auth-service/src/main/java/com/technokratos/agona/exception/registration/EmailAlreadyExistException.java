package com.technokratos.agona.exception.registration;

public class EmailAlreadyExistException extends RegistrationConflictException {

    public EmailAlreadyExistException(String email) {
        super("User with email %s already exists".formatted(email), "email_already_exists");
    }
}
