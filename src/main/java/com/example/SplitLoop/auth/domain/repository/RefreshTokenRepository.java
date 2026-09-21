package com.example.SplitLoop.auth.domain.repository;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.user.domain.entity.UserEntity;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByUserEmail(UserEntity userEntity);
    void deleteByToken(String token);
    int deleteByExpiryDateBefore(Instant now);
}
