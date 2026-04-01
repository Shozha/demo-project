package com.technokratos.agona.security.userdetails;

import com.technokratos.agona.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public UUID getId()       { return user.getId(); }

    @Override
    public String getUsername()  { return user.getUsername(); }
    @Override
    public String getPassword()  { return user.getPassword(); }
    @Override
    public boolean isEnabled()   { return user.isEnabled(); }

    @Override
    public boolean isAccountNonExpired()     { return true; }
    @Override
    public boolean isAccountNonLocked()      { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
}
