package com.example.SplitLoop.auth.infrastructure.persistence.adapter;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.persistence.jpa.SpringDataRefreshTokenRepository;
import com.example.SplitLoop.auth.infrastructure.persistence.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final SpringDataRefreshTokenRepository springDataRepository;
    private final RefreshTokenMapper mapper;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return springDataRepository.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByUserEmail(String email) {
        springDataRepository.deleteByUserEmail(email);
    }

    @Override
    public void deleteByToken(String token) {
        springDataRepository.findByToken(token)
                .ifPresent(springDataRepository::delete);
    }

    @Override
    public int deleteByExpiryDateBefore(Instant now) {
        return springDataRepository.deleteByExpiryDateBefore(now);
    }

    @Override
    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        // 1. Mapea de Modelo de Dominio -> Entidad JPA
        RefreshTokenEntity entity = mapper.toEntity(refreshToken);

        // 2. Guarda en la base de datos con Spring Data
        RefreshTokenEntity savedEntity = springDataRepository.save(entity);

        // 3. Mapea de Entidad JPA -> Modelo de Dominio para devolverlo
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(RefreshToken token) {
        springDataRepository.deleteById(token.id());
    }
}
