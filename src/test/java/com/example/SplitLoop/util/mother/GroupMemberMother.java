package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.entity.MemberRole;
import com.example.SplitLoop.user.domain.entity.UserEntity;

import java.util.UUID;

public final class GroupMemberMother {

    private GroupMemberMother() {
    }

    public static GroupMember admin() {

        UserEntity userEntity = UserMother.userEntity();
        Group group = GroupMother.group(userEntity);

        return admin(group, userEntity);
    }

    public static GroupMember admin(Group group, UserEntity userEntity) {

        return GroupMember.builder()
                .id(UUID.randomUUID())
                .group(group)
                .user(userEntity)
                .memberRole(MemberRole.ADMIN)
                .build();
    }

    public static GroupMember member() {

        UserEntity userEntity = UserMother.userEntity();
        Group group = GroupMother.group(userEntity);

        return member(group, userEntity);
    }

    public static GroupMember member(Group group, UserEntity userEntity) {

        return GroupMember.builder()
                .id(UUID.randomUUID())
                .group(group)
                .user(userEntity)
                .memberRole(MemberRole.MEMBER)
                .build();
    }
}