package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.persistence.jpa.SpringDataRefreshTokenRepository;
import com.example.SplitLoop.auth.domain.exception.TokenExpiredException;
import com.example.SplitLoop.user.domain.entity.UserEntity;
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
class RefreshTokenEntityServiceTest {

    @Mock
    private SpringDataRefreshTokenRepository springDataRefreshTokenRepository;

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
        UserEntity usuario = UserMother.userEntity();

        when(springDataRefreshTokenRepository.save(any(RefreshTokenEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshTokenEntity tokenCreado = refreshTokenService.createRefreshToken(usuario);

        verify(springDataRefreshTokenRepository).deleteByUser(usuario);

        ArgumentCaptor<RefreshTokenEntity> tokenCaptor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        verify(springDataRefreshTokenRepository).save(tokenCaptor.capture());
        RefreshTokenEntity tokenGuardado = tokenCaptor.getValue();

        assertThat(tokenCreado).isNotNull();
        assertThat(tokenGuardado.getUser()).isEqualTo(usuario);
        assertThat(tokenGuardado.getToken()).isNotNull();
        assertThat(UUID.fromString(tokenGuardado.getToken())).isNotNull();
        assertThat(tokenGuardado.getExpiryDate()).isAfter(Instant.now());
    }

    @Test
    @DisplayName("Debe retornar el token si no ha expirado")
    void debeValidarTokenNoExpirado() {
        RefreshTokenEntity tokenValido = RefreshTokenMother.refreshTokenEntity();

        RefreshTokenEntity resultado = refreshTokenService.verifyExpiration(tokenValido);

        assertThat(resultado).isEqualTo(tokenValido);
        verify(springDataRefreshTokenRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Debe eliminar el token y lanzar TokenExpiredException si está expirado")
    void debeEliminarYLanzarExcepcionSiTokenExpiro() {
        RefreshTokenEntity tokenExpirado = RefreshTokenMother.expiredRefreshTokenEntity();

        assertThatThrownBy(() -> refreshTokenService.verifyExpiration(tokenExpirado))
                .isInstanceOf(TokenExpiredException.class);

        verify(springDataRefreshTokenRepository).delete(tokenExpirado);
    }

    @Test
    @DisplayName("Debe buscar un RefreshToken por su cadena de texto")
    void debeBuscarPorToken() {
        RefreshTokenEntity tokenEsperado = RefreshTokenMother.refreshTokenEntity();

        when(springDataRefreshTokenRepository.findByToken(tokenEsperado.getToken()))
                .thenReturn(Optional.of(tokenEsperado));

        Optional<RefreshTokenEntity> resultado = refreshTokenService.findByToken(tokenEsperado.getToken());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getToken()).isEqualTo(tokenEsperado.getToken());
        verify(springDataRefreshTokenRepository).findByToken(tokenEsperado.getToken());
    }
}