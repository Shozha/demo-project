package com.technokratos.agona.exception;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(UUID id) {
        super(String.format("User with id = %s not found", id));
    }
}
