package com.example.SplitLoop.payment.infrastructure.persistence.adapter;

import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.model.MemberRole;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.payment.domain.port.GroupMemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentGroupMemberAdapter implements GroupMemberPort {

    private final GroupMemberRepository memberRepository;

    @Override
    public Optional<GroupMember> findByGroupIdAndUserId(UUID groupId, UUID userId) {
        return memberRepository.findByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public List<GroupMember> findMembersByGroupId(UUID groupId) {
        return memberRepository.findByGroupId(groupId);
    }

    @Override
    public boolean isUserInGroup(UUID groupId, UUID userId) {
        return memberRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public boolean isUserAdminInGroup(UUID groupId, UUID userId) {
        return memberRepository.existsByGroupIdAndUserIdAndMemberRole(groupId, userId, MemberRole.ADMIN);
    }
}
