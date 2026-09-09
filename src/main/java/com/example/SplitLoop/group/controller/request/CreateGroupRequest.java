package com.example.SplitLoop.group.controller.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateGroupRequest {

    @NotBlank(message = "Group name is required")
    private String name;

    @NotNull(message = "CreatedBy is required")
    private UUID createdBy;
}