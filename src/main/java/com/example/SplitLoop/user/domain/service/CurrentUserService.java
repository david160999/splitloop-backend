package com.example.SplitLoop.user.domain.service;

import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    /**
     * Obtiene el ID del usuario autenticado directamente desde el contexto de memoria (sin ir a BD).
     */
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No hay ningún usuario autenticado en el contexto de seguridad.");
        }

        // Hacemos el cast directo a tu entidad User 👤
        User principal = (User) authentication.getPrincipal();


        assert principal != null;
        return principal.getId();
    }

    /**
     * Obtiene la entidad completa del usuario directamente de la Base de Datos.
     * Útil si necesitas actualizar sus datos o validar relaciones mapeadas.
     */
    public User getCurrentUser() {
        UUID userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
