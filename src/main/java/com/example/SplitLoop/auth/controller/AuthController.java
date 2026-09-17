package com.example.SplitLoop.auth.controller;

import com.example.SplitLoop.auth.application.command.LoginUseCase;
import com.example.SplitLoop.auth.application.command.RefreshTokenUseCase;
import com.example.SplitLoop.auth.application.command.RegisterUserUseCase;
import com.example.SplitLoop.auth.controller.request.LoginRequest;
import com.example.SplitLoop.auth.controller.request.RefreshTokenRequest;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

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
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(registerUserUseCase.execute(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = loginUseCase.execute(request);

        // Guardar el Refresh Token en una Cookie HttpOnly
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authResponse.refreshToken())
                .httpOnly(true)
                .secure(true) // Solo HTTPS en prod
                .path("/auth/refresh-token") // Limitar la cookie solo al endpoint de refresh
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(authResponse); // O devuelves solo el accessToken en el cuerpo
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @CookieValue(value = "refreshToken", required = false) String cookieRefreshToken
    ) {
        // Soporta recibir el token por Header (Bearer) O por Cookie HttpOnly
        String tokenToVerify = null;

        if (cookieRefreshToken != null) {
            tokenToVerify = cookieRefreshToken;
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenToVerify = authHeader.substring(7);
        }

        AuthResponse authResponse = refreshTokenUseCase.execute(tokenToVerify);

        return ResponseEntity.ok(authResponse);
    }
}