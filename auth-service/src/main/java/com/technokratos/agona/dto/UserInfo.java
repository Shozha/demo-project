package com.technokratos.agona.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserInfo(
        String userId,
        String username,
        Collection<? extends GrantedAuthority> authorities
) {
    public boolean hasRole(String role) {
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }
}
