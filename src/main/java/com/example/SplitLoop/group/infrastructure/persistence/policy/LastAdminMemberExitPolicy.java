package com.example.SplitLoop.group.infrastructure.persistence.policy;

import com.example.SplitLoop.group.domain.exception.GroupMemberNotFoundException;
import com.example.SplitLoop.group.domain.exception.LastAdminCannotLeaveGroupException;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.model.MemberRole;
import com.example.SplitLoop.group.domain.policy.MemberExitPolicy;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LastAdminMemberExitPolicy implements MemberExitPolicy {

    private final GroupMemberRepository memberRepository;

    @Override
    public void validateCanExist(Group group, User user) {
        GroupMember member = memberRepository.findByGroupIdAndUserId(group.id(), user.id())
                .orElseThrow(() -> new GroupMemberNotFoundException(group.id(), user.id()));

        if (member.isAdmin()) {
            long adminCount = memberRepository.countByGroupIdAndMemberRole(group.id(), MemberRole.ADMIN);
            if (adminCount <= 1) {
                throw new LastAdminCannotLeaveGroupException();
            }
        }
    }
}
