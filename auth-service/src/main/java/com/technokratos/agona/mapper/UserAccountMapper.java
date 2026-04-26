package com.technokratos.agona.mapper;

import com.technokratos.agona.entity.UserAccount;
import com.technokratos.agona.dto.request.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashPassword", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserAccount toEntity(SignUpRequest request);
}
