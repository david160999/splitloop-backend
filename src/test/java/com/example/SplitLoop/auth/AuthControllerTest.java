package com.example.SplitLoop.auth;

import com.example.SplitLoop.auth.application.command.LoginUseCase;
import com.example.SplitLoop.auth.application.command.RefreshTokenUseCase;
import com.example.SplitLoop.auth.application.command.RegisterUserUseCase;
import com.example.SplitLoop.auth.controller.AuthController;
import com.example.SplitLoop.auth.controller.request.RegisterRequest;
import com.example.SplitLoop.auth.controller.response.TokenResponse;
import com.example.SplitLoop.common.security.config.SecurityConfigTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
@Import(SecurityConfigTest.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private RefreshTokenUseCase refreshTokenUseCase;

    @Test
    void shouldFailWhenEmailIsInvalid() throws Exception {

        String body = """
        {
          "username": "John",
          "email": "invalid-email",
          "password": "password123"
        }
        """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.fields.email").value("Email must be valid"));
    }

    @Test
    void shouldFailWhenUsernameIsBlank() throws Exception {

        String body = """
        {
          "username": "",
          "email": "john@test.com",
          "password": "password123"
        }
        """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.fields.username").value("Username is required"));
    }

    @Test
    void shouldRegisterUser() throws Exception {

        TokenResponse response = new TokenResponse(
                "access-token",
                "refresh-token"
        );

        when(registerUserUseCase.execute(any(RegisterRequest.class)))
                .thenReturn(response);

        String body = """
        {
          "username": "John",
          "email": "john@test.com",
          "password": "password123"
        }
        """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access-token"))
                .andExpect(jsonPath("$.refresh_token").value("refresh-token"));

        verify(registerUserUseCase).execute(any(RegisterRequest.class));
    }
}