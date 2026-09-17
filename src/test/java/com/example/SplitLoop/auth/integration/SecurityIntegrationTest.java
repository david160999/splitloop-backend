package com.example.SplitLoop.auth.integration;

import com.example.SplitLoop.util.integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class SecurityIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Security: Debe rechazar peticiones POST sin CSRF con 403 Forbidden")
    void debeRechazarPostSinCsrf() throws Exception {
        mockMvc.perform(post("/api/v1/protected-resource") // Ruta no ignorada en ignoringRequestMatchers
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Security: Debe aceptar peticiones POST cuando se incluye un token CSRF válido")
    void debeAceptarPostConCsrfValido() throws Exception {
        mockMvc.perform(post("/api/v1/protected-resource")
                        .with(csrf()) // Simula la inclusión del header/cookie CSRF correcto
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized()); // Pasa el filtro CSRF y llega a la regla de auth (401)
    }

    @Test
    @DisplayName("Security: Debe denegar el acceso a rutas protegidas sin JWT con 401 Unauthorized")
    void debeDenegarAccesoARutasProtegidasSinToken() throws Exception {
        mockMvc.perform(get("/api/v1/protected-resource"))
                .andExpect(status().isUnauthorized());
    }
}