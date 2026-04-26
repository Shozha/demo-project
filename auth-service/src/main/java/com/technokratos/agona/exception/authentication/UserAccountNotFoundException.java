package com.technokratos.agona.exception.authentication;

public class UserAccountNotFoundException extends AuthenticationException {

    private static final String ERROR_CODE = "invalid_credentials";

    public UserAccountNotFoundException(String identifierType, String identifierValue) {
        super("User with %s: %s not found".formatted(identifierType, identifierValue), ERROR_CODE);
    }
}
