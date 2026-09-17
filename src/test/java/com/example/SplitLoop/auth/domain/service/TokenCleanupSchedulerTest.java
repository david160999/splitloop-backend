package com.example.SplitLoop.auth.domain.service;

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
class TokenCleanupSchedulerTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private TokenCleanupScheduler tokenCleanupScheduler;

    @Test
    @DisplayName("Debe ejecutar la purga de tokens expirados llamando al repositorio con un Instant actual")
    void debePurgarTokensExpirados() {
        int tokensEliminadosMock = 5;
        when(refreshTokenRepository.deleteByExpiryDateBefore(any(Instant.class)))
                .thenReturn(tokensEliminadosMock);

        Instant antesDeEjecutar = Instant.now();

        tokenCleanupScheduler.purgeExpiredTokens();

        Instant despuesDeEjecutar = Instant.now();

        // Capturamos el Instant exacto pasado al método deleteByExpiryDateBefore
        ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(refreshTokenRepository).deleteByExpiryDateBefore(instantCaptor.capture());

        Instant instantCapturado = instantCaptor.getValue();

        // Verificamos que el Instant enviado al repositorio esté dentro del rango de ejecución
        assertThat(instantCapturado).isBetween(antesDeEjecutar, despuesDeEjecutar);
    }
}