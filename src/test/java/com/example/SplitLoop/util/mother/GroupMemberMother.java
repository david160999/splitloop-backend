package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.group.domain.model.MemberRole;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;

import java.util.UUID;

public final class GroupMemberMother {

    private GroupMemberMother() {
    }

    public static GroupMemberEntity admin() {

        UserEntity userEntity = UserMother.userEntity();
        GroupEntity groupEntity = GroupMother.group(userEntity);

        return admin(groupEntity, userEntity);
    }

    public static GroupMemberEntity admin(GroupEntity groupEntity, UserEntity userEntity) {

        return GroupMemberEntity.builder()
                .id(UUID.randomUUID())
                .group(groupEntity)
                .user(userEntity)
                .memberRole(MemberRole.ADMIN)
                .build();
    }

    public static GroupMemberEntity member() {

        UserEntity userEntity = UserMother.userEntity();
        GroupEntity groupEntity = GroupMother.group(userEntity);

        return member(groupEntity, userEntity);
    }

    public static GroupMemberEntity member(GroupEntity groupEntity, UserEntity userEntity) {

        return GroupMemberEntity.builder()
                .id(UUID.randomUUID())
                .group(groupEntity)
                .user(userEntity)
                .memberRole(MemberRole.MEMBER)
                .build();
    }
}