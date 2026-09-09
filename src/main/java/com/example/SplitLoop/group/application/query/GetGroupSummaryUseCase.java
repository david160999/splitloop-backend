package com.example.SplitLoop.group.application.query;

import com.example.SplitLoop.group.controller.query.GetGroupSummaryQuery;
import com.example.SplitLoop.group.controller.response.GroupSummaryResponse;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.modelo.GroupSummary;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.service.GroupSummaryService;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import com.example.SplitLoop.group.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetGroupSummaryUseCase {

    private final GroupRepository groupRepository;
    private final GroupSummaryService groupSummaryService;
    private final GroupMapper groupMapper;

    @Transactional(readOnly = true)
    public GroupSummaryResponse execute(UUID groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        GroupSummary summary = groupSummaryService.getSummary(group);

        return groupMapper.toSummaryResponse(summary);
    }
}