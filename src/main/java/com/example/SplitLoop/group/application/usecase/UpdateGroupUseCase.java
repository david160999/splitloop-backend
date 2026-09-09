package com.example.SplitLoop.group.application.usecase;

import com.example.SplitLoop.group.controller.request.UpdateGroupRequest;
import com.example.SplitLoop.group.controller.response.GroupResponse;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import com.example.SplitLoop.group.mapper.GroupMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @Transactional
    public GroupResponse execute(UUID groupId, UpdateGroupRequest request) throws BadRequestException {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        groupService.updateGroup(group, request.getName(), request.getDescription());

        return groupMapper.toResponse(group);
    }
}


