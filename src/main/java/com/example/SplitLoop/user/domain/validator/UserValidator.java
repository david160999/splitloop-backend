package com.example.SplitLoop.user.domain.validator;

import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.exception.*;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateUpdateUser(
            User user,
            String username,
            String email) {

        if (username == null || username.isBlank()) {
            throw new UsernameRequiredException();
        }

        if (username.equals(user.getUsername()) && email.equals(user.getEmail())) {
            throw new NothingToUpdateException();
        }
    }

    public void validatePasswordChange(
            String newPassword,
            String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        validatePasswordStrength(newPassword);
    }

    private void validatePasswordStrength(String password) {

        if (password == null || password.isBlank()) {
            throw new PasswordRequiredException();
        }

        if (password.length() < 8) {
            throw new WeakPasswordException();
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new WeakPasswordException();
        }

        if (!password.matches(".*[a-z].*")) {
            throw new WeakPasswordException();
        }

        if (!password.matches(".*\\d.*")) {
            throw new WeakPasswordException();
        }
    }
}