package com.example.SplitLoop.user.domain.repository;

import com.example.SplitLoop.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query("""
    SELECT u
    FROM User u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<User> searchByUsername(String query);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
}
