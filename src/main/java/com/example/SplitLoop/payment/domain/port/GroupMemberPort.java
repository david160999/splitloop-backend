package com.example.SplitLoop.payment.domain.port;

import com.example.SplitLoop.group.domain.model.GroupMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberPort {

    Optional<GroupMember> findByGroupIdAndUserId(UUID groupId, UUID userId);

    List<GroupMember> findMembersByGroupId(UUID groupId);

    boolean isUserInGroup(UUID groupId, UUID userId);

    boolean isUserAdminInGroup(UUID groupId, UUID userId);
}
