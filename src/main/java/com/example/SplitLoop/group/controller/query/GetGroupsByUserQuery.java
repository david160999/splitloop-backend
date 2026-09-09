package com.example.SplitLoop.group.controller.query;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetGroupsByUserQuery {

    @NotNull(message = "User id is required")
    private UUID userId;

}