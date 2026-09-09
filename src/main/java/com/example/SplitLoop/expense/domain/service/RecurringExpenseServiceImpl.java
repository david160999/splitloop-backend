package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.validator.ExpenseValidator;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringExpenseServiceImpl implements RecurringExpenseService {

    private static final int GENERATION_HORIZON_MONTHS = 3;

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final RecurringExpenseParticipantRepository participantRepository;

    private final ExpenseValidator validator;
    private final ExpenseOccurrenceService occurrenceService;

    @Override
    public RecurringExpense createRecurringExpense(
            RecurringExpense recurringExpense,
            List<RecurringExpenseParticipant> participants,
            List<GroupMember> groupMembers) {

        validator.validateRecurringExpense(recurringExpense);
        validator.validateParticipants(groupMembers, participants);
        validator.validateSplitConfiguration(recurringExpense, participants);

        RecurringExpense savedExpense = recurringExpenseRepository.save(recurringExpense);

        participants.forEach(participant ->
                participant.setRecurringExpense(savedExpense));

        participantRepository.saveAll(participants);

        // Sincronizar la relación en memoria
        savedExpense.getParticipants().clear();
        savedExpense.getParticipants().addAll(participants);

        occurrenceService.generatePendingOccurrences(
                savedExpense,
                savedExpense.getStartDate());

        return savedExpense;
    }

    @Override
    public RecurringExpense updateRecurringExpense(
            RecurringExpense recurringExpense,
            List<RecurringExpenseParticipant> participants,
            List<GroupMember> groupMembers) {

        validator.validateRecurringExpense(recurringExpense);
        validator.validateParticipants(groupMembers, participants);
        validator.validateSplitConfiguration(recurringExpense, participants);

        RecurringExpense savedExpense = recurringExpenseRepository.save(recurringExpense);

        participantRepository.deleteByRecurringExpense(savedExpense);

        participants.forEach(participant ->
                participant.setRecurringExpense(savedExpense));

        participantRepository.saveAll(participants);

        // Sincronizar la relación en memoria
        savedExpense.getParticipants().clear();
        savedExpense.getParticipants().addAll(participants);

        occurrenceService.updateFutureOccurrences(savedExpense);

        return savedExpense;
    }

    @Override
    public void deleteRecurringExpense(RecurringExpense recurringExpense, GroupMember member) {

        validator.validateCanDelete(recurringExpense, member);

        recurringExpense.setStatus(RecurringExpenseStatus.DELETED);

        recurringExpenseRepository.save(recurringExpense);

        occurrenceService.cancelFutureOccurrences(
                recurringExpense,
                member);
    }

    @Override
    public void pause(RecurringExpense recurringExpense, GroupMember member) {

        validator.validateCanPause(recurringExpense, member);

        recurringExpense.setStatus(RecurringExpenseStatus.PAUSED);

        recurringExpenseRepository.save(recurringExpense);
    }

    @Override
    public void resume(RecurringExpense recurringExpense, GroupMember member) {

        validator.validateCanResume(recurringExpense, member);

        recurringExpense.setStatus(RecurringExpenseStatus.ACTIVE);

        recurringExpenseRepository.save(recurringExpense);

        occurrenceService.generatePendingOccurrences(
                recurringExpense,
                LocalDate.now().plusMonths(GENERATION_HORIZON_MONTHS));
    }

    @Override
    public RecurringExpense duplicateRecurringExpense(
            RecurringExpense original,
            List<RecurringExpenseParticipant> participants,
            User createdBy,
            LocalDate startDate,
            List<GroupMember> groupMembers) {

        RecurringExpense duplicate = RecurringExpense.builder()
                .group(original.getGroup())
                .name(original.getName())
                .description(original.getDescription())
                .amount(original.getAmount())
                .frequency(original.getFrequency())
                .splitType(original.getSplitType())
                .startDate(startDate)
                .endDate(original.getEndDate())
                .paidBy(original.getPaidBy())
                .createdBy(createdBy)
                .status(RecurringExpenseStatus.ACTIVE)
                .build();

        return createRecurringExpense(
                duplicate,
                participants,
                groupMembers);
    }

    @Override
    public void completeIfExpired(RecurringExpense recurringExpense, LocalDate today) {

        if (recurringExpense.getEndDate() != null && today.isAfter(recurringExpense.getEndDate())) {

            recurringExpense.setStatus(RecurringExpenseStatus.COMPLETED);
            recurringExpenseRepository.save(recurringExpense);
        }
    }
}