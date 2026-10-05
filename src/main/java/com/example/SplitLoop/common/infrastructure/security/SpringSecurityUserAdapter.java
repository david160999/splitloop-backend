package com.example.SplitLoop.common.infrastructure.security;

import com.example.SplitLoop.common.domain.port.AuthenticatedUserPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpringSecurityUserAdapter implements AuthenticatedUserPort {

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof CustomUserDetails principal)) {
            throw new IllegalStateException("No hay ningún usuario autenticado en el contexto de seguridad.");
        }

        return principal.getId();
    }
}
