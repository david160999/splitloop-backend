package com.example.SplitLoop.group.application.useCase.command;

import com.example.SplitLoop.group.application.dto.request.AddMemberRequest;
import com.example.SplitLoop.group.application.dto.response.GroupMemberResponse;
import com.example.SplitLoop.group.application.mapper.GroupDtoMapper;
import com.example.SplitLoop.group.domain.exception.UserAlreadyInGroupException;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.port.GroupUserPort;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddMemberUseCase {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final GroupService groupService;
    private final GroupDtoMapper mapper;
    private final GroupUserPort userPort;

    @Transactional
    public GroupMemberResponse execute(UUID groupId, AddMemberRequest request) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(request.getUserId()));

        User user = userPort.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));


        if (groupService.isMember(group.id(), user.id())) {
            throw new UserAlreadyInGroupException(user.id(), group.id());
        }

        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .memberRole(request.getMemberRole())
                .build();

        member = memberRepository.save(member);

        return mapper.toGroupMemberResponse(member);
    }
}

