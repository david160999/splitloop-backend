package com.example.SplitLoop.auth.infrastructure.security;

import com.example.SplitLoop.auth.application.command.LogoutUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final LogoutUseCase logoutUseCase;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                logoutUseCase.execute(cookie.getValue());
                break;
            }
        }
    }
}
