package com.technokratos.agona.service.impl;


import com.technokratos.agona.dto.request.AuthRequest;
import com.technokratos.agona.dto.request.RefreshRequest;
import com.technokratos.agona.dto.request.RegisterRequest;
import com.technokratos.agona.dto.response.AuthResponse;
import com.technokratos.agona.entity.RefreshToken;
import com.technokratos.agona.entity.Role;
import com.technokratos.agona.entity.User;
import com.technokratos.agona.repository.RoleRepository;
import com.technokratos.agona.repository.UserRepository;
import com.technokratos.agona.security.JwtTokenProvider;
import com.technokratos.agona.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already taken");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found"));

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        Instant accessExpiry = jwtTokenProvider.getExpiryFromToken(accessToken);

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

            String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
            Instant accessExpiry = jwtTokenProvider.getExpiryFromToken(accessToken);

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .accessTokenExpiresAt(accessExpiry)
                    .refreshTokenExpiresAt(refreshToken.getExpiryDate())
                    .build();

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account is disabled");
        }
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken oldRefreshToken = refreshTokenService.verifyToken(request.getRefreshToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(oldRefreshToken.getUser().getUsername());

        String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
        Instant newAccessExpiry = jwtTokenProvider.getExpiryFromToken(newAccessToken);

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
