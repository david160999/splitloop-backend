package com.example.SplitLoop.group.domain.service;

import com.example.SplitLoop.group.controller.request.AddMemberRequest;
import com.example.SplitLoop.group.controller.response.GroupMemberResponse;
import com.example.SplitLoop.group.domain.entity.MemberRole;

import java.util.List;
import java.util.UUID;

public interface GroupMemberService {

    GroupMemberResponse addMember(UUID groupId, AddMemberRequest request);

    List<GroupMemberResponse> getMembers(UUID groupId);

    GroupMemberResponse changeRole(UUID groupId, UUID userId, MemberRole role);

    void removeMember(UUID groupId, UUID userId);




}
