package com.example.SplitLoop.user.domain.repository;

import com.example.SplitLoop.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    @Query("""
    SELECT u
    FROM UserEntity u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<UserEntity> searchByUsername(@Param("query") String query);

    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);
}
