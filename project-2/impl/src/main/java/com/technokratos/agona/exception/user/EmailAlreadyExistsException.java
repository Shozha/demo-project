package com.technokratos.agona.exception.user;

import com.technokratos.agona.exception.ConflictException;

public class EmailAlreadyExistsException extends ConflictException {

    public EmailAlreadyExistsException(String email) {
        super(String.format("Email '%s' is already taken", email));
    }
}
