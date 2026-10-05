package com.example.SplitLoop.group.application.useCase.query;

import com.example.SplitLoop.group.application.dto.response.GroupSummaryResponse;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import com.example.SplitLoop.group.domain.model.OccurrenceSummaryData;
import com.example.SplitLoop.group.domain.port.BalanceClientPort;
import com.example.SplitLoop.group.domain.port.ExpenseOccurrenceClientPort;
import com.example.SplitLoop.group.domain.port.RecurringExpenseClientPort;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.application.mapper.GroupSummaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetGroupSummaryUseCase {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    // Puertos de salida hacia otros dominios
    private final RecurringExpenseClientPort recurringExpenseClient;
    private final ExpenseOccurrenceClientPort expenseOccurrenceClient;
    private final BalanceClientPort balanceClient;

    private final GroupSummaryMapper groupSummaryMapper;

    @Transactional(readOnly = true)
    public GroupSummaryResponse execute(UUID groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        long membersCount = groupMemberRepository.countByGroupId(groupId);
        int activeRecurring = recurringExpenseClient.getActiveCountByGroupId(groupId);
        OccurrenceSummaryData occurrenceSummary = expenseOccurrenceClient.getOccurrenceSummaryByGroupId(groupId);
        BigDecimal pendingAmount = balanceClient.getPendingAmountByGroupId(groupId);

        return groupSummaryMapper.toResponse(
                group,
                membersCount,
                activeRecurring,
                occurrenceSummary,
                pendingAmount
        );
    }
}