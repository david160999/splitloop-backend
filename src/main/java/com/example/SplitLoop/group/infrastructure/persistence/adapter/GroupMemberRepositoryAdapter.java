package com.example.SplitLoop.group.infrastructure.persistence.adapter;

import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.group.domain.model.MemberRole;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.infrastructure.persistence.jpa.SpringDataGroupMemberRepository;
import com.example.SplitLoop.group.infrastructure.persistence.mapper.GroupPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class GroupMemberRepositoryAdapter implements GroupMemberRepository {

    private final SpringDataGroupMemberRepository jpaRepository;
    private final GroupPersistenceMapper groupMapper;

    @Override
    public GroupMember save(GroupMember groupMember) {
        GroupMemberEntity entity = groupMapper.toEntity(groupMember);
        GroupMemberEntity savedEntity = jpaRepository.save(entity);
        return groupMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<GroupMember> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(groupMapper::toDomain);
    }

    @Override
    public Optional<GroupMember> findByGroupIdAndUserId(UUID groupId, UUID userId) {
        return jpaRepository.findByGroupIdAndUserId(groupId, userId)
                .map(groupMapper::toDomain);
    }

    @Override
    public List<GroupMember> findByGroupId(UUID groupId) {
        return jpaRepository.findByGroupId(groupId)
                .stream()
                .map(groupMapper::toDomain)
                .toList();
    }

    @Override
    public List<GroupMember> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId)
                .stream()
                .map(groupMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByGroupIdAndUserId(UUID groupId, UUID userId) {
        return jpaRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public boolean existsByGroupIdAndUserIdAndMemberRole(UUID groupId, UUID userId, MemberRole role) {
        return jpaRepository.existsByGroupIdAndUserIdAndMemberRole(groupId, userId, role);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByGroupIdAndUserId(UUID groupId, UUID userId) {
        jpaRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public long countByGroupId(UUID groupId) {
        return jpaRepository.countByGroupId(groupId);
    }

    @Override
    public long countByGroupIdAndMemberRole(UUID groupId, MemberRole role) {
        return jpaRepository.countByGroupIdAndMemberRole(groupId, role);    }
}
