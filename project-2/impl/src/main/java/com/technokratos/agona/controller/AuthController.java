package com.technokratos.agona.controller;

import com.technokratos.agona.api.AuthApi;
import com.technokratos.agona.dto.request.AuthRequest;
import com.technokratos.agona.dto.request.RefreshRequest;
import com.technokratos.agona.dto.request.RegisterRequest;
import com.technokratos.agona.dto.response.AuthResponse;
import com.technokratos.agona.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        return authService.register(request);
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        return authService.login(request);
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        return authService.refresh(request);
    }

    @Override
    public void logout(RefreshRequest request) {
        authService.logout(request.getRefreshToken());
    }
}
