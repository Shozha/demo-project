package com.technokratos.agona.exception.auth;

import com.technokratos.agona.exception.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {

    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}
