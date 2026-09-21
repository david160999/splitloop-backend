package com.example.SplitLoop.auth.infrastructure.scheduler;

import com.example.SplitLoop.auth.application.command.PurgeExpiredTokensUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final PurgeExpiredTokensUseCase purgeExpiredTokensUseCase;

    // Se ejecuta todos los días a la medianoche
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleTokenPurge() {
        purgeExpiredTokensUseCase.execute();
    }
}
