package com.example.SplitLoop.expense.application.query;

import com.example.SplitLoop.expense.controller.query.GetRecurringExpensesQuery;
import com.example.SplitLoop.expense.controller.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.mapper.RecurringExpenseMapper;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRecurringExpensesUseCase {

    private final GroupRepository groupRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final RecurringExpenseMapper mapper;

    @Transactional(readOnly = true)
    public List<RecurringExpenseResponse> execute(GetRecurringExpensesQuery request) {

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException(request.getGroupId()));

        return recurringExpenseRepository
                .findByGroupAndStatus(
                        group,
                        request.getStatus())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
