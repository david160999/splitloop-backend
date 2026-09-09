package com.example.SplitLoop.group.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class LeaveGroupRequest {

    @NotNull(message = "User id is required")
    private UUID userId;

}
