package com.technokratos.agona.exception.token;

public class TooManyRefreshTokensException extends TokenRefreshException {

    public TooManyRefreshTokensException(int limit) {
        super("Maximum active refresh tokens reached: %d".formatted(limit), "too_many_tokens");
    }
}
