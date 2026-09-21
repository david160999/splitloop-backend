package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.application.command.PurgeExpiredTokensUseCase;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurgeExpiredTokensUseCaseTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository; // El puerto o repositorio que usa el UseCase

    @InjectMocks
    private PurgeExpiredTokensUseCase purgeExpiredTokensUseCase;

    @Test
    @DisplayName("Debe ejecutar la purga de tokens expirados llamando al repositorio con un Instant actual")
    void debePurgarTokensExpirados() {
        // Arrange
        int tokensEliminadosMock = 5;
        when(refreshTokenRepository.deleteByExpiryDateBefore(any(Instant.class)))
                .thenReturn(tokensEliminadosMock);

        Instant antesDeEjecutar = Instant.now();

        // Act
        int result = purgeExpiredTokensUseCase.execute();

        Instant despuesDeEjecutar = Instant.now();

        // Assert
        assertThat(result).isEqualTo(tokensEliminadosMock);

        ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(refreshTokenRepository).deleteByExpiryDateBefore(instantCaptor.capture());

        Instant instantCapturado = instantCaptor.getValue();
        assertThat(instantCapturado).isBetween(antesDeEjecutar, despuesDeEjecutar);
    }
}