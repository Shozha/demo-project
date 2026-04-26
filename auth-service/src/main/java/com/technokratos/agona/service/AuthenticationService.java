package com.technokratos.agona.service;

import com.technokratos.agona.dto.request.RefreshTokenRequest;
import com.technokratos.agona.dto.request.SignInRequest;
import com.technokratos.agona.dto.request.SignUpRequest;
import com.technokratos.agona.dto.response.TokenPair;

public interface AuthenticationService {

    TokenPair signUp(SignUpRequest request);

    TokenPair signIn(SignInRequest request);

    TokenPair refreshToken(RefreshTokenRequest request, String rawRefreshToken);

    void logout(RefreshTokenRequest request, String rawRefreshToken);
}
