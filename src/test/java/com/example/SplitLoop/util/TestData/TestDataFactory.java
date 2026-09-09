package com.example.SplitLoop.util.TestData;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import com.example.SplitLoop.util.mother.GroupMemberMother;
import com.example.SplitLoop.util.mother.GroupMother;
import com.example.SplitLoop.util.mother.UserMother;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataFactory {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;

    public ExpenseContext defaultContext() {

        User owner = userRepository.save(
                UserMother.user().toBuilder().id(null).build());

        User second = userRepository.save(
                UserMother.anotherUser().toBuilder().id(null).build());

        Group group = groupRepository.save(
                GroupMother.group(owner).toBuilder().id(null).build());

        GroupMember admin = memberRepository.save(
                GroupMemberMother.admin(group, owner).toBuilder().id(null).build());

        GroupMember member = memberRepository.save(
                GroupMemberMother.member(group, second).toBuilder().id(null).build());

        return ExpenseContext.builder()
                .owner(owner)
                .secondUser(second)
                .group(group)
                .admin(admin)
                .member(member)
                .build();
    }

}