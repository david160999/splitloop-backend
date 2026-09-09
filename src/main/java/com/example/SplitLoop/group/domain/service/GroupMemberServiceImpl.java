package com.example.SplitLoop.group.domain.service;

import com.example.SplitLoop.group.controller.request.AddMemberRequest;
import com.example.SplitLoop.group.controller.response.GroupMemberResponse;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.entity.MemberRole;
import com.example.SplitLoop.group.mapper.GroupMapper;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository memberRepository;
    private final GroupMapper mapper;


    @Override
    public GroupMemberResponse addMember(UUID groupId, AddMemberRequest request) {
        return null;
    }

    @Override
    public List<GroupMemberResponse> getMembers(UUID groupId) {

        return memberRepository.findByGroupId(groupId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public GroupMemberResponse changeRole(UUID groupId, UUID userId, MemberRole memberRole) {

        GroupMember member = memberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow();

        member.setMemberRole(memberRole);

        member = memberRepository.save(member);

        return mapper.toResponse(member);
    }

    @Override
    public void removeMember(UUID groupId, UUID userId) {

        memberRepository.deleteByGroupIdAndUserId(groupId, userId);
    }
}