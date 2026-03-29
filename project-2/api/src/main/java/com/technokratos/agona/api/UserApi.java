package com.technokratos.agona.api;

import com.technokratos.agona.dto.UserDto;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * REST-контракт для пользовательских endpoint-ов.
 */
@RequestMapping("/api/users")
public interface UserApi {

    @GetMapping("/me")
    UserDto me(@AuthenticationPrincipal(expression = "id") UUID userId);
}
