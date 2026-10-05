package com.example.SplitLoop.auth.domain.model;

import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record RefreshToken(
        UUID id,
        String token,
        boolean revoked,
        User user,
        LocalDateTime createdAt,
        Instant expiryDate
) {

    // Regla de Negocio: Verificar si el token ha expirado
    public boolean hasExpired() {
        return expiryDate != null && expiryDate.isBefore(Instant.now());
    }

    // Regla de Negocio: Devuelve una NUEVA instancia con el token revocado (Patrón Wither)
    public RefreshToken revoke() {
        return this.toBuilder()
                .revoked(true)
                .build();
    }
}
