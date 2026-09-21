package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void execute(String refreshTokenStr) {
        if (refreshTokenStr == null || refreshTokenStr.isBlank()) {
            return;
        }

        refreshTokenRepository.deleteByToken(refreshTokenStr);
    }
}
