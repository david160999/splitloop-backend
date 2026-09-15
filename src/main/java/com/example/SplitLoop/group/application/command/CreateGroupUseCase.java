package com.example.SplitLoop.group.application.command;

import com.example.SplitLoop.group.controller.request.CreateGroupRequest;
import com.example.SplitLoop.group.controller.response.GroupResponse;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.group.mapper.GroupMapper;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateGroupUseCase {

    private final GroupService groupService;
    private final UserRepository userRepository;
    private final GroupMapper mapper;

    @Transactional
    public GroupResponse execute(CreateGroupRequest request) {

        User creator = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new UserNotFoundException(request.getCreatedBy()));

        Group group = groupService.createGroup(creator, request.getName());

        return mapper.toResponse(group);

    }
}
