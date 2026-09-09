package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.controller.request.LoginRequest;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.response.TokenResponse;
import com.example.SplitLoop.auth.domain.entity.Token;
import com.example.SplitLoop.auth.domain.repository.TokenRepository;
import com.example.SplitLoop.auth.exception.InvalidBearerTokenException;
import com.example.SplitLoop.auth.exception.InvalidRefreshTokenException;
import com.example.SplitLoop.group.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        saveUserToken(savedUser, accessToken, Token.TokenType.ACCESS);
        saveUserToken(savedUser, refreshToken, Token.TokenType.REFRESH);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request) {

        if (!userRepository.existsByEmail(request.email())) {
            throw new UserNotFoundException(request.email());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow();

        revokeAccessTokens(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(user, accessToken, Token.TokenType.ACCESS);
        saveUserToken(user, refreshToken, Token.TokenType.REFRESH);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidBearerTokenException();
        }

        String refreshToken = authHeader.substring(7);

        Token storedToken = tokenRepository.findByToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (storedToken.getTokenType() != Token.TokenType.REFRESH) {
            throw new InvalidRefreshTokenException();
        }

        if (storedToken.isRevoked() || storedToken.hasExpired()) {
            throw new InvalidRefreshTokenException();
        }

        String userEmail = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new InvalidRefreshTokenException();
        }

        // Revocar el refresh token utilizado (rotación)
        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);

        // Revocar todos los access tokens anteriores
        revokeAccessTokens(user);

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(user, newAccessToken, Token.TokenType.ACCESS);
        saveUserToken(user, newRefreshToken, Token.TokenType.REFRESH);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    private void revokeAccessTokens(User user) {

        List<Token> validAccessTokens =
                tokenRepository.findByUserIdAndTokenTypeAndRevokedFalse(
                        user.getId(),
                        Token.TokenType.ACCESS
                );


        if (validAccessTokens.isEmpty()) {
            return;
        }

        validAccessTokens.forEach(token -> {
            token.setRevoked(true);
            tokenRepository.save(token);
        });

    }

    private void saveUserToken(
            User user,
            String token,
            Token.TokenType tokenType
    ) {

        Token entity = Token.builder()
                .user(user)
                .token(token)
                .tokenType(tokenType)
                .revoked(false)
                .expiresAt(
                        jwtService.extractExpiration(token)
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                )
                .build();

        tokenRepository.save(entity);
    }
}
