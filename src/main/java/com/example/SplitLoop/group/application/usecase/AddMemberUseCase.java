package com.example.SplitLoop.group.application.usecase;

import com.example.SplitLoop.group.controller.request.AddMemberRequest;
import com.example.SplitLoop.group.controller.response.GroupMemberResponse;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.group.mapper.GroupMapper;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddMemberUseCase {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final GroupMapper mapper;
    private final UserRepository userRepository;

    @Transactional
    public GroupMemberResponse execute(UUID groupId, AddMemberRequest request) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(request.getUserId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        GroupMember member = groupService.addMember(
                group,
                user,
                request.getMemberRole()) ;

        return mapper.toResponse(member);
    }
}

