package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.application.dto.request.RegisterRequest;
import com.example.SplitLoop.auth.application.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.port.TokenProviderPort;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.group.domain.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.user.infrastructure.persistence.entity.Role;
import com.example.SplitLoop.user.domain.model.User;
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
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponse execute(RegisterRequest request) {

        // 1. Validar que el email no exista
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        // 2. Construir y guardar el nuevo usuario con contraseña encriptada
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        // 3. Generar los tokens (El Refresh Token se persiste automáticamente dentro del jwtService)
        String accessToken = tokenProviderPort.generateAccessToken(savedUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);

        return new AuthResponse(accessToken, refreshToken.token());
    }
}
