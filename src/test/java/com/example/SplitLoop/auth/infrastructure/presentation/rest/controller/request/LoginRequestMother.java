package com.example.SplitLoop.auth.infrastructure.presentation.rest.controller.request;

import com.example.SplitLoop.auth.application.dto.request.LoginRequest;
import com.example.SplitLoop.auth.application.dto.request.RegisterRequest;

public class LoginRequestMother {

    // --- CREDENCIALES VÁLIDAS (HAPPY PATH) ---

    public static LoginRequest valid() {
        return new LoginRequest("user@example.com", "Password123!");
    }

    public static LoginRequest custom(String email, String password) {
        return new LoginRequest(email, password);
    }

    public static LoginRequest fromRegisterRequest(RegisterRequest registerRequest) {
        return new LoginRequest(registerRequest.email(), registerRequest.password());
    }

    // --- CASOS DE ERROR Y VALIDACIÓN ---

    public static LoginRequest wrongPassword() {
        return new LoginRequest("user@example.com", "WrongPass123!");
    }

    public static LoginRequest nonExistentEmail() {
        return new LoginRequest("nobody@example.com", "Password123!");
    }

    public static LoginRequest invalidEmailFormat() {
        return new LoginRequest("email-invalido", "Password123!");
    }

    public static LoginRequest emptyCredentials() {
        return new LoginRequest("", "");
    }
}
