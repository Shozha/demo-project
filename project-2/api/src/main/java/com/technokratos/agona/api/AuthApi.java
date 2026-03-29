package com.technokratos.agona.api;

import com.technokratos.agona.dto.request.AuthRequest;
import com.technokratos.agona.dto.request.RefreshRequest;
import com.technokratos.agona.dto.request.RegisterRequest;
import com.technokratos.agona.dto.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * REST-контракт для аутентификации.
 */
@RequestMapping("/api/auth")
public interface AuthApi {

    @PostMapping("/register")
    AuthResponse register(@Valid @RequestBody RegisterRequest request);

    @PostMapping("/login")
    AuthResponse login(@Valid @RequestBody AuthRequest request);

    @PostMapping("/refresh")
    AuthResponse refresh(@Valid @RequestBody RefreshRequest request);

    @PostMapping("/logout")
    void logout(@Valid @RequestBody RefreshRequest request);
}
