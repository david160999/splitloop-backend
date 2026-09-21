package com.example.SplitLoop.user.domain.model;

import com.example.SplitLoop.user.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String email;
    private String username;
    private String password;
    private Role role;

    // Métodos con reglas de negocio del usuario (opcional)
    public boolean isAdmin() {
        return Role.ADMIN.equals(this.role);
    }
}
