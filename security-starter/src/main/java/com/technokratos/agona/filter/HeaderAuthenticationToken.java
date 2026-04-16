package com.technokratos.agona.filter;

import com.technokratos.agona.dto.UserInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class HeaderAuthenticationToken extends AbstractAuthenticationToken {

    private final UserInfo principal;

    public HeaderAuthenticationToken(UserInfo principal, Collection<? extends GrantedAuthority> authorities) {
        super(validateAuthorities(authorities));
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    private static Collection<? extends GrantedAuthority> validateAuthorities(
            Collection<? extends GrantedAuthority> authorities) {
        return authorities == null ? Collections.emptyList() : authorities;
    }

}
