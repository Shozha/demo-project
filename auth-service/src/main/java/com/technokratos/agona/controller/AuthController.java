package com.technokratos.agona.controller;

import com.technokratos.agona.api.AuthApi;
import com.technokratos.agona.dto.request.SignUpRequest;
import com.technokratos.agona.dto.response.TokenPair;
import com.technokratos.agona.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticationService authenticationService;

    @Override
    public TokenPair signUp(@Valid @RequestBody SignUpRequest body) {
        return authenticationService.signUp(body);
    }
}
