package com.example.SplitLoop.expense.domain.validator;

import com.example.SplitLoop.expense.domain.entity.*;
import com.example.SplitLoop.expense.exception.*;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.exception.InsufficientPermissionsException;
import com.example.SplitLoop.user.domain.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseValidatorImpl implements ExpenseValidator {

    @Override
    public void validateRecurringExpense(RecurringExpense recurringExpense) {

        validateAmount(recurringExpense.getAmount());
        validateDates(
                recurringExpense.getStartDate(),
                recurringExpense.getEndDate());
        validateFrequency(recurringExpense.getFrequency());
    }

    @Override
    public void validateParticipants(
            List<GroupMember> groupMembers,
            List<RecurringExpenseParticipant> participants) {

        if (participants == null || participants.isEmpty()) {
            throw new ParticipantsCannotBeEmptyException();
        }

        Set<UUID> participantIds = new HashSet<>();

        Set<UUID> memberIds = groupMembers.stream()
                .map(groupMember -> groupMember.getUser().getId())
                .collect(Collectors.toSet());

        for (RecurringExpenseParticipant participant : participants) {

            UUID userId = participant.getUser().getId();

            if (!participantIds.add(userId)) {
                throw new DuplicatedParticipantException();
            }

            if (!memberIds.contains(userId)) {
                throw new UserNotMemberOfGroupException(userId);
            }
        }
    }

    @Override
    public void validateOccurrence(ExpenseOccurrence occurrence) {

        validateAmount(occurrence.getAmount());
    }

    @Override
    public void validateSplitConfiguration(
            RecurringExpense recurringExpense,
            List<RecurringExpenseParticipant> participants) {

        switch (recurringExpense.getSplitType()) {

            case EQUAL -> {
                // nada
            }

            case PERCENTAGE -> {

                BigDecimal total = participants.stream()
                        .map(RecurringExpenseParticipant::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
                    throw new IllegalArgumentException(
                            "Percentages must sum 100.");
                }

            }

            case FIXED -> {

                BigDecimal total = participants.stream()
                        .map(RecurringExpenseParticipant::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (total.compareTo(recurringExpense.getAmount()) != 0) {
                    throw new IllegalArgumentException(
                            "Fixed amounts must equal expense amount.");
                }

            }

        }

    }

    @Override
    public void validateCanCancel(ExpenseOccurrence occurrence, GroupMember member) {

        validateIsAdmin(member);

        if (occurrence.getStatus() == ExpenseOccurrenceStatus.CANCELLED) {
            throw new OccurrenceAlreadyCancelledException();
        }

        validateOccurrenceCanBeModified(occurrence);

    }

    @Override
    public void validateCanChangePaidBy(ExpenseOccurrence occurrence, User newPaidBy, GroupMember member) {

        validateIsAdmin(member);

        validateOccurrenceCanBeModified(occurrence);

        if (occurrence.getPaidBy().equals(newPaidBy)) {
            throw new SamePaidByException();
        }

    }

    @Override
    public void validateCanPause(RecurringExpense recurringExpense, GroupMember member) {
        validateIsAdmin(member);

        if (!recurringExpense.getStatus().equals(RecurringExpenseStatus.ACTIVE)) {
            throw new RecurringExpenseAlreadyPausedException(recurringExpense.getId());
        }
    }

    @Override
    public void validateCanResume(RecurringExpense recurringExpense, GroupMember member) {
        validateIsAdmin(member);

        if (recurringExpense.getStatus().equals(RecurringExpenseStatus.ACTIVE)) {
            throw new RecurringExpenseAlreadyActiveException(recurringExpense.getId());
        }
    }

    @Override
    public void validateCanReopen(ExpenseOccurrence occurrence, GroupMember member) {
        validateIsAdmin(member);

        if (occurrence.getStatus() != ExpenseOccurrenceStatus.CANCELLED) {
            throw new OccurrenceNotCancelledException(
                    occurrence.getId());
        }

    }

    @Override
    public void validateCanCancelFuture(ExpenseOccurrence occurrence, GroupMember member) {

        if (occurrence.getStatus() == ExpenseOccurrenceStatus.CANCELLED) {
            throw new OccurrenceAlreadyCancelledException();
        }

        validateOccurrenceCanBeModified(occurrence);

    }

    @Override
    public void validateCanDelete(RecurringExpense recurringExpense, GroupMember member) {
        validateIsAdmin(member);

        if (!recurringExpense.getStatus().equals(RecurringExpenseStatus.ACTIVE)) {
            throw new RecurringExpenseInactiveException();
        }
    }

    private void validateIsAdmin(GroupMember member) {
        if (!member.isAdmin()) {
            throw new InsufficientPermissionsException();
        }
    }

    private void validateOccurrenceCanBeModified(ExpenseOccurrence occurrence) {

        if (occurrence.getStatus() == ExpenseOccurrenceStatus.PAID) {
            throw new CannotModifyPaidOccurrenceException(
                    occurrence.getId());
        }

        if (occurrence.getStatus() == ExpenseOccurrenceStatus.PARTIALLY_PAID) {
            throw new CannotModifyPartiallyPaidOccurrenceException();
        }
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount.");
        }

    }

    private void validateDates(LocalDate start, LocalDate end) {

        if (end != null && start.isAfter(end)) {
            throw new IllegalArgumentException("Invalid dates.");
        }

    }

    private void validateFrequency(Frequency frequency) {

        if (frequency == null) {
            throw new IllegalArgumentException("Frequency is required.");
        }

    }

}
