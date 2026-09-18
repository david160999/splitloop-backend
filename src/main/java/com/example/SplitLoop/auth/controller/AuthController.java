package com.example.SplitLoop.auth.controller;

import com.example.SplitLoop.auth.application.command.LoginUseCase;
import com.example.SplitLoop.auth.application.command.RefreshTokenUseCase;
import com.example.SplitLoop.auth.application.command.RegisterUserUseCase;
import com.example.SplitLoop.auth.controller.request.LoginRequest;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.response.AccessTokenResponse;
import com.example.SplitLoop.auth.controller.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @Value("${application.security.cookie.secure:false}")
    private boolean isSecureCookie;

    @PostMapping("/register")
    @Operation(summary = "Register user")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        registerUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    public ResponseEntity<AccessTokenResponse> login(@Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = loginUseCase.execute(request);

        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(authResponse.refreshToken());

        AccessTokenResponse responseBody = new AccessTokenResponse(authResponse.accessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(responseBody);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<AccessTokenResponse> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ) {
        // 1. Validar que la cookie esté presente
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Ejecutar la lógica (valida expiración, borra el token anterior y crea uno nuevo)
        AuthResponse authResponse = refreshTokenUseCase.execute(refreshToken);

        // 3. Crear la nueva cookie HttpOnly con el nuevo Refresh Token
        ResponseCookie newRefreshTokenCookie = createRefreshTokenCookie(authResponse.refreshToken());

        // 4. Responder separando las vías de transporte
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
                .body(new AccessTokenResponse(authResponse.accessToken()));
    }

    private ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(isSecureCookie)
                .path("/auth/refresh-token") // Coincide exactamente con el endpoint
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax") // Cambiado a Lax para evitar problemas de CORS en desarrollo
                .build();
    }
}