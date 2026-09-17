package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(TokenCleanupScheduler.class);

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    // Se ejecuta todos los días a la medianoche (cron: segundo minuto hora día mes día_semana)
    @Scheduled(cron = "0 0 0 * * ?")
    public void purgeExpiredTokens() {
        log.info("Iniciando purga automática de Refresh Tokens expirados...");

        int deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());

        log.info("Purga completada. Se eliminaron {} tokens obsoletos.", deletedCount);
    }
}
