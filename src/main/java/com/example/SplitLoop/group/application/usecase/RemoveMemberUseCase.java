package com.example.SplitLoop.group.application.usecase;

import com.example.SplitLoop.user.domain.service.CurrentUserService;
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
public class RemoveMemberUseCase {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final CurrentUserService currentUserService;

    @Transactional
    public void execute(UUID groupId, UUID userId){

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User requester = currentUserService.getCurrentUser();

        User member = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        groupService.removeMember(group, requester, member);
    }
}
