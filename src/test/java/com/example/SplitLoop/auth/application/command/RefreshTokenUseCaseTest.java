package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.application.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.auth.domain.exception.InvalidRefreshTokenException;
import com.example.SplitLoop.auth.application.exception.RefreshTokenRequiredException;
import com.example.SplitLoop.auth.infrastructure.security.JwtTokenAdapter;
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
    private JwtTokenAdapter jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private RefreshTokenUseCase refreshTokenUseCase;

    @Test
    @DisplayName("Debe generar un nuevo AccessToken y un nuevo RefreshToken (Rotación)")
    void debeRefrescarTokenExitosamente() {
        User usuario = UserMother.userModel();
        RefreshToken oldRefreshToken = RefreshTokenMother.refreshTokenModelForUser(usuario);
        RefreshToken newRefreshToken = oldRefreshToken.toBuilder().token("nuevo-refresh-token-uuid").build();

        when(refreshTokenService.findByToken(oldRefreshToken.token())).thenReturn(Optional.of(oldRefreshToken));
        when(refreshTokenService.verifyExpiration(oldRefreshToken)).thenReturn(oldRefreshToken);
        when(jwtService.generateAccessToken(usuario)).thenReturn("new-access-token-jwt");
        when(refreshTokenService.createRefreshToken(usuario)).thenReturn(newRefreshToken);

        AuthResponse response = refreshTokenUseCase.execute(oldRefreshToken.token());

        assertThat(response.accessToken()).isEqualTo("new-access-token-jwt");
        assertThat(response.refreshToken()).isEqualTo("nuevo-refresh-token-uuid");

        verify(refreshTokenService).findByToken(oldRefreshToken.token());
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