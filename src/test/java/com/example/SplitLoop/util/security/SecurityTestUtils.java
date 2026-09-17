package com.example.SplitLoop.util.security;

import com.example.SplitLoop.user.domain.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public final class SecurityTestUtils {

    private SecurityTestUtils() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Simula un inicio de sesión inyectando la entidad User directamente
     * en el contexto de seguridad de Spring.
     */
    public static void login(User user) {
        // Creamos la autenticación usando el objeto User como Principal,
        // sus credenciales y su lista real de autoridades (roles).
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,                  // Principal (el usuario autenticado)
                user.getPassword(),    // Credentials
                user.getAuthorities()  // Authorities (roles)
        );

        // Inyectamos el token de autenticación en el contexto del hilo actual
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Limpia el contexto de seguridad al finalizar cada test.
     */
    public static void logout() {
        SecurityContextHolder.clearContext();
    }
}
