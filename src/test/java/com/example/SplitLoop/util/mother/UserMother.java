package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.user.domain.entity.Role;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.model.User;

import java.util.UUID;

public final class UserMother {

    private UserMother() {
    }

    // =========================================================================
    // 1. MODELOS DE DOMINIO (Para UseCases, Domain Services y Tests Unitarios)
    // =========================================================================

    public static User userModel() {
        return User.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")) // UUID fijo facilita aserciones
                .username("john")
                .email("john@test.com")
                .password("encoded_password")
                .role(Role.USER)
                .build();
    }

    public static User anotherUserModel() {
        return userModel().toBuilder()
                .id(UUID.fromString("987e6543-e21b-12d3-a456-426614174000"))
                .username("mary")
                .email("mary@test.com")
                .build();
    }

    public static User adminUserModel() {
        return userModel().toBuilder()
                .role(Role.ADMIN)
                .build();
    }

    public static User withCredentialsModel(String email, String encodedPassword) {
        return User.builder()
                .id(UUID.randomUUID())
                .username("testUser_" + System.currentTimeMillis())
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();
    }

    // =========================================================================
    // 2. ENTIDADES JPA DE INFRAESTRUCTURA (Para RepositoryImpl, Mappers y Integration Tests)
    // =========================================================================

    public static UserEntity userEntity() {
        User domain = userModel();
        return UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .role(domain.getRole())
                .build();
    }

    public static UserEntity anotherUserEntity() {
        User domain = anotherUserModel();
        return UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .role(domain.getRole())
                .build();
    }

    public static UserEntity withCredentialsEntity(String email, String encodedPassword) {
        User domain = userModel();
        return UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(email)
                .password(encodedPassword)
                .role(domain.getRole())
                .build();
    }
}