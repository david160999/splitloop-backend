package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.port.TokenProviderPort;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.persistence.jpa.SpringDataRefreshTokenRepository;
import com.example.SplitLoop.auth.domain.exception.TokenExpiredException;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
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
    private TokenProviderPort tokenProviderPort;
    @Mock
    private RefreshTokenRepository repository;

    private RefreshTokenService refreshTokenService;

    private final long testRefreshExpiration = 604800000L; // 7 días en ms

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService(
                repository,
                tokenProviderPort,
                testRefreshExpiration
        );
    }

    @Test
    @DisplayName("Debe eliminar tokens anteriores y crear un nuevo RefreshToken válido")
    void debeCrearRefreshToken() {
        // 1. GIVEN
        User user = UserMother.userModel();
        String tokenSimulado = "jwt-o-uuid-de-prueba-12345";

        // Stubbing del puerto para devolver un valor simulado
        when(tokenProviderPort.generateToken()).thenReturn(tokenSimulado);

        // Stubbing del repositorio para devolver exactamente lo que recibe
        when(repository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // 2. WHEN
        RefreshToken tokenCreado = refreshTokenService.createRefreshToken(user);

        // 3. THEN / VERIFICATIONS
        // Verificamos interacciones con las dependencias
        verify(repository).deleteByUserEmail(user.email());
        verify(tokenProviderPort).generateToken();

        // Capturamos el objeto para validar sus atributos de forma precisa
        ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(repository).save(tokenCaptor.capture());
        RefreshToken tokenGuardado = tokenCaptor.getValue();

        assertThat(tokenCreado).isNotNull();
        assertThat(tokenGuardado.user()).isEqualTo(user);
        assertThat(tokenGuardado.token()).isEqualTo(tokenSimulado); // Comprobamos que usó el token generado
        assertThat(tokenGuardado.revoked()).isFalse();            // Es buena práctica validar también los flags
        assertThat(tokenGuardado.expiryDate()).isAfter(Instant.now());
    }

    @Test
    @DisplayName("Debe retornar el token si no ha expirado")
    void debeValidarTokenNoExpirado() {
        RefreshToken tokenValido = RefreshTokenMother.refreshTokenModel();

        RefreshToken resultado = refreshTokenService.verifyExpiration(tokenValido);

        assertThat(resultado).isEqualTo(tokenValido);
        verify(repository, never()).delete(any());
    }

    @Test
    @DisplayName("Debe eliminar el token y lanzar TokenExpiredException si está expirado")
    void debeEliminarYLanzarExcepcionSiTokenExpiro() {
        RefreshToken tokenExpirado = RefreshTokenMother.expiredRefreshTokenModel();

        assertThatThrownBy(() -> refreshTokenService.verifyExpiration(tokenExpirado))
                .isInstanceOf(TokenExpiredException.class);

        verify(repository).delete(tokenExpirado);
    }

    @Test
    @DisplayName("Debe buscar un RefreshToken por su cadena de texto")
    void debeBuscarPorToken() {
        RefreshToken tokenEsperado = RefreshTokenMother.refreshTokenModel();

        when(repository.findByToken(tokenEsperado.token()))
                .thenReturn(Optional.of(tokenEsperado));

        Optional<RefreshToken> resultado = refreshTokenService.findByToken(tokenEsperado.token());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().token()).isEqualTo(tokenEsperado.token());
        verify(repository).findByToken(tokenEsperado.token());
    }
}