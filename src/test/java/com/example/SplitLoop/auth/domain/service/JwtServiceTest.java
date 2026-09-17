package com.example.SplitLoop.auth.domain.service;

import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.util.mother.UserMother;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User usuarioPrueba;

    private final String testSecretKey = "dGhpc0lzQVN1cGVyU2VjcmV0S2V5Rm9ySldUVGVzdGluZ1B1cnBvc2VzMTIzNDU2Nzg5MA";
    private final long testExpiration = 3600000; // 1 hora

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", testSecretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", testExpiration);
        ReflectionTestUtils.invokeMethod(jwtService, "initSigningKey");

        usuarioPrueba = UserMother.user();
    }

    @Test
    @DisplayName("Debe generar un token válido y extraer el username")
    void debeGenerarYExtraerUsername() {
        String token = jwtService.generateAccessToken(usuarioPrueba);

        assertThat(token).isNotNull().isNotBlank();

        String extractedUsername = jwtService.extractUsername(token);
        assertThat(extractedUsername).isEqualTo(usuarioPrueba.getUsername());
    }

    @Test
    @DisplayName("Debe incluir el UUID y claims extra correctamente")
    void debeGenerarTokenConExtraClaimsYUuid() {
        UUID userId = usuarioPrueba.getId();
        Map<String, Object> claims = Map.of("userId", userId.toString(), "role", "ADMIN");

        String token = jwtService.generateAccessToken(claims, usuarioPrueba);

        String extractedRole = jwtService.extractClaim(token, c -> c.get("role", String.class));
        String extractedUserId = jwtService.extractClaim(token, c -> c.get("userId", String.class));

        assertThat(extractedRole).isEqualTo("ADMIN");
        assertThat(UUID.fromString(extractedUserId)).isEqualTo(userId);
    }

    @Test
    @DisplayName("Debe validar que el token pertenece al usuario correcto")
    void debeValidarTokenCorrecto() {
        String token = jwtService.generateAccessToken(usuarioPrueba);

        boolean isValid = jwtService.isTokenValid(token, usuarioPrueba);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Debe rechazar la validación si el usuario difiere")
    void debeInvalidarTokenSiUsuarioDifiere() {
        String token = jwtService.generateAccessToken(usuarioPrueba);

        User otroUsuario = new User();
        otroUsuario.setId(UUID.randomUUID());
        otroUsuario.setEmail("otro@email.com");

        boolean isValid = jwtService.isTokenValid(token, otroUsuario);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Debe lanzar ExpiredJwtException si el token está expirado")
    void debeLanzarExcepcionSiTokenEstaExpirado() {
        // Expiración en el pasado
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);

        String tokenExpirado = jwtService.generateAccessToken(usuarioPrueba);

        assertThatThrownBy(() -> jwtService.extractUsername(tokenExpirado))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("Debe lanzar SignatureException si la firma del token fue alterada")
    void debeLanzarExcepcionSiFirmaEsInvalida() {
        String tokenConFirmaFalsa = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3VhcmlvIn0.firmaIncorrecta";

        assertThatThrownBy(() -> jwtService.extractUsername(tokenConFirmaFalsa))
                .isInstanceOf(io.jsonwebtoken.security.SignatureException.class);
    }
}