package com.example.SplitLoop.util.security;

import com.example.SplitLoop.user.domain.model.CustomUserPrincipal;
import com.example.SplitLoop.user.domain.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public final class SecurityTestUtils {

    private SecurityTestUtils() {
    }

    public static void login(User user) {

        CustomUserPrincipal principal =
                new CustomUserPrincipal(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        List.of().toString());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities());

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    public static void logout() {

        SecurityContextHolder.clearContext();

    }

}
