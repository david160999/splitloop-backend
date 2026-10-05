package com.example.SplitLoop.group.domain.port;

import com.example.SplitLoop.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface GroupUserPort {
    Optional<User> findById(UUID userId);
    boolean existsById(UUID userId);
}
