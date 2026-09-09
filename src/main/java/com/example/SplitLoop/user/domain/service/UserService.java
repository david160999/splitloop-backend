package com.example.SplitLoop.user.domain.service;

import com.example.SplitLoop.user.controller.command.CreateUserRequest;
import com.example.SplitLoop.user.controller.response.UserResponse;
import com.example.SplitLoop.user.domain.entity.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse getById(UUID id);
    UserResponse getByEmail(String email);

    void changePassword(
            User user,
            String currentPassword,
            String newPassword,
            String confirmPassword);

    List<UserResponse> getAllUsers();

    void deleteUser(UUID id);

    void updateUser(User currentUser, String username, String email);
}
