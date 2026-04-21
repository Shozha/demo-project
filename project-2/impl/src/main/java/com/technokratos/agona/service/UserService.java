package com.technokratos.agona.service;

import com.technokratos.agona.dto.UserDto;
import com.technokratos.agona.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto getCurrentUser(UUID id);

    List<UserDto> getAllUsers();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User getByUsername(String username);

    void save(User user);

}
