package com.example.SplitLoop.expense.application.command;

import com.example.SplitLoop.expense.controller.command.UpdateOccurrenceCommand;
import com.example.SplitLoop.expense.controller.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.expense.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.expense.mapper.ExpenseOccurrenceMapper;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOccurrenceUseCase {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final UserRepository userRepository;

    private final ExpenseOccurrenceService occurrenceService;

    private final ExpenseOccurrenceMapper mapper;

    @Transactional
    public ExpenseOccurrenceResponse execute(UUID occurrenceId, UpdateOccurrenceCommand request) {

        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        User paidBy = userRepository.findById(request.getPaidById())
                .orElseThrow(() -> new UserNotFoundException(request.getPaidById()));

        mapper.updateEntity(
                request,
                occurrence,
                paidBy);

        ExpenseOccurrence updated = occurrenceService.updateOccurrence(occurrence);

        return mapper.toResponse(updated);
    }
}