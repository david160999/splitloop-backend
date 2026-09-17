package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.domain.entity.RefreshToken;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.auth.exception.TokenExpiredException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.util.mother.RefreshTokenMother;
import com.example.SplitLoop.util.mother.UserMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private final long testRefreshExpiration = 604800000L; // 7 días en ms

    @BeforeEach
    void setUp() {
        // En el setUp solo mantenemos la inyección de propiedades inmutables
        ReflectionTestUtils.setField(refreshTokenService, "refreshExpiration", testRefreshExpiration);
    }

    @Test
    @DisplayName("Debe eliminar tokens anteriores y crear un nuevo RefreshToken válido")
    void debeCrearRefreshToken() {
        User usuario = UserMother.user();

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken tokenCreado = refreshTokenService.createRefreshToken(usuario);

        verify(refreshTokenRepository).deleteByUser(usuario);

        ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(tokenCaptor.capture());
        RefreshToken tokenGuardado = tokenCaptor.getValue();

        assertThat(tokenCreado).isNotNull();
        assertThat(tokenGuardado.getUser()).isEqualTo(usuario);
        assertThat(tokenGuardado.getToken()).isNotNull();
        assertThat(UUID.fromString(tokenGuardado.getToken())).isNotNull();
        assertThat(tokenGuardado.getExpiryDate()).isAfter(Instant.now());
    }

    @Test
    @DisplayName("Debe retornar el token si no ha expirado")
    void debeValidarTokenNoExpirado() {
        RefreshToken tokenValido = RefreshTokenMother.refreshToken();

        RefreshToken resultado = refreshTokenService.verifyExpiration(tokenValido);

        assertThat(resultado).isEqualTo(tokenValido);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Debe eliminar el token y lanzar TokenExpiredException si está expirado")
    void debeEliminarYLanzarExcepcionSiTokenExpiro() {
        RefreshToken tokenExpirado = RefreshTokenMother.expiredRefreshToken();

        assertThatThrownBy(() -> refreshTokenService.verifyExpiration(tokenExpirado))
                .isInstanceOf(TokenExpiredException.class);

        verify(refreshTokenRepository).delete(tokenExpirado);
    }

    @Test
    @DisplayName("Debe buscar un RefreshToken por su cadena de texto")
    void debeBuscarPorToken() {
        RefreshToken tokenEsperado = RefreshTokenMother.refreshToken();

        when(refreshTokenRepository.findByToken(tokenEsperado.getToken()))
                .thenReturn(Optional.of(tokenEsperado));

        Optional<RefreshToken> resultado = refreshTokenService.findByToken(tokenEsperado.getToken());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getToken()).isEqualTo(tokenEsperado.getToken());
        verify(refreshTokenRepository).findByToken(tokenEsperado.getToken());
    }
}