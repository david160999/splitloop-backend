package com.example.SplitLoop.group.infrastructure.persistence.mapper;

import com.example.SplitLoop.group.application.dto.response.GroupMemberResponse;
import com.example.SplitLoop.group.application.dto.response.GroupResponse;
import com.example.SplitLoop.group.application.dto.response.GroupSummaryResponse;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.group.domain.model.GroupSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "createdBy", source = "createdBy.id")
    GroupResponse toResponse(GroupEntity groupEntity);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    GroupMemberResponse toResponse(GroupMemberEntity member);

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "name", source = "group.name")
    GroupSummaryResponse toSummaryResponse(GroupSummary summary);
}