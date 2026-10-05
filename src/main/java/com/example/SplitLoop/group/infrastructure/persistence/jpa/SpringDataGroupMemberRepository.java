package com.example.SplitLoop.group.infrastructure.persistence.jpa;

import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.group.domain.model.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataGroupMemberRepository extends JpaRepository<GroupMemberEntity, UUID> {

    Optional<GroupMemberEntity> findByGroupIdAndUserId(UUID groupId, UUID userId);

    List<GroupMemberEntity> findByGroupId(UUID groupId);

    List<GroupMemberEntity> findByUserId(UUID userId);

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);

    boolean existsByGroupIdAndUserIdAndMemberRole(UUID groupId, UUID userId, MemberRole role);

    void deleteByGroupIdAndUserId(UUID groupId, UUID userId);

    long countByGroupId(UUID groupId);

    long countByGroupIdAndMemberRole(UUID groupId, MemberRole memberRole);
}
