package com.example.SplitLoop.group.controller.response;

import com.example.SplitLoop.group.domain.entity.MemberRole;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberResponse {

    private UUID id;

    private UUID userId;

    private String name;

    private String email;

    private MemberRole memberRole;

    private LocalDateTime joinedAt;
}