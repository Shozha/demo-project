package com.technokratos.agona.service;

import com.technokratos.agona.dto.request.AuthRequest;
import com.technokratos.agona.dto.request.RefreshRequest;
import com.technokratos.agona.dto.request.RegisterRequest;
import com.technokratos.agona.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);

    AuthResponse refresh(RefreshRequest request);

    void logout(String refreshToken);

}
