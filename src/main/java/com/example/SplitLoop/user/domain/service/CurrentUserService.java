package com.example.SplitLoop.user.domain.service;

import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.CustomUserPrincipal;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public UUID getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        CustomUserPrincipal principal =
                (CustomUserPrincipal) authentication.getPrincipal();

        assert principal != null;
        return principal.getId();
    }

    public User getCurrentUser() {

        return userRepository.findById(getCurrentUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(getCurrentUserId()));
    }
}