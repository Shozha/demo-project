package com.technokratos.agona.exception.auth;

import com.technokratos.agona.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends ServiceException {
    public RoleNotFoundException(String roleName) {
        super(String.format("Role '%s' not found", roleName), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
