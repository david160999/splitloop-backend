package com.example.SplitLoop.group.controller.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {

    private UUID id;

    private String name;

    private UUID createdBy;

    private LocalDateTime createdAt;
}
