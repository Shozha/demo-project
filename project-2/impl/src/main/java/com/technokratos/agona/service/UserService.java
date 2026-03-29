package com.technokratos.agona.service;

import com.technokratos.agona.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto getCurrentUser(UUID id);

    List<UserDto> getAllUsers();

}
