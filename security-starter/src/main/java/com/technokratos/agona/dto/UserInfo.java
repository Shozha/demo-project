package com.technokratos.agona.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
public record UserInfo(
        String userId,
        String username,
        Collection<? extends GrantedAuthority> authorities
) {
    public boolean hasRole(String role) {
        String normalizedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(normalizedRole));
    }
}
