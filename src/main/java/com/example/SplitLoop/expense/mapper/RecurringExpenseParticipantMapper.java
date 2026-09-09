package com.example.SplitLoop.expense.mapper;

import com.example.SplitLoop.expense.controller.request.ParticipantRequest;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecurringExpenseParticipantMapper {

    private final UserRepository userRepository;

    public List<RecurringExpenseParticipant> toEntities(
            List<ParticipantRequest> requests) {

        List<UUID> ids = requests.stream()
                .map(ParticipantRequest::getUserId)
                .toList();

        Map<UUID, User> users = userRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        User::getId,
                        Function.identity()));

        return requests.stream()
                .map(request -> {

                    User user = users.get(request.getUserId());

                    if (user == null) {
                        throw new UserNotFoundException(
                                request.getUserId());
                    }

                    return RecurringExpenseParticipant.builder()
                            .user(user)
                            .value(request.getSplitValue())
                            .build();
                })
                .toList();
    }
}