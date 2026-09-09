package com.example.SplitLoop.user.application.command;

import com.example.SplitLoop.user.controller.command.UpdateUserRequest;
import com.example.SplitLoop.user.controller.response.UserResponse;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.user.domain.service.UserService;
import com.example.SplitLoop.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCurrentUserUseCase {

    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse execute(UpdateUserRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        userService.updateUser(currentUser, request.getUsername(), request.getEmail());

        return userMapper.toResponse(currentUser);
    }
}
