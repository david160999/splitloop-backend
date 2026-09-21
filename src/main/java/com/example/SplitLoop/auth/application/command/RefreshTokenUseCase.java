package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.response.AuthResponse;
import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.domain.service.JwtService;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.auth.domain.exception.InvalidRefreshTokenException;
import com.example.SplitLoop.auth.application.exception.RefreshTokenRequiredException;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse execute(String refreshTokenStr) {

        if (refreshTokenStr == null || refreshTokenStr.isBlank()) {
            throw new RefreshTokenRequiredException("El Refresh Token es requerido");
        }

        // 1. Buscar en BD
        RefreshTokenEntity oldToken = refreshTokenService.findByToken(refreshTokenStr)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh Token no encontrado"));

        // 2. Validar expiración (Si expiró, el servicio lo elimina de la BD y lanza excepción)
        refreshTokenService.verifyExpiration(oldToken);

        UserEntity userEntity = oldToken.getUser();

        // 3. Generar nuevos tokens:
        // createRefreshToken(user) BORRA el viejo token del usuario y GUARDA el nuevo en un solo paso
        String newAccessToken = jwtService.generateAccessToken(userEntity);
        RefreshTokenEntity newRefreshTokenEntity = refreshTokenService.createRefreshToken(userEntity);

        return new AuthResponse(newAccessToken, newRefreshTokenEntity.getToken());
    }
}
