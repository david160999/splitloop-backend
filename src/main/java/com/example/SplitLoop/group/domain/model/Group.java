package com.example.SplitLoop.group.domain.model;

import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record Group(
        UUID id,
        String name,
        String description,
        User createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // Constructor compacto opcional para validar reglas de invariant del dominio
    public Group {
        Objects.requireNonNull(name, "El nombre del grupo no puede ser nulo");
    }

    public boolean isCreatedBy(UUID userId) {
        return createdBy != null && createdBy.id().equals(userId);
    }

}
