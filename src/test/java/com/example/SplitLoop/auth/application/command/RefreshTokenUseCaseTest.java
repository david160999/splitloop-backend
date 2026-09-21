package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.service.JwtService;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.auth.domain.exception.InvalidRefreshTokenException;
import com.example.SplitLoop.auth.application.exception.RefreshTokenRequiredException;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.util.mother.RefreshTokenMother;
import com.example.SplitLoop.util.mother.UserMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenUseCaseTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private RefreshTokenUseCase refreshTokenUseCase;

    @Test
    @DisplayName("Debe generar un nuevo AccessToken y un nuevo RefreshToken (Rotación)")
    void debeRefrescarTokenExitosamente() {
        UserEntity usuario = UserMother.userEntity();
        RefreshTokenEntity oldRefreshToken = RefreshTokenMother.refreshTokenEntityForUser(usuario);
        RefreshTokenEntity newRefreshToken = RefreshTokenMother.refreshTokenEntityForUser(usuario);
        newRefreshToken.setToken("nuevo-refresh-token-uuid");

        when(refreshTokenService.findByToken(oldRefreshToken.getToken())).thenReturn(Optional.of(oldRefreshToken));
        when(refreshTokenService.verifyExpiration(oldRefreshToken)).thenReturn(oldRefreshToken);
        when(jwtService.generateAccessToken(usuario)).thenReturn("new-access-token-jwt");
        when(refreshTokenService.createRefreshToken(usuario)).thenReturn(newRefreshToken);

        AuthResponse response = refreshTokenUseCase.execute(oldRefreshToken.getToken());

        assertThat(response.accessToken()).isEqualTo("new-access-token-jwt");
        assertThat(response.refreshToken()).isEqualTo("nuevo-refresh-token-uuid");

        verify(refreshTokenService).findByToken(oldRefreshToken.getToken());
        verify(refreshTokenService).verifyExpiration(oldRefreshToken);
        verify(refreshTokenService).createRefreshToken(usuario);
    }

    @Test
    @DisplayName("Debe lanzar RefreshTokenRequiredException si el token es nulo o vacío")
    void debeLanzarExcepcionSiRefreshTokenEsNuloOVacio() {
        assertThatThrownBy(() -> refreshTokenUseCase.execute(""))
                .isInstanceOf(RefreshTokenRequiredException.class);
    }

    @Test
    @DisplayName("Debe lanzar InvalidRefreshTokenException si no existe en BD")
    void debeLanzarExcepcionSiRefreshTokenNoExiste() {
        when(refreshTokenService.findByToken("invalid-token")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenUseCase.execute("invalid-token"))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }
}