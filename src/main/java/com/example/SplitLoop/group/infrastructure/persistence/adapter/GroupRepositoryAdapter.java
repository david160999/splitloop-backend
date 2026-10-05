package com.example.SplitLoop.group.infrastructure.persistence.adapter;

import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.infrastructure.persistence.jpa.SpringDataGroupRepository;
import com.example.SplitLoop.group.infrastructure.persistence.mapper.GroupPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("groupDomainRepositoryAdapter")
@RequiredArgsConstructor
public class GroupRepositoryAdapter implements GroupRepository {

    private final SpringDataGroupRepository jpaRepository;
    private final GroupPersistenceMapper groupMapper;

    @Override
    public Group save(Group group) {
        GroupEntity entity = groupMapper.toEntity(group);
        GroupEntity savedEntity = jpaRepository.save(entity);
        return groupMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Group> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(groupMapper::toDomain);
    }

    @Override
    public List<Group> findByCreatedBy(UUID createdById) {
        return jpaRepository.findByCreatedById(createdById)
                .stream()
                .map(groupMapper::toDomain)
                .toList();
    }

    @Override
    public List<Group> findAllByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId)
                .stream()
                .map(groupMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}