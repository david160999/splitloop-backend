package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.request.RegisterRequest;
import com.example.SplitLoop.auth.infrastructure.presentation.rest.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.service.JwtService;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.group.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import com.example.SplitLoop.util.mother.RefreshTokenMother;
import com.example.SplitLoop.util.mother.UserMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    @DisplayName("Debe registrar al usuario y devolver AuthResponse cuando el email no existe")
    void debeRegistrarUsuarioExitosamente() {
        RegisterRequest request = new RegisterRequest("john", "john@test.com", "password123");
        UserEntity userGuardado = UserMother.userEntity();
        RefreshTokenEntity refreshTokenModel = RefreshTokenMother.refreshTokenEntityForUser(userGuardado);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded_password");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userGuardado);
        when(jwtService.generateAccessToken(any(UserEntity.class))).thenReturn("access-token-jwt");
        when(refreshTokenService.createRefreshToken(any(UserEntity.class))).thenReturn(refreshTokenModel);

        AuthResponse response = registerUserUseCase.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token-jwt");
        assertThat(response.refreshToken()).isEqualTo(refreshTokenModel.getToken());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Debe lanzar EmailAlreadyExistsException si el email ya está registrado")
    void debeLanzarExcepcionSiEmailYaExiste() {
        RegisterRequest request = new RegisterRequest("john", "john@test.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> registerUserUseCase.execute(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }
}