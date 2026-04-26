package com.technokratos.agona.controller;

import com.technokratos.agona.api.TokenApi;
import com.technokratos.agona.exception.token.InvalidRefreshTokenException;
import com.technokratos.agona.service.AuthenticationService;
import com.technokratos.agona.dto.request.RefreshTokenRequest;
import com.technokratos.agona.dto.response.TokenPair;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController implements TokenApi {

    private final AuthenticationService authenticationService;

    @Override
    public TokenPair refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                             @Valid @RequestBody RefreshTokenRequest fingerprint) {
        if (refreshToken == null) {
            throw new InvalidRefreshTokenException();
        }
        return authenticationService.refreshToken(fingerprint, refreshToken);
    }
}
