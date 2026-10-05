package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.port.AuthenticationPort;
import com.example.SplitLoop.auth.application.dto.request.LoginRequest;
import com.example.SplitLoop.auth.application.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.port.TokenProviderPort;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final AuthenticationPort authenticationPort;
    private final RefreshTokenService refreshTokenService;
    private final TokenProviderPort tokenProviderPort;

    @Transactional
    public AuthResponse execute(LoginRequest request) {

        User auth = authenticationPort.authenticate(request.email(), request.password());

        String accessToken = tokenProviderPort.generateAccessToken(auth);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(auth);

        return new AuthResponse(accessToken, refreshToken.token());

    }
}
