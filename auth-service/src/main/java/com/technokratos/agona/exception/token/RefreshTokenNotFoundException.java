package com.technokratos.agona.exception.token;

public class RefreshTokenNotFoundException extends InvalidGrantException {

    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
