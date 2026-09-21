package com.example.SplitLoop.auth.infrastructure.security;

import com.example.SplitLoop.auth.domain.port.AuthenticationPort;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecurityAuthAdapter implements AuthenticationPort {

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper; // Convierte UserEntity -> User (Dominio)

    @Override
    public User authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserEntity entity = (UserEntity) authentication.getPrincipal();
        return userMapper.toDomain(entity);
    }
}
