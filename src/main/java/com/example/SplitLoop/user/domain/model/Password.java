package com.example.SplitLoop.user.domain.model;

import com.example.SplitLoop.user.application.exception.PasswordMismatchException;
import com.example.SplitLoop.user.domain.exception.PasswordRequiredException;
import com.example.SplitLoop.user.domain.exception.WeakPasswordException;

public record Password(String value) {

    public Password {
        validateStrength(value);
    }

    public static void validateConfirmation(String newPassword, String confirmPassword) {
        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
    }

    private static void validateStrength(String password) {
        if (password == null || password.isBlank()) {
            throw new PasswordRequiredException();
        }

        if (password.length() < 8) {
            throw new WeakPasswordException();
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;

            if (hasUpper && hasLower && hasDigit) break;
        }

        if (!hasUpper || !hasLower || !hasDigit) {
            throw new WeakPasswordException();
        }
    }
}
