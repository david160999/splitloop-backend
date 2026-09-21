package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.request.LoginRequest;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.service.JwtService;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Test
    @DisplayName("Debe autenticar y devolver AuthResponse")
    void debeAutenticarYDevolverTokens() {
        LoginRequest request = new LoginRequest("john@test.com", "password123");
        UserEntity usuario = UserMother.userEntity();
        RefreshTokenEntity refreshTokenModel = RefreshTokenMother.refreshTokenEntityForUser(usuario);
        Authentication authMock = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(usuario);
        when(jwtService.generateAccessToken(usuario)).thenReturn("access-token-jwt");
        when(refreshTokenService.createRefreshToken(usuario)).thenReturn(refreshTokenModel);

        AuthResponse response = loginUseCase.execute(request);

        assertThat(response.accessToken()).isEqualTo("access-token-jwt");
        assertThat(response.refreshToken()).isEqualTo(refreshTokenModel.getToken());
    }

    @Test
    @DisplayName("Debe propagar BadCredentialsException si las credenciales fallan")
    void debeLanzarExcepcionSiCredencialesSonInvalidas() {
        LoginRequest request = new LoginRequest("john@test.com", "wrong_pass");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> loginUseCase.execute(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}