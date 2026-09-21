package com.example.SplitLoop.util.security;

import com.example.SplitLoop.user.domain.entity.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityTestUtils {

    private SecurityTestUtils() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Simula un inicio de sesión inyectando la entidad User directamente
     * en el contexto de seguridad de Spring.
     */
    public static void login(UserEntity userEntity) {
        // Creamos la autenticación usando el objeto User como Principal,
        // sus credenciales y su lista real de autoridades (roles).
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userEntity,                  // Principal (el usuario autenticado)
                userEntity.getPassword(),    // Credentials
                userEntity.getAuthorities()  // Authorities (roles)
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
