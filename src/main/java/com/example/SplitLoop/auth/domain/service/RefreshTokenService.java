package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.persistence.jpa.SpringDataRefreshTokenRepository;
import com.example.SplitLoop.auth.domain.exception.TokenExpiredException;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private final SpringDataRefreshTokenRepository springDataRefreshTokenRepository;

    public RefreshTokenService(SpringDataRefreshTokenRepository springDataRefreshTokenRepository) {
        this.springDataRefreshTokenRepository = springDataRefreshTokenRepository;
    }

    @Transactional
    public RefreshTokenEntity createRefreshToken(UserEntity userEntity) {
        springDataRefreshTokenRepository.deleteByUser(userEntity);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUser(userEntity);
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setExpiryDate(Instant.now().plusMillis(refreshExpiration));

        return springDataRefreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            springDataRefreshTokenRepository.delete(token);
            throw new TokenExpiredException("El Refresh Token ha expirado. Inicie sesión nuevamente.");
        }
        return token;
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return springDataRefreshTokenRepository.findByToken(token);
    }
}
