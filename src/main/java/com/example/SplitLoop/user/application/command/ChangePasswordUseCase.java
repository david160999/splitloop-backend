package com.example.SplitLoop.user.application.command;

import com.example.SplitLoop.user.application.dto.request.ChangePasswordRequest;
import com.example.SplitLoop.user.application.exception.PasswordMismatchException;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.port.PasswordEncoderPort;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import com.example.SplitLoop.user.domain.service.UserService;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final UserService userDomainService; // Servicio de Dominio
    private final PasswordEncoderPort passwordEncoder;

    @Transactional
    public void execute(ChangePasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        User user = currentUserService.getCurrentUser();

        // Reutilizamos la regla del dominio
        userDomainService.validatePasswordChange(user, request.getCurrentPassword(), request.getNewPassword());

        // Creamos la nueva instancia inmutable con la clave cifrada
        User updatedUser = new User(
                user.id(),
                user.email(),
                user.username(),
                passwordEncoder.encode(request.getNewPassword()),
                user.role()
        );

        userRepository.save(updatedUser);
    }
}
