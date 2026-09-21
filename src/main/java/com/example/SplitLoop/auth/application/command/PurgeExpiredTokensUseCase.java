package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurgeExpiredTokensUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public int execute() {
        log.info("Iniciando purga automática de Refresh Tokens expirados...");
        int deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
        log.info("Purga completada. Se eliminaron {} tokens obsoletos.", deletedCount);
        return deletedCount;
    }
}