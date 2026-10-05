package com.example.SplitLoop.group.application.useCase.command;

import com.example.SplitLoop.group.domain.exception.CannotRemoveGroupCreatorException;
import com.example.SplitLoop.group.domain.exception.InsufficientPermissionsException;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.policy.MemberExitPolicy;
import com.example.SplitLoop.group.domain.port.GroupUserPort;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveMemberUseCase {

    private final CurrentUserService currentUserService;
    private final GroupUserPort userPort;

    private final GroupService groupService;

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final List<MemberExitPolicy> memberExitPolicies;

    @Transactional
    public void execute(UUID groupId, UUID userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User requester = currentUserService.getCurrentUser();

        User memberToRemove = userPort.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        GroupMember requesterMember = groupService.getMember(group.id(), requester.id());

        if (group.isCreatedBy(memberToRemove.id())) {
            throw new CannotRemoveGroupCreatorException();
        }

        if (!requesterMember.isAdmin()) {
            throw new InsufficientPermissionsException();
        }

        GroupMember member = groupService.getMember(group.id(), memberToRemove.id());

        //Ejecutar todas las políticas de salida
        memberExitPolicies.forEach(policy -> policy.validateCanExist(group, memberToRemove));

        memberRepository.deleteById(member.id());

    }
}
