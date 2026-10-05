package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.user.infrastructure.persistence.entity.Role;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
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
                .id(domain.id())
                .username(domain.username())
                .email(domain.email())
                .password(domain.password())
                .role(domain.role())
                .build();
    }

    public static UserEntity anotherUserEntity() {
        User domain = anotherUserModel();
        return UserEntity.builder()
                .id(domain.id())
                .username(domain.username())
                .email(domain.email())
                .password(domain.password())
                .role(domain.role())
                .build();
    }

    public static UserEntity withCredentialsEntity(String email, String encodedPassword) {
        User domain = userModel();
        return UserEntity.builder()
                .id(domain.id())
                .username(domain.username())
                .email(email)
                .password(encodedPassword)
                .role(domain.role())
                .build();
    }
}