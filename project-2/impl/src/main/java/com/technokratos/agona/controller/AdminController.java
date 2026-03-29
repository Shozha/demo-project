package com.technokratos.agona.controller;

import com.technokratos.agona.api.AdminApi;
import com.technokratos.agona.dto.UserDto;
import com.technokratos.agona.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController implements AdminApi {

    private final UserService userService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
