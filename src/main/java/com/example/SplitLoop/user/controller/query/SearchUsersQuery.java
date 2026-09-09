package com.example.SplitLoop.user.controller.query;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUsersQuery {

    @NotBlank
    private String query;
}
