package com.example.SplitLoop.auth.application.command;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.application.dto.request.RegisterRequest;
import com.example.SplitLoop.auth.application.dto.response.AuthResponse;
import com.example.SplitLoop.auth.domain.service.RefreshTokenService;
import com.example.SplitLoop.auth.infrastructure.security.JwtTokenAdapter;
import com.example.SplitLoop.group.domain.exception.EmailAlreadyExistsException;
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
    private JwtTokenAdapter jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    @DisplayName("Debe registrar al usuario y devolver AuthResponse cuando el email no existe")
    void debeRegistrarUsuarioExitosamente() {
        RegisterRequest request = new RegisterRequest("john", "john@test.com", "password123");
        User userGuardado = UserMother.userModel();
        RefreshToken refreshTokenModel = RefreshTokenMother.refreshTokenModelForUser(userGuardado);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(userGuardado);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("access-token-jwt");
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(refreshTokenModel);

        AuthResponse response = registerUserUseCase.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token-jwt");
        assertThat(response.refreshToken()).isEqualTo(refreshTokenModel.token());
        verify(userRepository).save(any(User.class));
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