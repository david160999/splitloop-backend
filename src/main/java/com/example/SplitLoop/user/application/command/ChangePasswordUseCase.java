package com.example.SplitLoop.user.application.command;

import com.example.SplitLoop.user.controller.command.ChangePasswordRequest;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final CurrentUserService currentUserService;
    private final UserService userService;

    @Transactional
    public void execute(ChangePasswordRequest request) {

        UserEntity currentUserEntity = currentUserService.getCurrentUser();

        userService.changePassword(
                currentUserEntity,
                request.getCurrentPassword(),
                request.getNewPassword(),
                request.getConfirmPassword());
    }
}
