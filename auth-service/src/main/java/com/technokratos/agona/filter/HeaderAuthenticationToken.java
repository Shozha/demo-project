package com.technokratos.agona.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class HeaderAuthenticationToken extends AbstractAuthenticationToken {

    private final String userId;
    private final String username;

    public HeaderAuthenticationToken(String userId,
                                     String username,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        this.username = username;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
