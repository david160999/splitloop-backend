package com.example.SplitLoop.auth.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        String tokenType,
        long expiresIn
) {
    // Constructor secundario con valores por defecto para conveniencia
    public AccessTokenResponse(String accessToken) {
        this(accessToken, "Bearer", 900); // 900 segundos = 15 minutos (ajusta según la vida de tu token)
    }
}
