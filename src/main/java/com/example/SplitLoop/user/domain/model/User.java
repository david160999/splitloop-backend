package com.example.SplitLoop.user.domain.model;

import com.example.SplitLoop.user.domain.exception.NothingToUpdateException;
import com.example.SplitLoop.user.domain.exception.UsernameRequiredException;
import com.example.SplitLoop.user.infrastructure.persistence.entity.Role;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record User(
        UUID id,
        String email,
        String username,
        String password,
        Role role
) {
    public boolean isAdmin() {
        return Role.ADMIN.equals(this.role);
    }

    public void validateUpdate(String newUsername, String newEmail) {
        if (newUsername == null || newUsername.isBlank()) {
            throw new UsernameRequiredException();
        }

        if (newUsername.equals(this.username) && newEmail.equals(this.email)) {
            throw new NothingToUpdateException();
        }
    }
}
