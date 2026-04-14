package com.technokratos.agona.exception.token;

public class InvalidGrantException extends TokenRefreshException {

    public InvalidGrantException(String description) {
        super(description, "invalid_grant");
    }
}
