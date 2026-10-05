package com.example.SplitLoop.expense.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.port.GroupMemberPort;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.infrastructure.persistence.jpa.SpringDataGroupMemberRepository;
import com.example.SplitLoop.group.infrastructure.persistence.mapper.GroupPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseGroupMemberAdapter implements GroupMemberPort {

    private final SpringDataGroupMemberRepository jpaRepository;
    private final GroupPersistenceMapper mapper;

    @Override
    public Optional<GroupMember> findByGroupIdAndUserId(UUID groupId, UUID userId) {
        return jpaRepository.findByGroupIdAndUserId(groupId, userId)
                .map(mapper::toDomain);
    }

    @Override
    public List<GroupMember> findByGroupId(UUID groupId) {
        return mapper.toDomainList(jpaRepository.findByGroupId(groupId));
    }
}
