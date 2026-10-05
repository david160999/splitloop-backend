package com.example.SplitLoop.user.domain.service;

import com.example.SplitLoop.group.domain.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.user.domain.exception.InvalidPasswordException;
import com.example.SplitLoop.user.domain.exception.SamePasswordException;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.port.PasswordEncoderPort;
import com.example.SplitLoop.user.domain.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Lógica reusable: Validar que un email no esté ocupado por OTRO usuario
    public void ensureEmailIsUnique(String email, String currentEmail) {
        if (!email.equalsIgnoreCase(currentEmail) && userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    // Lógica reusable: Validar políticas de contraseña sobre la entidad
    public void validatePasswordChange(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.password())) {
            throw new InvalidPasswordException();
        }
        if (passwordEncoder.matches(newPassword, user.password())) {
            throw new SamePasswordException();
        }
    }
}
