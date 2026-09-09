package com.example.SplitLoop.user.controller;

import com.example.SplitLoop.user.application.command.ChangePasswordUseCase;
import com.example.SplitLoop.user.application.command.SearchUsersUseCase;
import com.example.SplitLoop.user.application.command.UpdateCurrentUserUseCase;
import com.example.SplitLoop.user.controller.command.ChangePasswordRequest;
import com.example.SplitLoop.user.controller.command.UpdateUserRequest;
import com.example.SplitLoop.user.controller.query.SearchUsersQuery;
import com.example.SplitLoop.user.controller.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final SearchUsersUseCase searchUsersUseCase;
    private final UpdateCurrentUserUseCase updateCurrentUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    @GetMapping("/search")
    @Operation(summary = "Search users")
    public ResponseEntity<List<UserResponse>> searchUsers(@Valid SearchUsersQuery query) {

        return ResponseEntity.ok(searchUsersUseCase.execute(query));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user")
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(updateCurrentUserUseCase.execute(request));
    }

    @PostMapping("/me/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {

        changePasswordUseCase.execute(request);

        return ResponseEntity.noContent().build();
    }
}