package com.technokratos.agona.service.impl;


import com.technokratos.agona.dto.request.AuthRequest;
import com.technokratos.agona.dto.request.RefreshRequest;
import com.technokratos.agona.dto.request.RegisterRequest;
import com.technokratos.agona.dto.response.AuthResponse;
import com.technokratos.agona.entity.RefreshToken;
import com.technokratos.agona.entity.Role;
import com.technokratos.agona.entity.User;
import com.technokratos.agona.exception.auth.AccountDisabledException;
import com.technokratos.agona.exception.auth.InvalidCredentialsException;
import com.technokratos.agona.exception.user.EmailAlreadyExistsException;
import com.technokratos.agona.exception.user.UsernameAlreadyExistsException;
import com.technokratos.agona.security.provider.JwtAccessTokenProvider;
import com.technokratos.agona.security.userdetails.UserDetailsImpl;
import com.technokratos.agona.service.AuthService;
import com.technokratos.agona.service.RefreshTokenService;
import com.technokratos.agona.service.RoleService;
import com.technokratos.agona.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public static final String DEFAULT_USER_ROLE = "ROLE_USER";

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }
        if (userService.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Role userRole = roleService.getByName(DEFAULT_USER_ROLE);

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(Set.of(userRole))
                .build();

        userService.save(user);

        UserDetailsImpl principal = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .authorities(user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(r.getName()))
                        .toList())
                .build();

        String accessToken = jwtAccessTokenProvider.generateAccessToken(principal);
        Instant accessExpiry = jwtAccessTokenProvider.parseAccessToken(accessToken).getExpiration().toInstant();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .accessTokenExpiresAt(accessExpiry)
                .refreshTokenExpiresAt(refreshToken.getExpiryDate())
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String accessToken = jwtAccessTokenProvider.generateAccessToken((UserDetailsImpl) userDetails);
            Instant accessExpiry = jwtAccessTokenProvider.parseAccessToken(accessToken).getExpiration().toInstant();

            User user = userService.getByUsername(request.getUsername());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .accessTokenExpiresAt(accessExpiry)
                    .refreshTokenExpiresAt(refreshToken.getExpiryDate())
                    .build();

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        } catch (DisabledException e) {
            throw new AccountDisabledException();
        }
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken oldRefreshToken = refreshTokenService.verifyToken(request.getRefreshToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(oldRefreshToken.getUser().getUsername());

        String newAccessToken = jwtAccessTokenProvider.generateAccessToken((UserDetailsImpl) userDetails);
        Instant newAccessExpiry = jwtAccessTokenProvider.parseAccessToken(newAccessToken).getExpiration().toInstant();

        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(oldRefreshToken);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .accessTokenExpiresAt(newAccessExpiry)
                .refreshTokenExpiresAt(newRefreshToken.getExpiryDate())
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
    }
}
