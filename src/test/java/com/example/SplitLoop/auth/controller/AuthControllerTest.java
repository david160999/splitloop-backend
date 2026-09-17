package com.example.SplitLoop.auth.controller;

import com.example.SplitLoop.auth.application.command.LoginUseCase;
import com.example.SplitLoop.auth.application.command.RefreshTokenUseCase;
import com.example.SplitLoop.auth.application.command.RegisterUserUseCase;
import com.example.SplitLoop.auth.controller.request.LoginRequest;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.request.RequestMother;
import com.example.SplitLoop.auth.controller.response.AuthResponse;
import com.example.SplitLoop.auth.domain.service.AuthService;
import com.example.SplitLoop.auth.domain.service.JwtService;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private RefreshTokenUseCase refreshTokenUseCase;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // --- REGISTER TESTS ---

    @Test
    @DisplayName("Register: Debe responder 200 OK y retornar AuthResponse al enviar datos válidos")
    void debeRegistrarUsuarioExitosamente() throws Exception {
        RegisterRequest request = RequestMother.registerRequest();
        AuthResponse expectedResponse = new AuthResponse("access-token", "refresh-token");

        when(registerUserUseCase.execute(any(RegisterRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access-token"))
                .andExpect(jsonPath("$.refresh_token").value("refresh-token"));

        verify(registerUserUseCase).execute(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("Register: Debe responder 400 Bad Request si los datos del body violan @Valid")
    void debeFallarRegisterPorValidacionConstraint() throws Exception {
        RegisterRequest invalidRequest = RequestMother.invalidRegisterRequest();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.fields.username").value("Username is required"))
                .andExpect(jsonPath("$.fields.email").value("Email must be valid"));

        verifyNoInteractions(registerUserUseCase);
    }

    // --- LOGIN TESTS ---

    @Test
    @DisplayName("Login: Debe responder 200 OK y establecer la Cookie HttpOnly correctamente")
    void debeAutenticarYSetearCookieHttpOnly() throws Exception {
        LoginRequest request = RequestMother.loginRequest();
        AuthResponse expectedResponse = new AuthResponse("access-token", "refresh-token-uuid");

        when(loginUseCase.execute(any(LoginRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access-token"))
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken=refresh-token-uuid")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Secure")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/auth/refresh-token")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")));

        verify(loginUseCase).execute(any(LoginRequest.class));
    }

    @Test
    @DisplayName("Login: Debe responder 400 Bad Request si el formato de login es inválido")
    void debeFallarLoginPorValidacionConstraint() throws Exception {
        LoginRequest invalidRequest = RequestMother.invalidLoginRequest();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(loginUseCase);
    }

    // --- REFRESH TOKEN TESTS ---

    @Test
    @DisplayName("RefreshToken: Debe renovar el token leyendo el valor desde una Cookie")
    void debeRefrescarTokenDesdeCookie() throws Exception {
        Cookie cookie = new Cookie("refreshToken", "token-de-cookie");
        AuthResponse expectedResponse = new AuthResponse("new-access-token", "token-de-cookie");

        when(refreshTokenUseCase.execute("token-de-cookie")).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/refresh-token")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("new-access-token"));

        verify(refreshTokenUseCase).execute("token-de-cookie");
    }

    @Test
    @DisplayName("RefreshToken: Debe renovar el token leyendo la cabecera Authorization Bearer si no hay Cookie")
    void debeRefrescarTokenDesdeHeaderBearer() throws Exception {
        String bearerHeader = "Bearer token-de-header";
        AuthResponse expectedResponse = new AuthResponse("new-access-token", "token-de-header");

        when(refreshTokenUseCase.execute("token-de-header")).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/refresh-token")
                        .header(HttpHeaders.AUTHORIZATION, bearerHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("new-access-token"));

        verify(refreshTokenUseCase).execute("token-de-header");
    }

    @Test
    @DisplayName("RefreshToken: Debe enviar null al UseCase si no recibe ni Cookie ni Header")
    void debeEjecutarConNullSiNoHayToken() throws Exception {
        AuthResponse expectedResponse = new AuthResponse("new-access-token", null);

        when(refreshTokenUseCase.execute(null)).thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/refresh-token"))
                .andExpect(status().isOk());

        verify(refreshTokenUseCase).execute(null);
    }


    @Test
    void loginDebeEstablecerCookieYDevolverAccessToken() throws Exception {
        when(loginUseCase.execute(Mockito.any()))
                .thenReturn(new AuthResponse("mocked-access-token", "mocked-refresh-token"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(jsonPath("$.access_token").value("mocked-access-token"))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken=mocked-refresh-token")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Secure")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Strict")));

    }
}