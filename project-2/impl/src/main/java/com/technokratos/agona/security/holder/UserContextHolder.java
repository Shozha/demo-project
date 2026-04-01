package com.technokratos.agona.security.holder;

import com.technokratos.agona.security.userdetails.UserDetailsImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContextHolder {

    public UserDetailsImpl getUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl) {
                return (UserDetailsImpl) principal;
            }
        }
        return null;
    }

    public UserDetailsImpl getUserContextOrThrow() {
        UserDetailsImpl userDetails = getUserContext();
        if (userDetails == null) {
            throw new AccessDeniedException("Пользователь не авторизован");
        }
        return userDetails;
    }
}
