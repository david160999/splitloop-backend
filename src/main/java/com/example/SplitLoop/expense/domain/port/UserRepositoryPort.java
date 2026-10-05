package com.example.SplitLoop.expense.domain.port;

import com.example.SplitLoop.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    Optional<User> findById(UUID id);
    boolean existsById(UUID id);
}