package com.technokratos.agona.exception.auth;

import com.technokratos.agona.exception.UnauthorizedException;

public class AccountDisabledException extends UnauthorizedException {

    public AccountDisabledException() {
        super("Account is disabled");
    }
}
