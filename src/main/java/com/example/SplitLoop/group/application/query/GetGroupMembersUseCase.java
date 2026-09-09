package com.example.SplitLoop.group.application.query;

import com.example.SplitLoop.group.controller.query.GetGroupMembersQuery;
import com.example.SplitLoop.group.controller.response.GroupMemberResponse;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import com.example.SplitLoop.group.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetGroupMembersUseCase {

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final GroupMapper mapper;

    @Transactional(readOnly = true)
    public List<GroupMemberResponse> execute(GetGroupMembersQuery request) {

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() ->
                        new GroupNotFoundException(request.getGroupId()));

        return groupService.getMembers(group)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}