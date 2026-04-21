package com.technokratos.agona.exception.auth;

import com.technokratos.agona.exception.UnauthorizedException;

public class RefreshTokenRevokedException extends UnauthorizedException {

    public RefreshTokenRevokedException() {
        super("Refresh token has been revoked");
    }
}
