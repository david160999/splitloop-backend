package com.example.SplitLoop.auth.infrastructure.security;

import com.example.SplitLoop.auth.domain.port.TokenGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidTokenGeneratorAdapter implements TokenGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
