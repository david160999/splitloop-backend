package com.example.SplitLoop.user.application.mapper;

import com.example.SplitLoop.user.application.dto.response.UserResponse;
import com.example.SplitLoop.user.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    UserResponse toResponse(User user);
}
