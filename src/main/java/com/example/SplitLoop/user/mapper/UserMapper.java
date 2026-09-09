package com.example.SplitLoop.user.mapper;

import com.example.SplitLoop.user.controller.command.CreateUserRequest;
import com.example.SplitLoop.user.controller.response.UserResponse;
import com.example.SplitLoop.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "username")
    UserResponse toResponse(User user);
}