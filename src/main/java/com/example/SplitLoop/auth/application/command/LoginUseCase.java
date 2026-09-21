package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.request.LoginRequest;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.service.JwtService;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse execute(LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserEntity entity = (UserEntity) auth.getPrincipal();

        String accessToken = jwtService.generateAccessToken(entity);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(entity);

        return new AuthResponse(accessToken, refreshToken.getToken());

    }
}
