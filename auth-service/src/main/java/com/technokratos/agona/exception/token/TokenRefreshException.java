package com.technokratos.agona.exception.token;

import com.technokratos.agona.exception.ApiException;
import org.springframework.http.HttpStatus;

public class TokenRefreshException extends ApiException {

    public TokenRefreshException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
}
