package com.example.SplitLoop.group.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record GroupResponse(
        UUID id,
        String name,
        UUID createdBy,
        LocalDateTime createdAt
) {}
