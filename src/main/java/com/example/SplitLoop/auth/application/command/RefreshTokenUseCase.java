package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.application.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.port.TokenProviderPort;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.auth.domain.exception.InvalidRefreshTokenException;
import com.example.SplitLoop.auth.application.exception.RefreshTokenRequiredException;
import com.example.SplitLoop.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {

    private final RefreshTokenService refreshTokenService;
    private final TokenProviderPort tokenProviderPort;


    @Transactional
    public AuthResponse execute(String refreshTokenStr) {

        if (refreshTokenStr == null || refreshTokenStr.isBlank()) {
            throw new RefreshTokenRequiredException("El Refresh Token es requerido");
        }

        // 1. Buscar en BD
        RefreshToken oldToken = refreshTokenService.findByToken(refreshTokenStr)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh Token no encontrado"));

        // 2. Validar expiración (Si expiró, el servicio lo elimina de la BD y lanza excepción)
        refreshTokenService.verifyExpiration(oldToken);

        User user = oldToken.user();

        // 3. Generar nuevos tokens:
        // createRefreshToken(user) BORRA el viejo token del usuario y GUARDA el nuevo en un solo paso
        String newAccessToken = tokenProviderPort.generateAccessToken(user);
        RefreshToken newRefreshTokenEntity = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(newAccessToken, newRefreshTokenEntity.token());
    }
}
