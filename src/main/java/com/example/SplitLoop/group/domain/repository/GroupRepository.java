package com.example.SplitLoop.group.domain.repository;

import com.example.SplitLoop.group.domain.model.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository {

    Group save(Group group);

    Optional<Group> findById(UUID id);

    List<Group> findByCreatedBy(UUID createdById);

    List<Group> findAllByUserId(UUID userId);

    void deleteById(UUID id);
}