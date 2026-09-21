package com.example.SplitLoop.auth.infrastructure.persistence.adapter;

import com.example.SplitLoop.auth.domain.repository.RefreshTokenRepository;
import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.auth.infrastructure.persistence.jpa.SpringDataRefreshTokenRepository;
import com.example.SplitLoop.auth.infrastructure.persistence.mapper.RefreshTokenMapper;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final SpringDataRefreshTokenRepository springDataRepository;
    private final RefreshTokenMapper mapper;

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return springDataRepository.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByUserEmail(UserEntity userEntity) {
        springDataRepository.deleteByUserEmail(userEntity.getEmail());
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
}
