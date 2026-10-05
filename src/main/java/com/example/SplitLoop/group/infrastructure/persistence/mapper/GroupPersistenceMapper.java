package com.example.SplitLoop.group.infrastructure.persistence.mapper;

import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserPersistenceMapper.class} // Reutiliza el mapper de User para convertir UserEntity -> User
)
public interface GroupPersistenceMapper {

    Group toDomain(GroupEntity entity);
    GroupEntity toEntity(Group domain);

    GroupMember toDomain(GroupMemberEntity entity);
    GroupMemberEntity toEntity(GroupMember domain);

    List<GroupMember> toDomainList(List<GroupMemberEntity> entities);
    List<GroupMemberEntity> toEntityList(List<GroupMember> domainList);
}
