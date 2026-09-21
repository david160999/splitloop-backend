package com.example.SplitLoop.group.domain.repository;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.entity.MemberRole;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {
    List<GroupMember> findByGroupId(UUID groupId);

    Optional<GroupMember> findByGroupIdAndUserId(UUID groupId, UUID userId);

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);

    void deleteByGroupIdAndUserId(UUID groupId, UUID userId);

    Optional<GroupMember> findByGroupAndUser(Group group, UserEntity userEntity);

    boolean existsByGroupIdAndUserIdAndMemberRole(UUID groupId, UUID userId, MemberRole memberRole);

    long countByGroupIdAndMemberRole(UUID groupId, MemberRole memberRole);

    List<GroupMember> findAllByGroup(Group group);

    int countByGroup(Group group);
}