package com.example.SplitLoop.auth.domain.repository;

import com.example.SplitLoop.auth.domain.entity.RefreshToken;
import com.example.SplitLoop.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String jwtToken);
    void deleteByUser(User user);
//    List<RefreshToken> findByUserIdAndTokenTypeAndRevokedFalse(UUID id);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken t WHERE t.expiryDate < :now")
    int deleteByExpiryDateBefore(Instant now);
}
