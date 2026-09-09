package com.example.SplitLoop.group.domain.repository;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.entity.MemberRole;
import com.example.SplitLoop.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {
    List<GroupMember> findByGroupId(UUID groupId);

    List<GroupMember> findByUserId(UUID userId);

    Optional<GroupMember> findByGroupIdAndUserId(UUID groupId, UUID userId);

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);

    void deleteByGroupIdAndUserId(UUID groupId, UUID userId);

    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    boolean existsByGroupIdAndUserIdAndMemberRole(UUID groupId, UUID userId, MemberRole memberRole);

    long countByGroupIdAndMemberRole(UUID groupId, MemberRole memberRole);

    List<GroupMember> findAllByGroup(Group group);

    int countByGroup(Group group);
}