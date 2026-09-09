package com.example.SplitLoop.user.controller.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    private String firstName;

    private String lastName;
}
