package com.example.SplitLoop.group.application.useCase.command;

import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.group.domain.exception.OnlyGroupCreatorCanDeleteException;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.policy.GroupDeletionPolicy;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteGroupUseCase {

    private final GroupRepository groupRepository;
    private final CurrentUserService userService;
    private final List<GroupDeletionPolicy> groupDeletionPolicies;

    @Transactional
    public void execute(UUID groupId) {

        // 1. Obtener usuario actual y el grupo de dominio
        User currentUser = userService.getCurrentUser();
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        // 2. Validar que el usuario actual sea el creador del grupo
        if (!group.isCreatedBy(currentUser.id())) {
            throw new OnlyGroupCreatorCanDeleteException();
        }

        // 3. Ejecutar las políticas de borrado
        groupDeletionPolicies.forEach(policy -> policy.validateCanDelete(group));

        // 4. Eliminar a través del puerto
        groupRepository.deleteById(group.id());
    }
}