package com.example.SplitLoop.group.application.useCase.command;

import com.example.SplitLoop.group.application.dto.request.CreateGroupRequest;
import com.example.SplitLoop.group.application.dto.response.GroupResponse;
import com.example.SplitLoop.group.application.mapper.GroupDtoMapper;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.model.MemberRole;
import com.example.SplitLoop.group.domain.port.GroupUserPort;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CreateGroupUseCase {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final GroupUserPort userPort;
    private final GroupDtoMapper mapper;

    @Transactional
    public GroupResponse execute(CreateGroupRequest request) {

        User creator = userPort.findById(request.getCreatedBy())
                .orElseThrow(() -> new UserNotFoundException(request.getCreatedBy()));

        Group group = Group.builder()
                .name(request.getName())
                .createdBy(creator)
                .createdAt(LocalDateTime.now(ZoneId.systemDefault()))
                .build();

        Group savedGroup = groupRepository.save(group);

        GroupMember admin = GroupMember.builder()
                .group(savedGroup)
                .user(creator)
                .memberRole(MemberRole.ADMIN)
                .build();

        memberRepository.save(admin);

        return mapper.toGroupResponse(savedGroup);
    }
}
