package com.example.SplitLoop.expense.domain.port;

import com.example.SplitLoop.group.domain.model.Group;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepositoryPort {
    Optional<Group> findById(UUID id);
    boolean existsById(UUID id);
}
