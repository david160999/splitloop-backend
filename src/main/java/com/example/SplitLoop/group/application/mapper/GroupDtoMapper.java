package com.example.SplitLoop.group.application.mapper;

import com.example.SplitLoop.group.application.dto.response.GroupMemberResponse;
import com.example.SplitLoop.group.application.dto.response.GroupResponse;
import com.example.SplitLoop.group.application.dto.response.GroupSummaryResponse;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.model.GroupSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupDtoMapper {

    // Group -> GroupResponse
    // Extrae el ID del objeto User (createdBy.id)
    @Mapping(target = "createdBy", source = "createdBy.id")
    GroupResponse toGroupResponse(Group group);

    // GroupMember -> GroupMemberResponse
    // Extrae campos de la relación anidada user (user.id, user.username, user.email)
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    GroupMemberResponse toGroupMemberResponse(GroupMember groupMember);

    // GroupSummary -> GroupSummaryResponse
    // Desempaqueta las propiedades de group (group.id, group.name)
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "name", source = "group.name")
    GroupSummaryResponse toGroupSummaryResponse(GroupSummary groupSummary);
}