package com.example.SplitLoop.group.controller.request;

import com.example.SplitLoop.group.domain.entity.MemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddMemberRequest {

    @NotNull(message = "User is required")
    private UUID userId;

    @NotNull(message = "Role is required")
    private MemberRole memberRole;
}
