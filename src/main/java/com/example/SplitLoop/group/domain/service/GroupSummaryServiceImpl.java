package com.example.SplitLoop.group.domain.service;

import com.example.SplitLoop.balance.domain.service.BalanceService;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.modelo.GroupSummary;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupSummaryServiceImpl implements GroupSummaryService {

    private final GroupMemberRepository groupMemberRepository;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final ExpenseOccurrenceSplitRepository splitRepository;
    private final BalanceService balanceService;

    @Override
    @Transactional(readOnly = true)
    public GroupSummary getSummary(Group group) {

        int members = groupMemberRepository.countByGroup(group);

        int activeRecurringExpenses = recurringExpenseRepository.countByGroupAndStatus(
                group,
                RecurringExpenseStatus.ACTIVE);

        int pendingOccurrences = occurrenceRepository.countByGroupAndStatus(
                group,
                ExpenseOccurrenceStatus.PENDING);

        BigDecimal totalExpenses = occurrenceRepository.sumAmountByGroup(group);

        List<ExpenseOccurrenceSplit> splits = splitRepository.findByOccurrenceGroupId(group.getId());

        BigDecimal pendingAmount = balanceService.calculateBalances(splits)
                .stream()
                .filter(balance -> balance.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(balance -> balance.getAmount().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate nextDueDate = occurrenceRepository.findNextDueDate(
                group,
                ExpenseOccurrenceStatus.PENDING);

        return GroupSummary.builder()
                .group(group)
                .members(members)
                .activeRecurringExpenses(activeRecurringExpenses)
                .pendingOccurrences(pendingOccurrences)
                .totalExpenses(totalExpenses)
                .pendingAmount(pendingAmount)
                .nextDueDate(nextDueDate)
                .build();
    }
}
