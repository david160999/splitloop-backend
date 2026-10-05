package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.port.TokenProviderPort;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.auth.domain.exception.TokenExpiredException;
import com.example.SplitLoop.user.domain.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final TokenProviderPort tokenProviderPort;
    private final long expirationMillis;

    public RefreshTokenService(RefreshTokenRepository repository,
                               TokenProviderPort tokenProviderPort,
                               long expirationMillis) {
        this.repository = repository;
        this.tokenProviderPort = tokenProviderPort;
        this.expirationMillis = expirationMillis;
    }

    public RefreshToken createRefreshToken(User user) {
        // 1. Elimina tokens anteriores asociados al email del usuario record
        repository.deleteByUserEmail(user.email());

        // 2. Construye la instancia inmutable del record
        RefreshToken refreshToken = new RefreshToken(
                null,
                tokenProviderPort.generateToken(),
                false,
                user,
                LocalDateTime.now(),
                Instant.now().plusMillis(expirationMillis)
        );

        // 3. Persiste mediante la interfaz del repositorio de Dominio
        return repository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        // Aprovecha la regla de negocio pura encapsulada en el record RefreshToken
        if (token.hasExpired()) {
            repository.delete(token);
            throw new TokenExpiredException("El Refresh Token ha expirado. Inicie sesión nuevamente.");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }
}
