package com.example.SplitLoop.auth.application.dto.request;


import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @NotBlank(message = "Email is required.")
        String refreshToken

) {
}

