package com.example.SplitLoop.group.application.useCase.command;

import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.policy.MemberExitPolicy;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveGroupUseCase {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final GroupService groupService;
    private final CurrentUserService currentUserService;
    private final List<MemberExitPolicy> memberExitPolicies;

    @Transactional
    public void execute(UUID groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User requester = currentUserService.getCurrentUser();

        GroupMember member = groupService.getMember(group.id(), requester.id());

        //Ejecutar todas las políticas de salida
        memberExitPolicies.forEach(policy -> policy.validateCanExist(group, requester));

        memberRepository.deleteById(member.id());

    }
}
