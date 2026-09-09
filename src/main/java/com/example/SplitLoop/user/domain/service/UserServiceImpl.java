package com.example.SplitLoop.user.domain.service;

import com.example.SplitLoop.group.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.controller.command.ChangePasswordRequest;
import com.example.SplitLoop.user.controller.command.CreateUserRequest;
import com.example.SplitLoop.user.controller.response.UserResponse;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.validator.UserValidator;
import com.example.SplitLoop.user.exception.InvalidPasswordException;
import com.example.SplitLoop.user.exception.PasswordsDoNotMatchException;
import com.example.SplitLoop.user.exception.SamePasswordException;
import com.example.SplitLoop.user.mapper.UserMapper;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator validator;


    @Override
    public UserResponse getById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return userMapper.toResponse(user);
    }

    @Override
    public void updateUser(User user, String username, String email) {

        validator.validateUpdateUser(user, username, email);

        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {

            throw new EmailAlreadyExistsException(email);
        }

        user.setUsername(username);
        user.setEmail(email);
    }

    @Override
    public void deleteUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }

    @Override
    public void changePassword(
            User user,
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new SamePasswordException();
        }

        validator.validatePasswordChange(newPassword, confirmPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

}