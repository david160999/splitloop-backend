package com.example.SplitLoop.user.domain.service;

import com.example.SplitLoop.user.controller.response.UserResponse;
import com.example.SplitLoop.user.domain.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse getById(UUID id);
    UserResponse getByEmail(String email);

    void changePassword(
            UserEntity userEntity,
            String currentPassword,
            String newPassword,
            String confirmPassword);

    List<UserResponse> getAllUsers();

    void deleteUser(UUID id);

    void updateUser(UserEntity currentUserEntity, String username, String email);
}
