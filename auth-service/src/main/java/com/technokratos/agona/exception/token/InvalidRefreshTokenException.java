package com.technokratos.agona.exception.token;

public class InvalidRefreshTokenException extends InvalidGrantException {

    public InvalidRefreshTokenException() {
        super("Token is invalid or context mismatch");
    }
}
