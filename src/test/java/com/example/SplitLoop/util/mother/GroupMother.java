package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public final class GroupMother {

    private GroupMother() {
    }

    public static GroupEntity group() {

        return group(UserMother.userEntity());
    }

    public static GroupEntity group(UserEntity createdBy) {

        return GroupEntity.builder()
                .id(UUID.randomUUID())
                .name("Test Group")
                .description("Description")
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
