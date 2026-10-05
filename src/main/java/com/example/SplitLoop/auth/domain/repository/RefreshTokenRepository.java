package com.example.SplitLoop.auth.domain.repository;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserEmail(String email);
    void deleteByToken(String token);
    int deleteByExpiryDateBefore(Instant now);

    RefreshToken save(RefreshToken refreshToken);
    void delete(RefreshToken token);
}
