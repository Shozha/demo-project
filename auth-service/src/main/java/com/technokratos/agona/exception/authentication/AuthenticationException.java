package com.technokratos.agona.exception.authentication;

import com.technokratos.agona.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApiException {

    public AuthenticationException(String message, String errorCode) {
        super(message, HttpStatus.UNAUTHORIZED, errorCode);
    }
}
