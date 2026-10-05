package com.example.SplitLoop.user.domain.repository;

import com.example.SplitLoop.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> searchByUsername(String query);
    boolean existsByEmail(String email);
}