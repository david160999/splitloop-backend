package com.example.SplitLoop.auth.infrastructure.persistence.mapper;

import com.example.SplitLoop.auth.domain.model.RefreshToken;
import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import com.example.SplitLoop.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { UserPersistenceMapper.class })
public interface RefreshTokenMapper {

    RefreshToken toDomain(RefreshTokenEntity entity);

    RefreshTokenEntity toEntity(RefreshToken domain);
}
