package com.technokratos.agona.controller;

import com.technokratos.agona.api.SessionsApi;
import com.technokratos.agona.exception.token.InvalidRefreshTokenException;
import com.technokratos.agona.service.AuthenticationService;
import com.technokratos.agona.dto.request.RefreshTokenRequest;
import com.technokratos.agona.dto.request.SignInRequest;
import com.technokratos.agona.dto.response.TokenPair;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionsController implements SessionsApi {

    private final AuthenticationService authenticationService;

    @Override
    public TokenPair signIn(@Valid @RequestBody SignInRequest body) {
        return authenticationService.signIn(body);
    }

    @Override
    public void logout(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                       @Valid @RequestBody RefreshTokenRequest fingerprint) {
        if (refreshToken == null) {
            throw new InvalidRefreshTokenException();
        }
        authenticationService.logout(fingerprint, refreshToken);
    }
}
