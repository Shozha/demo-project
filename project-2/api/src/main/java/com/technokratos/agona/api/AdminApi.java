package com.technokratos.agona.api;

import com.technokratos.agona.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * REST-контракт для административных endpoint-ов.
 */
@RequestMapping("/api/admin")
public interface AdminApi {

    @GetMapping("/users")
    List<UserDto> getAllUsers();
}
