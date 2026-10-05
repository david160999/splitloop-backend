package com.example.SplitLoop.user.infrastructure.config;

import com.example.SplitLoop.user.domain.port.PasswordEncoderPort;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import com.example.SplitLoop.user.domain.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        return new UserService(userRepository, passwordEncoder);
    }
}
