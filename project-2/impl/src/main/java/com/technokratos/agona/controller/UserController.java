package com.technokratos.agona.controller;

import com.technokratos.agona.api.UserApi;
import com.technokratos.agona.dto.UserDto;
import com.technokratos.agona.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PreAuthorize("hasRole('USER')")
    public UserDto me(@AuthenticationPrincipal(expression = "id") UUID userId) {
        return userService.getCurrentUser(userId);
    }
}
