package com.technokratos.agona.exception.authentication;

public class InvalidCredentialsException extends AuthenticationException {

    public InvalidCredentialsException() {
        super("Invalid email or password", "invalid_credentials");
    }
}
