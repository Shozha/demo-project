package com.technokratos.agona.mapper;

import com.technokratos.agona.dto.UserDto;
import com.technokratos.agona.entity.Role;
import com.technokratos.agona.entity.User;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper
public class UserMapper {

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
