package com.example.SplitLoop.expense.domain.policy;

import com.example.SplitLoop.expense.domain.exception.DuplicatedParticipantException;
import com.example.SplitLoop.expense.domain.exception.ParticipantsCannotBeEmptyException;
import com.example.SplitLoop.expense.domain.exception.UserNotMemberOfGroupException;
import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;
import com.example.SplitLoop.group.domain.model.GroupMember;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ExpenseSplitPolicy {

    private ExpenseSplitPolicy() {
    }

    public static void validateParticipants(
            List<GroupMember> groupMembers,
            List<RecurringExpenseParticipant> participants) {

        if (participants == null || participants.isEmpty()) {
            throw new ParticipantsCannotBeEmptyException();
        }

        Set<UUID> memberUserIds = groupMembers.stream()
                .map(gm -> gm.user().id())
                .collect(Collectors.toSet());

        Set<UUID> participantUserIds = new HashSet<>();

        for (RecurringExpenseParticipant participant : participants) {
            UUID userId = participant.user().id();

            if (!participantUserIds.add(userId)) {
                throw new DuplicatedParticipantException();
            }

            if (!memberUserIds.contains(userId)) {
                throw new UserNotMemberOfGroupException(userId);
            }
        }
    }

    public static void validateSplitConfiguration(
            RecurringExpense recurringExpense,
            List<RecurringExpenseParticipant> participants) {

        if (recurringExpense.splitType() == null) {
            throw new IllegalArgumentException("Split type is required.");
        }

        switch (recurringExpense.splitType()) {
            case EQUAL -> {
                // El reparto equitativo no requiere validación de suma
            }

            case PERCENTAGE -> {
                BigDecimal totalPercentage = participants.stream()
                        .map(RecurringExpenseParticipant::value)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalPercentage.compareTo(BigDecimal.valueOf(100)) != 0) {
                    throw new IllegalArgumentException("Percentages must sum 100.");
                }
            }

            case FIXED -> {
                BigDecimal totalFixedAmount = participants.stream()
                        .map(RecurringExpenseParticipant::value)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalFixedAmount.compareTo(recurringExpense.amount().amount()) != 0) {
                    throw new IllegalArgumentException("Fixed amounts must equal expense amount.");
                }
            }
        }
    }
}