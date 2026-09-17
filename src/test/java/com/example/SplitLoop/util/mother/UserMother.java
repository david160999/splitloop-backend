package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.user.domain.entity.Role;
import com.example.SplitLoop.user.domain.entity.User;

import java.util.UUID;

public final class UserMother {

    private UserMother() {
    }

    public static User user() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("john")
                .email("john@test.com")
                .password("password")
                .build();
    }

    public static User anotherUser() {
        return user().toBuilder()
                .id(UUID.randomUUID())
                .username("mary")
                .email("mary@test.com")
                .build();
    }

    public static User adminUser() {
        return user().toBuilder()
                .role(Role.ADMIN)
                .build();
    }

    public static User withCredentials(String email, String encodedPassword) {
        return User.builder()
                .username("testUser_" + System.currentTimeMillis())
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();
    }
}