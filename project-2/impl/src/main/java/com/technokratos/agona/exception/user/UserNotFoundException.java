package com.technokratos.agona.exception.user;

import com.technokratos.agona.exception.NotFoundException;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(UUID id) {
        super(String.format("User with id '%s' not found", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("User with name '%s' not found", username));
    }
}
