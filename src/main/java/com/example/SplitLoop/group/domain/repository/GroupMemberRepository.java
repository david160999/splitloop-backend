package com.example.SplitLoop.group.domain.repository;

import com.example.SplitLoop.group.domain.model.MemberRole;
import com.example.SplitLoop.group.domain.model.GroupMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository {

    GroupMember save(GroupMember groupMember);

    Optional<GroupMember> findById(UUID id);

    Optional<GroupMember> findByGroupIdAndUserId(UUID groupId, UUID userId);

    List<GroupMember> findByGroupId(UUID groupId);

    List<GroupMember> findByUserId(UUID userId);

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);

    boolean existsByGroupIdAndUserIdAndMemberRole(UUID groupId, UUID userId, MemberRole role);

    void deleteById(UUID id);

    void deleteByGroupIdAndUserId(UUID groupId, UUID userId);

    long countByGroupId(UUID groupId);

    long countByGroupIdAndMemberRole(UUID groupId, MemberRole role);
}