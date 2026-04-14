package com.technokratos.agona.exception.registration;

import com.technokratos.agona.exception.ApiException;
import org.springframework.http.HttpStatus;

public class RegistrationConflictException extends ApiException {

    public RegistrationConflictException(String message, String errorCode) {
        super(message, HttpStatus.CONFLICT, errorCode);
    }
}
