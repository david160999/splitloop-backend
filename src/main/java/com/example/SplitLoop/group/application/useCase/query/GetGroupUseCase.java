package com.example.SplitLoop.group.application.useCase.query;

import com.example.SplitLoop.group.application.mapper.GroupDtoMapper;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.group.application.dto.response.GroupResponse;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final GroupDtoMapper groupMapper;
    private final CurrentUserService currentUserService;

    @Transactional
    public GroupResponse execute(UUID groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User requester = currentUserService.getCurrentUser();

        groupService.validateMember(groupId, requester.id());

        return groupMapper.toGroupResponse(group);
    }
}