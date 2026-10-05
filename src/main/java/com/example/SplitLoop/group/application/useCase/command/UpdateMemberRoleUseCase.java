package com.example.SplitLoop.group.application.useCase.command;

import com.example.SplitLoop.group.application.mapper.GroupDtoMapper;
import com.example.SplitLoop.group.domain.exception.*;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.model.MemberRole;
import com.example.SplitLoop.group.domain.port.GroupUserPort;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.group.application.dto.request.UpdateMemberRoleRequest;
import com.example.SplitLoop.group.application.dto.response.GroupMemberResponse;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateMemberRoleUseCase {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final GroupUserPort userPort;
    private final CurrentUserService currentUserService;
    private final GroupDtoMapper groupMapper;

    @Transactional
    public GroupMemberResponse execute(UUID groupId, UUID memberId, UpdateMemberRoleRequest request) {

        // 1. Obtener el grupo
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        // 2. Obtener el usuario objetivo
        User targetUser = userPort.findById(memberId).orElseThrow(() -> new UserNotFoundException(memberId));

        // 3. Regla: No se puede cambiar el rol del creador del grupo
        if (group.isCreatedBy(targetUser.id())) {
            throw new CannotChangeGroupCreatorRoleException();
        }

        // 4. Verificar permisos del solicitante (debe ser ADMIN)
        User requester = currentUserService.getCurrentUser();
        GroupMember requesterMember = memberRepository.findByGroupIdAndUserId(group.id(), requester.id())
                .orElseThrow(() -> new GroupMemberNotFoundException(group.id(), requester.id()));

        if (!requesterMember.isAdmin()) {
            throw new InsufficientPermissionsException();
        }

        // 5. Obtener la membresía del usuario objetivo
        GroupMember targetMember = memberRepository.findByGroupIdAndUserId(group.id(), targetUser.id())
                .orElseThrow(() -> new GroupMemberNotFoundException(group.id(), targetUser.id()));

        // 6. Si se le retira el rol de ADMIN, validar que no sea el último administrador
        if (targetMember.isAdmin() && request.getNewRole() != MemberRole.ADMIN) {
            long adminCount = memberRepository.countByGroupIdAndMemberRole(group.id(), MemberRole.ADMIN);
            if (adminCount <= 1) {
                throw new LastAdminCannotLeaveGroupException(); // O LastAdminRoleCannotBeChangedException
            }
        }

        // 7. Crear nueva instancia inmutable con el rol actualizado
        GroupMember updatedTargetMember = targetMember.toBuilder()
                .memberRole(request.getNewRole())
                .build();

        // 8. Persistir cambios y retornar DTO
        GroupMember savedMember = memberRepository.save(updatedTargetMember);

        return groupMapper.toGroupMemberResponse(savedMember);
    }
}