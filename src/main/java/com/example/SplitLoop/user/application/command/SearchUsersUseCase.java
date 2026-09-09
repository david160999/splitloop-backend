package com.example.SplitLoop.user.application.command;

import com.example.SplitLoop.user.controller.query.SearchUsersQuery;
import com.example.SplitLoop.user.controller.response.UserResponse;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import com.example.SplitLoop.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUsersUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserResponse> execute(SearchUsersQuery query) {

        return userRepository.searchByUsername(query.getQuery())
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }
}