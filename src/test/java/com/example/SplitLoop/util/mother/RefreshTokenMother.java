package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public final class RefreshTokenMother {

    private RefreshTokenMother() {
    }

    // =========================================================================
    // 1. MODELOS DE DOMINIO (Para UseCases y Tests Unitarios)
    // =========================================================================

    public static RefreshToken refreshTokenModel() {
        return refreshTokenModelForUser(UserMother.userModel());
    }

    public static RefreshToken refreshTokenModelForUser(User user) {
        return RefreshToken.builder()
                .id(UUID.fromString("223e4567-e89b-12d3-a456-426614174000"))
                .user(user)
                .token("valid-refresh-token-uuid")
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
    }

    public static RefreshToken expiredRefreshTokenModel() {
        RefreshToken token = refreshTokenModel();
        token.setExpiryDate(Instant.now().minus(1, ChronoUnit.HOURS));
        return token;
    }

    // =========================================================================
    // 2. ENTIDADES JPA DE INFRAESTRUCTURA (Para RepositoryImpl y Mappers)
    // =========================================================================

    public static RefreshTokenEntity refreshTokenEntity() {
        return refreshTokenEntityForUser(UserMother.userEntity());
    }

    public static RefreshTokenEntity refreshTokenEntityForUser(UserEntity userEntity) {
        RefreshToken domain = refreshTokenModelForUser(UserMother.userModel());

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(domain.getId());
        entity.setUser(userEntity);
        entity.setToken(domain.getToken());
        entity.setRevoked(domain.isRevoked());
        entity.setExpiryDate(domain.getExpiryDate());
        return entity;
    }

    public static RefreshTokenEntity expiredRefreshTokenEntity() {
        RefreshTokenEntity entity = refreshTokenEntity();
        entity.setExpiryDate(Instant.now().minus(1, ChronoUnit.HOURS));
        return entity;
    }
}
