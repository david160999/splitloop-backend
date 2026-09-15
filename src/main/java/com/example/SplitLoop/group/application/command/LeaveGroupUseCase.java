package com.example.SplitLoop.group.application.command;

import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveGroupUseCase {
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final CurrentUserService currentUserService;

    @Transactional
    public void execute(UUID groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User requester = currentUserService.getCurrentUser();

        groupService.leaveGroup(group, requester);
    }
}
