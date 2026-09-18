package com.example.SplitLoop.auth.integration;

import com.example.SplitLoop.auth.controller.request.LoginRequest;
import com.example.SplitLoop.auth.controller.request.LoginRequestMother;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.request.RequestMother;
import com.example.SplitLoop.auth.domain.entity.RefreshToken;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import com.example.SplitLoop.util.integration.BaseIntegrationTest;
import com.example.SplitLoop.util.mother.RefreshTokenMother;
import com.example.SplitLoop.util.mother.UserMother;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class AuthControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("Register: Debe crear usuario y retornar tokens")
    void debeRegistrarUsuarioCorrectamente() throws Exception {
        RegisterRequest registerReq = RequestMother.registerRequest();

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        assertThat(userRepository.existsByEmail(registerReq.email())).isTrue();
    }

    @Test
    @DisplayName("Login: Debe autenticar y devolver Cookie de Refresh Token")
    void debeHacerLoginCorrectamente() throws Exception {
        // GIVEN: Creamos el objeto en memoria con el Mother y lo guardamos en la BD real
        LoginRequest loginReq = LoginRequestMother.valid();
        userRepository.save(UserMother.withCredentials(loginReq.email(), passwordEncoder.encode(loginReq.password())));

        // WHEN / THEN
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, Matchers.containsString("refreshToken=")))
                .andExpect(jsonPath("$.access_token").isNotEmpty());
    }

    @Test
    @DisplayName("Flujo Completo: Registro, Login y Refresh Token contra BD real")
    void flujoCompletoAutenticacion() throws Exception {
        // 1. REGISTRO DE USUARIO
        RegisterRequest registerReq = RequestMother.registerRequest();

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated());

        // Verificamos persistencia real en la BD
        assertThat(userRepository.existsByEmail(registerReq.email())).isTrue();

        // 2. LOGIN DE USUARIO
        LoginRequest loginReq = new LoginRequest(registerReq.email(), registerReq.password());

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, Matchers.containsString("refreshToken=")))
                .andExpect(jsonPath("$.access_token").isNotEmpty())
                .andReturn();

        // Extraemos la Cookie Set-Cookie generada por el login
        String setCookieHeader = loginResult.getResponse().getHeader(HttpHeaders.SET_COOKIE);
        String refreshTokenValue = extractCookieValue(setCookieHeader, "refreshToken");

        // Verificamos que el RefreshToken se guardó correctamente en BD
        assertThat(refreshTokenRepository.findByToken(refreshTokenValue)).isPresent();

        // 3. REFRESH TOKEN (vía Cookie)
        Cookie refreshCookie = new Cookie("refreshToken", refreshTokenValue);

        mockMvc.perform(post("/auth/refresh-token")
                        .with(csrf())
                        .cookie(refreshCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isNotEmpty());
    }

    private String extractCookieValue(String header, String cookieName) {
        for (String raw : header.split(";")) {
            String[] pair = raw.trim().split("=");
            if (pair[0].equals(cookieName)) {
                return pair[1];
            }
        }
        return null;
    }
}
