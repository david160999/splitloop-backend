package com.example.SplitLoop.auth.controller;

import com.example.SplitLoop.auth.application.command.LoginUseCase;
import com.example.SplitLoop.auth.application.command.RefreshTokenUseCase;
import com.example.SplitLoop.auth.application.command.RegisterUserUseCase;
import com.example.SplitLoop.auth.controller.request.LoginRequest;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/register")
    @Operation(summary = "Register user")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(registerUserUseCase.execute(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(loginUseCase.execute(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {

        return ResponseEntity.ok(refreshTokenUseCase.execute(authHeader));
    }
}