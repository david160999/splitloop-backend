package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.controller.request.LoginRequest;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.response.AuthResponse;
import com.example.SplitLoop.auth.domain.entity.RefreshToken;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.auth.exception.InvalidBearerTokenException;
import com.example.SplitLoop.auth.exception.InvalidRefreshTokenException;
import com.example.SplitLoop.auth.exception.RefreshTokenRequiredException;
import com.example.SplitLoop.group.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.user.domain.entity.Role;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Genera el constructor para la inyección de dependencias final
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
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
        String accessToken = jwtService.generateAccessToken(savedUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse refreshToken(String refreshTokenStr) {
        if (refreshTokenStr == null || refreshTokenStr.isBlank()) {
            throw new RefreshTokenRequiredException("El Refresh Token es requerido");
        }

        // 1. Buscar en BD
        RefreshToken oldToken = refreshTokenService.findByToken(refreshTokenStr)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh Token no encontrado"));

        // 2. Validar expiración (Si expiró, el servicio lo elimina de la BD y lanza excepción)
        refreshTokenService.verifyExpiration(oldToken);

        User user = oldToken.getUser();

        // 3. Generar nuevos tokens:
        // createRefreshToken(user) BORRA el viejo token del usuario y GUARDA el nuevo en un solo paso
        String newAccessToken = jwtService.generateAccessToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(newAccessToken, newRefreshToken.getToken());
    }

    @Transactional
    public void logoutWithBearerToken(final String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return;
        }

        String accessToken = tokenHeader.substring(7);
        String userEmail = null;

        try {
            userEmail = jwtService.extractUsername(accessToken);
        } catch (Exception e) {
            // El token expiró o es inválido. No podemos identificar al usuario,
            // pero devolvemos el control limpiamente sin tocar la BD.
            return;
        }

        if (userEmail != null) {
            deleteRefreshTokenForUser(userEmail);
        }
    }

    @Transactional
    public void deleteRefreshTokenForUser(String email) {
        userRepository.findByEmail(email).ifPresent(refreshTokenRepository::deleteByUser);
    }
}