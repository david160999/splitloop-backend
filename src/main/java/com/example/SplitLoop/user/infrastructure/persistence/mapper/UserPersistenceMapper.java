package com.example.SplitLoop.user.infrastructure.persistence.mapper;

import com.example.SplitLoop.user.application.dto.response.UserResponse;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.common.infrastructure.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    UserResponse toResponse(User user);

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);

    // CustomUserDetails -> Domain Model
    @Mapping(target = "password", ignore = true) // Ignoramos la contraseña por seguridad si no la necesitas en el dominio
    User toDomain(CustomUserDetails userDetails);
}