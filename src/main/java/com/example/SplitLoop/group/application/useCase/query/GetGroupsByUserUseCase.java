package com.example.SplitLoop.group.application.useCase.query;

import com.example.SplitLoop.group.application.dto.query.GetGroupsByUserQuery;
import com.example.SplitLoop.group.application.dto.response.GroupResponse;
import com.example.SplitLoop.group.application.mapper.GroupDtoMapper;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetGroupsByUserUseCase {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<GroupResponse> execute(GetGroupsByUserQuery request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        return groupRepository.findAllByUserId(user.id())
                .stream()
                .map(mapper::toGroupResponse)
                .toList();
    }
}