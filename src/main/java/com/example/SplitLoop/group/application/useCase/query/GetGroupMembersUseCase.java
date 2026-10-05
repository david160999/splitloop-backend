package com.example.SplitLoop.group.application.useCase.query;

import com.example.SplitLoop.group.application.dto.query.GetGroupMembersQuery;
import com.example.SplitLoop.group.application.dto.response.GroupMemberResponse;
import com.example.SplitLoop.group.application.mapper.GroupDtoMapper;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetGroupMembersUseCase {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final GroupDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<GroupMemberResponse> execute(GetGroupMembersQuery request) {

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException(request.getGroupId()));

        return memberRepository.findByGroupId(group.id())
                .stream()
                .map(mapper::toGroupMemberResponse)
                .toList();
    }
}