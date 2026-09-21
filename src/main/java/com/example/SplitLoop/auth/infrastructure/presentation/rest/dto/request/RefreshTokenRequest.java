package com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.request;


import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @NotBlank(message = "Email is required.")
        String refreshToken

) {
}

