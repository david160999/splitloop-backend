package com.example.SplitLoop.group.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RemoveMemberRequest {

    @NotNull(message = "Group id is required")
    private UUID groupId;

    @NotNull(message = "Admin id is required")
    private UUID adminId;

    @NotNull(message = "Member id is required")
    private UUID memberId;

}