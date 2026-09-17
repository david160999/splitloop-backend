package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.auth.controller.request.LoginRequest;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.response.AuthResponse;
import com.example.SplitLoop.auth.domain.entity.RefreshToken;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.auth.exception.InvalidBearerTokenException;
import com.example.SplitLoop.auth.exception.RefreshTokenRequiredException;
import com.example.SplitLoop.group.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    // --- PRUEBAS DE REGISTER ---

    @Test
    @DisplayName("Register: Debe registrar al usuario y devolver AuthResponse cuando el email no existe")
    void debeRegistrarUsuarioExitosamente() {
        RegisterRequest request = new RegisterRequest("john", "john@test.com", "password123");
        User userGuardado = UserMother.user();
        RefreshToken refreshToken = RefreshTokenMother.refreshTokenForUser(userGuardado);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(userGuardado);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("access-token-jwt");
        when(refreshTokenService.createRefreshToken(any(User.class))).thenReturn(refreshToken);

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("access-token-jwt");
        assertThat(response.refreshToken()).isEqualTo(refreshToken.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Register: Debe lanzar EmailAlreadyExistsException si el email ya está registrado")
    void debeLanzarExcepcionSiEmailYaExiste() {
        RegisterRequest request = new RegisterRequest("john", "john@test.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    // --- PRUEBAS DE LOGIN ---

    @Test
    @DisplayName("Login: Debe autenticar y devolver AuthResponse")
    void debeAutenticarYDevolverTokens() {
        LoginRequest request = new LoginRequest("john@test.com", "password123");
        User usuario = UserMother.user();
        RefreshToken refreshToken = RefreshTokenMother.refreshTokenForUser(usuario);
        Authentication authMock = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(usuario);
        when(jwtService.generateAccessToken(usuario)).thenReturn("access-token-jwt");
        when(refreshTokenService.createRefreshToken(usuario)).thenReturn(refreshToken);

        AuthResponse response = authService.login(request);

        assertThat(response.accessToken()).isEqualTo("access-token-jwt");
        assertThat(response.refreshToken()).isEqualTo(refreshToken.getToken());
    }

    @Test
    @DisplayName("Login: Debe propagar BadCredentialsException si las credenciales fallan")
    void debeLanzarExcepcionSiCredencialesSonInvalidas() {
        LoginRequest request = new LoginRequest("john@test.com", "wrong_pass");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    // --- PRUEBAS DE REFRESH TOKEN ---

    @Test
    @DisplayName("RefreshToken: Debe generar un nuevo AccessToken con un RefreshToken válido")
    void debeRefrescarTokenExitosamente() {
        User usuario = UserMother.user();
        RefreshToken refreshToken = RefreshTokenMother.refreshTokenForUser(usuario);

        when(refreshTokenService.findByToken(refreshToken.getToken())).thenReturn(Optional.of(refreshToken));
        when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
        when(jwtService.generateAccessToken(usuario)).thenReturn("new-access-token-jwt");

        AuthResponse response = authService.refreshToken(refreshToken.getToken());

        assertThat(response.accessToken()).isEqualTo("new-access-token-jwt");
        assertThat(response.refreshToken()).isEqualTo(refreshToken.getToken());
    }

    @Test
    @DisplayName("RefreshToken: Debe lanzar RefreshTokenRequiredException si el token es nulo o vacío")
    void debeLanzarExcepcionSiRefreshTokenEsNuloOVacio() {
        assertThatThrownBy(() -> authService.refreshToken(""))
                .isInstanceOf(RefreshTokenRequiredException.class)
                .hasMessageContaining("El refresh token es requerido");
    }

    @Test
    @DisplayName("RefreshToken: Debe lanzar InvalidBearerTokenException si no existe en BD")
    void debeLanzarExcepcionSiRefreshTokenNoExiste() {
        when(refreshTokenService.findByToken("invalid-token")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refreshToken("invalid-token"))
                .isInstanceOf(InvalidBearerTokenException.class)
                .hasMessageContaining("El token de acceso es inválido");
    }

    // --- PRUEBAS DE LOGOUT ---

    @Test
    @DisplayName("Logout: Debe eliminar los refresh tokens si la cabecera Bearer es válida")
    void debeEliminarTokensEnLogoutExitoso() {
        User usuario = UserMother.user();
        String header = "Bearer token-valido";

        when(jwtService.extractUsername("token-valido")).thenReturn(usuario.getEmail());
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        authService.logoutWithBearerToken(header);

        verify(refreshTokenRepository).deleteByUser(usuario);
    }

    @Test
    @DisplayName("Logout: No debe tocar la base de datos si el token de la cabecera expiró o es inválido")
    void noDebeLanzarExcepcionEnLogoutSiTokenExpiro() {
        String header = "Bearer token-expirado";
        when(jwtService.extractUsername("token-expirado")).thenThrow(new RuntimeException("Token expirado"));

        authService.logoutWithBearerToken(header);

        verify(userRepository, never()).findByEmail(any());
        verify(refreshTokenRepository, never()).deleteByUser(any());
    }

    @Test
    @DisplayName("Logout: Debe ignorar la operación si la cabecera no tiene formato Bearer")
    void debeIgnorarLogoutSiHeaderNoEsBearer() {
        authService.logoutWithBearerToken("Basic 123456");

        verifyNoInteractions(jwtService, userRepository, refreshTokenRepository);
    }
}