package com.example.SplitLoop.group.application.usecase;

import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.mapper.GroupMapper;
import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.group.controller.request.UpdateMemberRoleRequest;
import com.example.SplitLoop.group.controller.response.GroupMemberResponse;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateMemberRoleUseCase {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final CurrentUserService currentUserService;
    private final GroupService groupService;

    private final GroupMapper groupMapper;

    @Transactional
    public GroupMemberResponse execute(UUID groupId, UUID memberId, UpdateMemberRoleRequest request) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User requester = currentUserService.getCurrentUser();

        User targetUser = userRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));

        GroupMember member = groupService.updateMemberRole(
                group,
                requester,
                targetUser,
                request.getNewRole());

        return groupMapper.toResponse(member);
    }
}