package com.example.SplitLoop.expense.application.usecase.query;

import com.example.SplitLoop.expense.application.dto.mapper.ExpenseDtoMapper;
import com.example.SplitLoop.expense.application.dto.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetActiveRecurringExpensesUseCase {

    private final GroupRepository groupRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ExpenseDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<RecurringExpenseResponse> execute(UUID groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        return recurringExpenseRepository
                .findByGroupIdAndStatus(
                        group.id(),
                        RecurringExpenseStatus.ACTIVE)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
