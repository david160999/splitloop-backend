package com.example.SplitLoop.group.application.dto.response;

import com.example.SplitLoop.group.domain.model.MemberRole;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GroupMemberResponse(
        UUID id,
        UUID userId,
        String name,
        String email,
        MemberRole memberRole,
        LocalDateTime joinedAt
) {}