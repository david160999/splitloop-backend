package com.example.SplitLoop.auth.infrastructure.persistence.jpa;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String jwtToken);

    void deleteByUser(UserEntity userEntity);

    void deleteByUserEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity t WHERE t.expiryDate < :now")
    int deleteByExpiryDateBefore(@Param("now") Instant now);}
