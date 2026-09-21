package com.example.SplitLoop.auth.domain.port;

import com.example.SplitLoop.user.domain.model.User;

public interface AuthenticationPort {
    User authenticate(String email, String password);
}
