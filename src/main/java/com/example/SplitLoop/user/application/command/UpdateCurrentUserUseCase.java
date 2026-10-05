package com.example.SplitLoop.user.application.command;

import com.example.SplitLoop.user.application.dto.request.UpdateUserRequest;
import com.example.SplitLoop.user.application.dto.response.UserResponse;
import com.example.SplitLoop.user.application.mapper.UserDtoMapper;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCurrentUserUseCase {

    private final CurrentUserService currentUserService;
    private final UserDtoMapper userMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public UserResponse execute(UpdateUserRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        // 1. Validar reglas de negocio simples sobre el modelo de dominio
        currentUser.validateUpdate(request.getUsername(), request.getEmail());

        // 2. Validar reglas de dominio que requieren infraestructura (existencia de email)
        userService.ensureEmailIsUnique(request.getEmail(), currentUser.email());

        // 3. Crear la nueva versión inmutable del usuario
        User updatedUser = currentUser.toBuilder()
                .username(request.getUsername())
                .email(request.getEmail())
                .build();

        // 4. Persistir la nueva versión en la BD
        User savedUser = userRepository.save(updatedUser);

        // 5. Mapear y devolver la respuesta
        return userMapper.toResponse(savedUser);
    }
}
