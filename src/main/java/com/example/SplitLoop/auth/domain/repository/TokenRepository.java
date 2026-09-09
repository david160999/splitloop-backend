package com.example.SplitLoop.auth.domain.repository;

import com.example.SplitLoop.auth.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByToken(String jwtToken);

    List<Token> findByUserIdAndTokenTypeAndRevokedFalse(UUID id, Token.TokenType tokenType);
}
