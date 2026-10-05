package com.example.SplitLoop.group.application.useCase.command;

import com.example.SplitLoop.group.application.dto.request.UpdateGroupRequest;
import com.example.SplitLoop.group.application.dto.response.GroupResponse;
import com.example.SplitLoop.group.application.mapper.GroupDtoMapper;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupDtoMapper mapper;

    @Transactional
    public GroupResponse execute(UUID groupId, UpdateGroupRequest request) {

        Group existingGroup = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        Group updatedGroup = existingGroup.toBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .updatedAt(LocalDateTime.now(ZoneId.systemDefault()))
                .build();

        Group savedGroup = groupRepository.save(updatedGroup);

        return mapper.toGroupResponse(savedGroup);
    }
}


