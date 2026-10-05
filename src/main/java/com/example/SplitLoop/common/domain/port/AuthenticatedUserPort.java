package com.example.SplitLoop.common.domain.port;

import java.util.UUID;

public interface AuthenticatedUserPort {
    /**
     * Obtiene el ID del usuario autenticado actualmente en la sesión/contexto.
     */
    UUID getCurrentUserId();
}
