package com.technokratos.agona.exception.auth;

import com.technokratos.agona.exception.UnauthorizedException;

public class RefreshTokenExpiredException extends UnauthorizedException {

    public RefreshTokenExpiredException() {
        super("Refresh token has expired");
    }
}
