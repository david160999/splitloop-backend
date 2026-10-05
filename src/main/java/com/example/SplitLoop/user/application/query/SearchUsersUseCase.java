package com.example.SplitLoop.user.application.query;

import com.example.SplitLoop.user.application.dto.query.SearchUsersQuery;
import com.example.SplitLoop.user.application.dto.response.UserResponse;
import com.example.SplitLoop.user.application.mapper.UserDtoMapper;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUsersUseCase {

    private final UserRepository userRepository;
    private final UserDtoMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserResponse> execute(SearchUsersQuery query) {

        return userRepository.searchByUsername(query.getQuery())
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }
}