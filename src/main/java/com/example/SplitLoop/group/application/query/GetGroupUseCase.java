package com.example.SplitLoop.group.application.query;

import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.group.controller.response.GroupResponse;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import com.example.SplitLoop.group.mapper.GroupMapper;
import com.example.SplitLoop.user.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final CurrentUserService currentUserService;

    @Transactional
    public GroupResponse execute(UUID groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User requester = currentUserService.getCurrentUser();

        groupService.validateMember(groupId, requester.getId());

        return groupMapper.toResponse(group);
    }
}