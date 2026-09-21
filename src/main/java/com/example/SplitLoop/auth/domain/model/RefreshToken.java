package com.example.SplitLoop.auth.domain.model;

import com.example.SplitLoop.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    private UUID id;
    private String token;
    private boolean revoked;
    private User user; // Entidad de Dominio User (no la entidad JPA)
    private LocalDateTime createdAt;
    private Instant expiryDate;

    // Regla de Negocio: Verificar si el token ha expirado
    public boolean hasExpired() {
        return expiryDate != null && expiryDate.isBefore(Instant.now());
    }

    // Regla de Negocio: Revocar manualmente el token
    public void revoke() {
        this.revoked = true;
    }
}
