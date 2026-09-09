package com.example.SplitLoop.group.mapper;

import com.example.SplitLoop.group.controller.response.GroupMemberResponse;
import com.example.SplitLoop.group.controller.response.GroupResponse;
import com.example.SplitLoop.group.controller.response.GroupSummaryResponse;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.modelo.GroupSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "createdBy", source = "createdBy.id")
    GroupResponse toResponse(Group group);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    GroupMemberResponse toResponse(GroupMember member);

    @Mapping(target = "id", source = "group.id")
    @Mapping(target = "name", source = "group.name")
    GroupSummaryResponse toSummaryResponse(GroupSummary summary);
}