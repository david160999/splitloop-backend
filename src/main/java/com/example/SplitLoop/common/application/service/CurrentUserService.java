package com.example.SplitLoop.common.application.service;

import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.common.domain.port.AuthenticatedUserPort;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrentUserService {

    private final AuthenticatedUserPort authenticatedUserPort;
    private final UserRepository userRepository;

    public UUID getCurrentUserId() {
        return authenticatedUserPort.getCurrentUserId();
    }

    public User getCurrentUser() {
        UUID userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
