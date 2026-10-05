package com.example.SplitLoop.group.application.dto.query;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetGroupQuery(

        @NotNull(message = "Group id is required")
        UUID groupId,

        @NotNull(message = "Requester id is required")
        UUID requesterId

) {
}
