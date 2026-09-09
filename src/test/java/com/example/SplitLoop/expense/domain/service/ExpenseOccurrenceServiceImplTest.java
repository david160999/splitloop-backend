package com.example.SplitLoop.expense.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.SplitLoop.expense.domain.entity.*;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.validator.ExpenseValidator;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.util.mother.ExpenseOccurrenceMother;
import com.example.SplitLoop.util.mother.UserMother;
import com.example.SplitLoop.util.TestData.ExpenseContext;
import com.example.SplitLoop.util.TestData.ExpenseFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExpenseOccurrenceServiceImplTest {
    @Mock
    private ExpenseOccurrenceRepository occurrenceRepository;

    @Mock
    private ExpenseOccurrenceSplitService splitService;

    @Mock
    private ExpenseValidator validator;

    @InjectMocks
    private ExpenseOccurrenceServiceImpl service;

    private User user;
    private Group group;
    private GroupMember member;
    private RecurringExpense recurringExpense;
    private ExpenseOccurrence occurrence;

    private ExpenseContext context;

    @BeforeEach
    void setUp() {
        context = ExpenseFixture.defaultContext();

        user = context.getOwner();
        group = context.getGroup();
        member = context.getAdmin();
        recurringExpense = context.getRecurringExpense();
        occurrence = context.getOccurrence();
    }



    // ==========================================================
    // updateOccurrence
    // ==========================================================


    @Test
    void shouldUpdateOccurrence() {

        when(occurrenceRepository.save(occurrence))
                .thenReturn(occurrence);

        ExpenseOccurrence result = service.updateOccurrence(occurrence);

        verify(validator).validateOccurrence(occurrence);
        verify(occurrenceRepository).save(occurrence);
        verify(splitService).recalculateParticipants(occurrence);

        assertSame(occurrence, result);
    }

    @Test
    void shouldThrowExceptionWhenUpdateOccurrenceValidationFails() {

        doThrow(new IllegalArgumentException())
                .when(validator)
                .validateOccurrence(occurrence);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateOccurrence(occurrence));

        verify(occurrenceRepository, never()).save(any());
        verify(splitService, never()).recalculateParticipants(any());
    }

    // ==========================================================
    // cancelOccurrence
    // ==========================================================

    @Test
    void shouldCancelOccurrence() {

        service.cancelOccurrence(occurrence, member);

        verify(validator).validateCanCancel(occurrence, member);

        assertEquals(
                ExpenseOccurrenceStatus.CANCELLED,
                occurrence.getStatus());

        verify(occurrenceRepository).save(occurrence);
    }

    @Test
    void shouldThrowExceptionWhenCancelValidationFails() {

        doThrow(new IllegalArgumentException())
                .when(validator)
                .validateCanCancel(occurrence, member);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cancelOccurrence(occurrence, member));

        verify(occurrenceRepository, never()).save(any());
    }

    // ==========================================================
    // reopen
    // ==========================================================

    @Test
    void shouldReopenOccurrence() {

        occurrence.setStatus(ExpenseOccurrenceStatus.CANCELLED);

        service.reopen(occurrence, member);

        verify(validator).validateCanReopen(occurrence, member);

        assertEquals(
                ExpenseOccurrenceStatus.PENDING,
                occurrence.getStatus());

        verify(occurrenceRepository).save(occurrence);
        verify(splitService).updateOccurrenceStatus(occurrence);
    }

    @Test
    void shouldThrowExceptionWhenReopenValidationFails() {

        doThrow(new IllegalArgumentException())
                .when(validator)
                .validateCanReopen(occurrence, member);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.reopen(occurrence, member));

        verify(occurrenceRepository, never()).save(any());
        verify(splitService, never()).updateOccurrenceStatus(any());
    }

    // ==========================================================
    // changePaidBy
    // ==========================================================

    @Test
    void shouldChangePaidBy() {

        User newUser = UserMother.anotherUser();

        when(occurrenceRepository.save(occurrence)).thenReturn(occurrence);

        ExpenseOccurrence result =service.changePaidBy(occurrence,newUser,member);

        verify(validator).validateCanChangePaidBy(occurrence,newUser,member);

        assertEquals(newUser, occurrence.getPaidBy());

        verify(occurrenceRepository).save(occurrence);
        verify(splitService).recalculateParticipants(occurrence);

        assertSame(occurrence, result);
    }

    @Test
    void shouldThrowExceptionWhenChangePaidByValidationFails() {

        User newUser = UserMother.anotherUser();

        doThrow(new IllegalArgumentException())
                .when(validator)
                .validateCanChangePaidBy(
                        occurrence,
                        newUser,
                        member);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.changePaidBy(
                        occurrence,
                        newUser,
                        member));

        verify(occurrenceRepository, never()).save(any());
        verify(splitService, never()).recalculateParticipants(any());
    }


    // ==========================================================
    // updateFutureOccurrences
    // ==========================================================

    @Test
    void shouldUpdateFutureOccurrences() {

        ExpenseOccurrence future1 = ExpenseOccurrenceMother.pending(recurringExpense);
        ExpenseOccurrence future2 = ExpenseOccurrenceMother.pending(recurringExpense);

        recurringExpense.setName("Spotify");
        recurringExpense.setAmount(BigDecimal.valueOf(35));

        User newPaidBy = UserMother.anotherUser();
        recurringExpense.setPaidBy(newPaidBy);

        when(occurrenceRepository.findFutureOccurrences(
                eq(recurringExpense),
                any(LocalDate.class)))
                .thenReturn(List.of(future1, future2));

        service.updateFutureOccurrences(recurringExpense);

        assertEquals("Spotify", future1.getName());
        assertEquals(BigDecimal.valueOf(35), future1.getAmount());
        assertEquals(newPaidBy, future1.getPaidBy());

        assertEquals("Spotify", future2.getName());
        assertEquals(BigDecimal.valueOf(35), future2.getAmount());
        assertEquals(newPaidBy, future2.getPaidBy());

        verify(splitService).recalculateParticipants(future1);
        verify(splitService).recalculateParticipants(future2);

        verify(occurrenceRepository).saveAll(List.of(future1, future2));
    }

    @Test
    void shouldDoNothingWhenNoFutureOccurrencesExist() {

        when(occurrenceRepository.findFutureOccurrences(
                eq(recurringExpense),
                any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        service.updateFutureOccurrences(recurringExpense);

        verify(splitService, never())
                .recalculateParticipants(any());

        verify(occurrenceRepository)
                .saveAll(List.of());
    }

    // ==========================================================
    // cancelFutureOccurrences
    // ==========================================================

    @Test
    void shouldCancelFutureOccurrences() {

        ExpenseOccurrence future1 = ExpenseOccurrenceMother.pending(recurringExpense);
        ExpenseOccurrence future2 = ExpenseOccurrenceMother.pending(recurringExpense);

        when(occurrenceRepository.findFutureOccurrences(
                eq(recurringExpense),
                any(LocalDate.class)))
                .thenReturn(List.of(future1, future2));

        service.cancelFutureOccurrences(recurringExpense, member);

        assertEquals(
                ExpenseOccurrenceStatus.CANCELLED,
                future1.getStatus());

        assertEquals(
                ExpenseOccurrenceStatus.CANCELLED,
                future2.getStatus());

        verify(validator).validateCanCancelFuture(future1, member);
        verify(validator).validateCanCancelFuture(future2, member);

        verify(occurrenceRepository)
                .saveAll(List.of(future1, future2));
    }

    @Test
    void shouldNotCancelWhenValidatorFails() {

        ExpenseOccurrence future = ExpenseOccurrenceMother.pending(recurringExpense);

        when(occurrenceRepository.findFutureOccurrences(
                eq(recurringExpense),
                any(LocalDate.class)))
                .thenReturn(List.of(future));

        doThrow(new IllegalArgumentException())
                .when(validator)
                .validateCanCancelFuture(future, member);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.cancelFutureOccurrences(
                        recurringExpense,
                        member));

        assertEquals(
                ExpenseOccurrenceStatus.PENDING,
                future.getStatus());

        verify(occurrenceRepository, never())
                .saveAll(any());
    }

    @Test
    void shouldNotCancelAnythingWhenThereAreNoFutureOccurrences() {

        when(occurrenceRepository.findFutureOccurrences(
                eq(recurringExpense),
                any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        service.cancelFutureOccurrences(recurringExpense, member);

        verify(validator, never())
                .validateCanCancelFuture(any(), any());

        verify(occurrenceRepository)
                .saveAll(List.of());
    }

    @Test
    void shouldValidateEveryFutureOccurrenceBeforeSaving() {

        ExpenseOccurrence future1 = ExpenseOccurrenceMother.pending(recurringExpense);
        ExpenseOccurrence future2 = ExpenseOccurrenceMother.pending(recurringExpense);
        ExpenseOccurrence future3 = ExpenseOccurrenceMother.pending(recurringExpense);

        when(occurrenceRepository.findFutureOccurrences(
                eq(recurringExpense),
                any(LocalDate.class)))
                .thenReturn(List.of(future1, future2, future3));

        service.cancelFutureOccurrences(recurringExpense, member);

        verify(validator).validateCanCancelFuture(future1, member);
        verify(validator).validateCanCancelFuture(future2, member);
        verify(validator).validateCanCancelFuture(future3, member);

        verify(occurrenceRepository)
                .saveAll(List.of(future1, future2, future3));
    }


    // ==========================================================
    // generatePendingOccurrences
    // ==========================================================

    @Test
    void shouldGenerateMonthlyOccurrencesFromStartDate() {

        recurringExpense.setFrequency(Frequency.MONTHLY);
        recurringExpense.setStartDate(LocalDate.of(2025, 1, 1));

        when(occurrenceRepository
                .findTopByRecurringExpenseOrderByDueDateDesc(recurringExpense))
                .thenReturn(Optional.empty());

        when(occurrenceRepository.save(any(ExpenseOccurrence.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<ExpenseOccurrence> result =
                service.generatePendingOccurrences(
                        recurringExpense,
                        LocalDate.of(2025, 3, 1));

        assertEquals(3, result.size());

        assertEquals(
                LocalDate.of(2025, 1, 1),
                result.get(0).getDueDate());

        assertEquals(
                LocalDate.of(2025, 2, 1),
                result.get(1).getDueDate());

        assertEquals(
                LocalDate.of(2025, 3, 1),
                result.get(2).getDueDate());

        verify(occurrenceRepository, times(3))
                .save(any());

        verify(splitService, times(3))
                .recalculateParticipants(any());

        verify(validator, times(3))
                .validateOccurrence(any());
    }

    @Test
    void shouldGenerateDailyOccurrences() {

        recurringExpense.setFrequency(Frequency.DAILY);
        recurringExpense.setStartDate(LocalDate.of(2025, 1, 1));

        when(occurrenceRepository
                .findTopByRecurringExpenseOrderByDueDateDesc(recurringExpense))
                .thenReturn(Optional.empty());

        when(occurrenceRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        List<ExpenseOccurrence> result =
                service.generatePendingOccurrences(
                        recurringExpense,
                        LocalDate.of(2025, 1, 3));

        assertEquals(3, result.size());

        assertEquals(
                LocalDate.of(2025, 1, 1),
                result.get(0).getDueDate());

        assertEquals(
                LocalDate.of(2025, 1, 2),
                result.get(1).getDueDate());

        assertEquals(
                LocalDate.of(2025, 1, 3),
                result.get(2).getDueDate());

        assertEquals(
                result.get(0).getDueDate(),
                result.get(0).getPeriodEnd());
    }

    @Test
    void shouldGenerateWeeklyOccurrences() {

        recurringExpense.setFrequency(Frequency.WEEKLY);

        when(occurrenceRepository
                .findTopByRecurringExpenseOrderByDueDateDesc(recurringExpense))
                .thenReturn(Optional.empty());

        when(occurrenceRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        List<ExpenseOccurrence> result =
                service.generatePendingOccurrences(
                        recurringExpense,
                        LocalDate.of(2025, 1, 15));

        assertEquals(3, result.size());

        assertEquals(
                LocalDate.of(2025, 1, 7),
                result.get(0).getPeriodEnd());

        assertEquals(
                LocalDate.of(2025, 1, 14),
                result.get(1).getPeriodEnd());

        assertEquals(
                LocalDate.of(2025, 1, 21),
                result.get(2).getPeriodEnd());
    }

    @Test
    void shouldGenerateYearlyOccurrences() {

        recurringExpense.setFrequency(Frequency.YEARLY);

        when(occurrenceRepository
                .findTopByRecurringExpenseOrderByDueDateDesc(recurringExpense))
                .thenReturn(Optional.empty());

        when(occurrenceRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        List<ExpenseOccurrence> result =
                service.generatePendingOccurrences(
                        recurringExpense,
                        LocalDate.of(2027, 1, 1));

        assertEquals(3, result.size());

        assertEquals(
                LocalDate.of(2025, 12, 31),
                result.get(0).getPeriodEnd());

        assertEquals(
                LocalDate.of(2026, 12, 31),
                result.get(1).getPeriodEnd());

        assertEquals(
                LocalDate.of(2027, 12, 31),
                result.get(2).getPeriodEnd());
    }

    @Test
    void shouldGenerateOnlyMissingOccurrences() {

        ExpenseOccurrence last = ExpenseOccurrenceMother.pending(recurringExpense);
        last.setDueDate(LocalDate.of(2025, 2, 1));

        recurringExpense.setFrequency(Frequency.MONTHLY);

        when(occurrenceRepository
                .findTopByRecurringExpenseOrderByDueDateDesc(recurringExpense))
                .thenReturn(Optional.of(last));

        when(occurrenceRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        List<ExpenseOccurrence> result =
                service.generatePendingOccurrences(
                        recurringExpense,
                        LocalDate.of(2025, 4, 1));

        assertEquals(2, result.size());

        assertEquals(
                LocalDate.of(2025, 3, 1),
                result.get(0).getDueDate());

        assertEquals(
                LocalDate.of(2025, 4, 1),
                result.get(1).getDueDate());
    }

    @Test
    void shouldReturnEmptyListWhenNothingNeedsToBeGenerated() {

        ExpenseOccurrence last = ExpenseOccurrenceMother.pending(recurringExpense);
        last.setDueDate(LocalDate.of(2025, 5, 1));

        recurringExpense.setFrequency(Frequency.MONTHLY);

        when(occurrenceRepository
                .findTopByRecurringExpenseOrderByDueDateDesc(recurringExpense))
                .thenReturn(Optional.of(last));

        List<ExpenseOccurrence> result =
                service.generatePendingOccurrences(
                        recurringExpense,
                        LocalDate.of(2025, 5, 15));

        assertTrue(result.isEmpty());

        verify(occurrenceRepository, never()).save(any());
        verify(splitService, never()).recalculateParticipants(any());
    }

    @Test
    void shouldStopGeneratingWhenValidatorFails() {

        recurringExpense.setFrequency(Frequency.MONTHLY);

        when(occurrenceRepository
                .findTopByRecurringExpenseOrderByDueDateDesc(recurringExpense))
                .thenReturn(Optional.empty());

        doThrow(new IllegalArgumentException())
                .when(validator)
                .validateOccurrence(any());

        assertThrows(
                IllegalArgumentException.class,
                () -> service.generatePendingOccurrences(
                        recurringExpense,
                        LocalDate.of(2025, 2, 1)));

        verify(occurrenceRepository, never()).save(any());
        verify(splitService, never()).recalculateParticipants(any());
    }
}





































































































