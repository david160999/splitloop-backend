package com.example.SplitLoop.auth.controller.request;

public final class RequestMother {

    private RequestMother() {
    }

    public static RegisterRequest registerRequest() {
        return new RegisterRequest("john", "john@test.com", "password123");
    }

    public static RegisterRequest invalidRegisterRequest() {
        return new RegisterRequest("", "email-invalido", "");
    }

    public static LoginRequest loginRequest() {
        return new LoginRequest("john@test.com", "password123");
    }

    public static LoginRequest invalidLoginRequest() {
        return new LoginRequest("email-invalido", "");
    }
}
