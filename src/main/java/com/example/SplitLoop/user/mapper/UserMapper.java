package com.example.SplitLoop.user.mapper;

import com.example.SplitLoop.user.controller.response.UserResponse;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "username")
    UserResponse toResponse(UserEntity userEntity);

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}