package com.example.SplitLoop.auth.infrastructure.security;

import com.example.SplitLoop.auth.domain.port.TokenProviderPort;
import com.example.SplitLoop.user.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtTokenAdapter implements TokenProviderPort {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey cachedSigningKey;

    @PostConstruct
    private void initSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.cachedSigningKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateAccessToken(User user) {
        return generateAccessToken(new HashMap<>(), user);
    }

    @Override
    public String generateAccessToken(Map<String, Object> extraClaims, User user) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.email()) // Usa el modelo de dominio en lugar de UserDetails
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(cachedSigningKey)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, User user) {
        final Claims claims = extractAllClaims(token);
        final String username = claims.getSubject();
        final Date expiration = claims.getExpiration();

        return (username.equals(user.email())) && !expiration.before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(cachedSigningKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
