package com.example.SplitLoop.group.domain.model;

import com.example.SplitLoop.group.domain.exception.InsufficientPermissionsException;
import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record GroupMember(
        UUID id,
        Group group,
        User user,
        MemberRole memberRole,
        LocalDateTime joinedAt
) {
    // Constructor compacto para asegurarnos de que siempre tenga rol y miembros válidos
    public GroupMember {
        Objects.requireNonNull(group, "El grupo no puede ser nulo");
        Objects.requireNonNull(user, "El usuario no puede ser nulo");
        Objects.requireNonNull(memberRole, "El rol de miembro es obligatorio");
    }

    public boolean isAdmin() {
        return MemberRole.ADMIN.equals(this.memberRole);
    }

    public boolean isMember(UUID userId) {
        return user != null && user.id().equals(userId);
    }

    public void ensureIsAdmin() {
        if (!isAdmin()) {
            throw new InsufficientPermissionsException();
        }
    }
}
