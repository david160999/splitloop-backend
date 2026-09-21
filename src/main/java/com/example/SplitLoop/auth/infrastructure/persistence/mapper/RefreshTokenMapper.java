package com.example.SplitLoop.auth.infrastructure.persistence.mapper;

import com.example.SplitLoop.auth.infrastructure.persistence.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RefreshTokenMapper {

    RefreshTokenEntity toDomain(RefreshTokenEntity entity);

    RefreshTokenEntity toEntity(RefreshTokenEntity domain);
}
