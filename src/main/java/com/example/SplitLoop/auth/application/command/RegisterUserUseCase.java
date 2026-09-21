package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.request.RegisterRequest;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.response.AuthResponse;
import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.domain.service.JwtService;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.group.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.user.domain.entity.Role;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse execute(RegisterRequest request) {

        // 1. Validar que el email no exista
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        // 2. Construir y guardar el nuevo usuario con contraseña encriptada
        UserEntity userEntity = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        UserEntity savedUserEntity = userRepository.save(userEntity);

        // 3. Generar los tokens (El Refresh Token se persiste automáticamente dentro del jwtService)
        String accessToken = jwtService.generateAccessToken(savedUserEntity);
        RefreshTokenEntity refreshTokenEntity = refreshTokenService.createRefreshToken(savedUserEntity);

        return new AuthResponse(accessToken, refreshTokenEntity.getToken());
    }
}
