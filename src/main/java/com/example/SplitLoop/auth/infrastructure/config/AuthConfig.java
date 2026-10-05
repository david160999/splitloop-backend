package com.example.SplitLoop.auth.infrastructure.config;

import com.example.SplitLoop.auth.domain.port.TokenProviderPort;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.balance.domain.service.BalanceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Bean
    public RefreshTokenService refreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            TokenProviderPort tokenProviderPort) {

        return new RefreshTokenService(
                refreshTokenRepository,
                tokenProviderPort,
                refreshExpiration
        );
    }

    @Bean
    public BalanceService balanceService() {
        return new BalanceService();
    }
}