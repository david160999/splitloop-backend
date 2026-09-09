package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.entity.MemberRole;
import com.example.SplitLoop.user.domain.entity.User;

import java.util.UUID;

public final class GroupMemberMother {

    private GroupMemberMother() {
    }

    public static GroupMember admin() {

        User user = UserMother.user();
        Group group = GroupMother.group(user);

        return admin(group, user);
    }

    public static GroupMember admin(Group group, User user) {

        return GroupMember.builder()
                .id(UUID.randomUUID())
                .group(group)
                .user(user)
                .memberRole(MemberRole.ADMIN)
                .build();
    }

    public static GroupMember member() {

        User user = UserMother.user();
        Group group = GroupMother.group(user);

        return member(group, user);
    }

    public static GroupMember member(Group group, User user) {

        return GroupMember.builder()
                .id(UUID.randomUUID())
                .group(group)
                .user(user)
                .memberRole(MemberRole.MEMBER)
                .build();
    }
}