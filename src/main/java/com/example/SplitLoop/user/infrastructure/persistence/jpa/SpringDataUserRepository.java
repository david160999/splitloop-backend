package com.example.SplitLoop.user.infrastructure.persistence.jpa;

import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByEmail(String email);

    @Query("""
    SELECT u
    FROM UserEntity u
    WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<UserEntity> searchByUsername(@Param("query") String query);
}
