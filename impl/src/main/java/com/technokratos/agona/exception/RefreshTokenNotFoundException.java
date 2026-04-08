package com.technokratos.agona.exception;

import java.util.UUID;

public class RefreshTokenNotFoundException extends NotFoundException {
    public RefreshTokenNotFoundException(UUID id) {
        super(String.format("No RefreshToken found with id: %s", id));
    }
}
