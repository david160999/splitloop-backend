package com.example.SplitLoop.group.application.query;

import com.example.SplitLoop.group.controller.query.GetGroupsByUserQuery;
import com.example.SplitLoop.group.controller.response.GroupResponse;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.group.mapper.GroupMapper;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetGroupsByUserUseCase {

    private final UserRepository userRepository;
    private final GroupService groupService;
    private final GroupMapper mapper;

    @Transactional(readOnly = true)
    public List<GroupResponse> execute(GetGroupsByUserQuery request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        return groupService.getGroupsByUserId(user.getId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}