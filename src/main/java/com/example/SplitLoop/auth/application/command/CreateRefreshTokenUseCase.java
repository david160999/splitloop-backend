package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.port.TokenGenerator;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRefreshTokenUseCase {

//    @Value("${application.security.jwt.refresh-token.expiration}")
//    private long refreshExpiration;
//
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final TokenGenerator tokenGenerator;
//
//    @Transactional
//    public RefreshTokenResult execute(CreateRefreshTokenCommand command) {
//        // 1. Invalida tokens previos del usuario (Rotación)
//        refreshTokenRepository.deleteByUserId(command.userId());
//
//        // 2. Genera nuevo token y calcula expiración
//        String tokenValue = tokenGenerator.generate();
//        Instant expiryDate = Instant.now().plusMillis(refreshExpiration);
//
//        // 3. Persiste mediante el repositorio de Dominio
//        RefreshToken savedToken = refreshTokenRepository.save(command.userId(), tokenValue, expiryDate);
//
//        // 4. Retorna el resultado desacoplado como DTO
//        return new RefreshTokenResult(
//                savedToken.getToken(),
//                savedToken.getExpiryDate()
//        );
//    }
}