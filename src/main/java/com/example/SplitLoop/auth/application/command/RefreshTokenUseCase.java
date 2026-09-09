package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.controller.response.TokenResponse;
import com.example.SplitLoop.auth.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final AuthService authService;

    @Transactional
    public TokenResponse execute(String authHeader) {

        return authService.refreshToken(authHeader);
    }
}
