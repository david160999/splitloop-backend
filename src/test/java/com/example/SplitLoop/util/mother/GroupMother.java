package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public final class GroupMother {

    private GroupMother() {
    }

    public static Group group() {

        return group(UserMother.user());
    }

    public static Group group(User createdBy) {

        return Group.builder()
                .id(UUID.randomUUID())
                .name("Test Group")
                .description("Description")
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
