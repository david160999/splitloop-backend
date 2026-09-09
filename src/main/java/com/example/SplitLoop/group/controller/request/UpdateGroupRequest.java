package com.example.SplitLoop.group.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateGroupRequest {

        @NotBlank(message = "Group name is required")
        private String name;

        private String description;
}
