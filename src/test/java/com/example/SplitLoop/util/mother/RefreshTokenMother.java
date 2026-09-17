package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.auth.domain.entity.RefreshToken;
import com.example.SplitLoop.user.domain.entity.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public final class RefreshTokenMother {

    private RefreshTokenMother() {
    }

    /**
     * Genera un RefreshToken válido con 7 días de duración para el usuario por defecto (UserMother.user()).
     */
    public static RefreshToken refreshToken() {
        return refreshTokenForUser(UserMother.user());
    }

    /**
     * Genera un RefreshToken válido para un usuario específico.
     */
    public static RefreshToken refreshTokenForUser(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(UUID.randomUUID());
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        return refreshToken;
    }

    /**
     * Genera un RefreshToken expirado (1 hora en el pasado) para probar casos de fallo.
     */
    public static RefreshToken expiredRefreshToken() {
        RefreshToken token = refreshToken();
        token.setExpiryDate(Instant.now().minus(1, ChronoUnit.HOURS));
        return token;
    }
}
