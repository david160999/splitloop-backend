package com.example.SplitLoop.group.infrastructure.persistence.adapter;

import com.example.SplitLoop.group.domain.port.GroupUserPort;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.infrastructure.persistence.jpa.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocalUserAdapter implements GroupUserPort {

    private final SpringDataUserRepository userRepository;

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId)
                .map(u -> User.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .email(u.getEmail())
                        .build());
    }

    @Override
    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }
}
