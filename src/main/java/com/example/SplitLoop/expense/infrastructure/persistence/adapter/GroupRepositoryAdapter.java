package com.example.SplitLoop.expense.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.port.GroupRepositoryPort;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.infrastructure.persistence.jpa.SpringDataGroupRepository;
import com.example.SplitLoop.group.infrastructure.persistence.mapper.GroupPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("expenseGroupRepositoryAdapter")
@RequiredArgsConstructor
public class GroupRepositoryAdapter implements GroupRepositoryPort {

    private final SpringDataGroupRepository jpaRepository;
    private final GroupPersistenceMapper mapper;

    @Override
    public Optional<Group> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}
