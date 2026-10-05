package com.example.SplitLoop.auth.domain.port;

import com.example.SplitLoop.user.domain.model.User;

import java.util.Map;

public interface TokenProviderPort {

    String generateAccessToken(User user);

    String generateAccessToken(Map<String, Object> extraClaims, User user);

    String extractUsername(String token);

    boolean isTokenValid(String token, User user);

    String generateToken();

}