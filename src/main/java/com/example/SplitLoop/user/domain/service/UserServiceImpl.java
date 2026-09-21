package com.example.SplitLoop.user.domain.service;

import com.example.SplitLoop.group.exception.EmailAlreadyExistsException;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.controller.response.UserResponse;
import com.example.SplitLoop.user.domain.entity.UserEntity;
import com.example.SplitLoop.user.domain.validator.UserValidator;
import com.example.SplitLoop.user.exception.InvalidPasswordException;
import com.example.SplitLoop.user.exception.SamePasswordException;
import com.example.SplitLoop.user.mapper.UserMapper;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponse(userEntity);
    }

    @Override
    public UserResponse getByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return userMapper.toResponse(userEntity);
    }

    @Override
    public void updateUser(UserEntity userEntity, String username, String email) {

        validator.validateUpdateUser(userEntity, username, email);

        if (!userEntity.getEmail().equals(email) && userRepository.existsByEmail(email)) {

            throw new EmailAlreadyExistsException(email);
        }

        userEntity.setUsername(username);
        userEntity.setEmail(email);
    }

    @Override
    public void deleteUser(UUID id) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(userEntity);
    }

    @Override
    public void changePassword(
            UserEntity userEntity,
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        if (!passwordEncoder.matches(currentPassword, userEntity.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (passwordEncoder.matches(newPassword, userEntity.getPassword())) {
            throw new SamePasswordException();
        }

        validator.validatePasswordChange(newPassword, confirmPassword);

        userEntity.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

}