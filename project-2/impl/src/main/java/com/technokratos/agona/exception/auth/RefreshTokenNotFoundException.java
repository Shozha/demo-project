package com.technokratos.agona.exception.auth;

import com.technokratos.agona.exception.UnauthorizedException;

public class RefreshTokenNotFoundException extends UnauthorizedException {

    public RefreshTokenNotFoundException() {
        super("Refresh token not found or invalid");
    }
}
