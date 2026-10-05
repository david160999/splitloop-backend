package com.example.SplitLoop.group.domain.service;

import com.example.SplitLoop.group.domain.exception.UserNotInGroupException;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;

import java.util.List;
import java.util.UUID;

public class GroupService {

    private final GroupMemberRepository memberRepository;

    public GroupService(GroupMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public GroupMember getMember(UUID groupId, UUID userId) {

        return memberRepository
                .findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new UserNotInGroupException(userId, groupId));
    }

    public boolean isMember(UUID groupId, UUID userId) {
        return memberRepository.existsByGroupIdAndUserId(groupId, userId);
    }

    public void validateMember(UUID groupId, UUID userId) {

        if (!isMember(groupId, userId)) {
            throw new UserNotInGroupException(userId, groupId);
        }
    }

    public List<GroupMember> getMembers(Group group) {
        return memberRepository.findByGroupId(group.id());
    }
}