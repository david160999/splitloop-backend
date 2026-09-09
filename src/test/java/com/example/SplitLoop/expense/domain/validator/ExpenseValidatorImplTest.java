package com.example.SplitLoop.expense.domain.validator;

import com.example.SplitLoop.expense.domain.entity.*;
import com.example.SplitLoop.expense.exception.*;
import com.example.SplitLoop.group.exception.InsufficientPermissionsException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.util.mother.UserMother;
import com.example.SplitLoop.util.TestData.ExpenseContext;
import com.example.SplitLoop.util.TestData.ExpenseFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseValidatorImplTest {

    private ExpenseValidatorImpl validator;

    private ExpenseContext context;

    @BeforeEach
    void setUp() {
        validator = new ExpenseValidatorImpl();
        context = ExpenseFixture.defaultContext();
    }

    // ============================================================
    // validateRecurringExpense()
    // ============================================================

    @Test
    void shouldValidateRecurringExpense() {

        assertDoesNotThrow(() ->
                validator.validateRecurringExpense(
                        context.getRecurringExpense()));
    }

    @Test
    void shouldThrowWhenRecurringExpenseAmountIsZero() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .amount(BigDecimal.ZERO)
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateRecurringExpense(recurringExpense));
    }

    @Test
    void shouldThrowWhenRecurringExpenseAmountIsNegative() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .amount(BigDecimal.valueOf(-10))
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateRecurringExpense(recurringExpense));
    }

    @Test
    void shouldThrowWhenRecurringExpenseAmountIsNull() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .amount(null)
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateRecurringExpense(recurringExpense));
    }

    @Test
    void shouldThrowWhenFrequencyIsNull() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .frequency(null)
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateRecurringExpense(recurringExpense));
    }

    @Test
    void shouldThrowWhenStartDateIsAfterEndDate() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .startDate(LocalDate.of(2025, 2, 1))
                        .endDate(LocalDate.of(2025, 1, 1))
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateRecurringExpense(recurringExpense));
    }

    @Test
    void shouldAllowNullEndDate() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .endDate(null)
                        .build();

        assertDoesNotThrow(() ->
                validator.validateRecurringExpense(recurringExpense));
    }

    @Test
    void shouldAllowSameStartAndEndDate() {

        LocalDate date = LocalDate.of(2025, 1, 1);

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .startDate(date)
                        .endDate(date)
                        .build();

        assertDoesNotThrow(() ->
                validator.validateRecurringExpense(recurringExpense));
    }

    // ============================================================
    // validateOccurrence()
    // ============================================================

    @Test
    void shouldValidateOccurrence() {

        assertDoesNotThrow(() ->
                validator.validateOccurrence(
                        context.getOccurrence()));
    }

    @Test
    void shouldThrowWhenOccurrenceAmountIsZero() {

        ExpenseOccurrence occurrence =
                context.getOccurrence()
                        .toBuilder()
                        .amount(BigDecimal.ZERO)
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateOccurrence(occurrence));
    }

    @Test
    void shouldThrowWhenOccurrenceAmountIsNegative() {

        ExpenseOccurrence occurrence =
                context.getOccurrence()
                        .toBuilder()
                        .amount(BigDecimal.valueOf(-5))
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateOccurrence(occurrence));
    }

    @Test
    void shouldThrowWhenOccurrenceAmountIsNull() {

        ExpenseOccurrence occurrence =
                context.getOccurrence()
                        .toBuilder()
                        .amount(null)
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateOccurrence(occurrence));
    }


    // ============================================================
// validateParticipants()
// ============================================================

    @Test
    void shouldValidateParticipants() {

        assertDoesNotThrow(() ->
                validator.validateParticipants(
                        List.of(
                                context.getAdmin(),
                                context.getMember()),
                        context.getParticipants()));
    }

    @Test
    void shouldThrowWhenParticipantsAreNull() {

        assertThrows(
                ParticipantsCannotBeEmptyException.class,
                () -> validator.validateParticipants(
                        List.of(
                                context.getAdmin(),
                                context.getMember()),
                        null));
    }

    @Test
    void shouldThrowWhenParticipantsAreEmpty() {

        assertThrows(
                ParticipantsCannotBeEmptyException.class,
                () -> validator.validateParticipants(
                        List.of(
                                context.getAdmin(),
                                context.getMember()),
                        List.of()));
    }

    @Test
    void shouldThrowWhenParticipantIsDuplicated() {

        RecurringExpenseParticipant duplicated =
                context.getParticipants().get(0);

        List<RecurringExpenseParticipant> participants = List.of(
                duplicated,
                duplicated);

        assertThrows(
                DuplicatedParticipantException.class,
                () -> validator.validateParticipants(
                        List.of(context.getAdmin(), context.getMember()), participants));
    }

    @Test
    void shouldThrowWhenParticipantIsNotGroupMember() {

        User outsider = UserMother.user()
                .toBuilder()
                .email("outsider@test.com")
                .username("outsider")
                .build();

        RecurringExpenseParticipant participant =
                context.getParticipants()
                        .get(0)
                        .toBuilder()
                        .user(outsider)
                        .build();

        List<RecurringExpenseParticipant> participants = List.of(
                participant);

        assertThrows(
                UserNotMemberOfGroupException.class,
                () -> validator.validateParticipants(
                        List.of(context.getAdmin(), context.getMember()), participants));
    }

// ============================================================
// validateSplitConfiguration()
// ============================================================

    @Test
    void shouldValidateEqualSplitConfiguration() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .splitType(SplitType.EQUAL)
                        .build();

        assertDoesNotThrow(() ->
                validator.validateSplitConfiguration(
                        recurringExpense,
                        context.getParticipants()));
    }

    @Test
    void shouldValidatePercentageSplitConfiguration() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .splitType(SplitType.PERCENTAGE)
                        .build();

        List<RecurringExpenseParticipant> participants = List.of(

                context.getParticipants()
                        .get(0)
                        .toBuilder()
                        .value(BigDecimal.valueOf(50))
                        .build(),

                context.getParticipants()
                        .get(1)
                        .toBuilder()
                        .value(BigDecimal.valueOf(50))
                        .build());

        assertDoesNotThrow(() ->
                validator.validateSplitConfiguration(
                        recurringExpense,
                        participants));
    }

    @Test
    void shouldThrowWhenPercentageDoesNotSum100() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .splitType(SplitType.PERCENTAGE)
                        .build();

        List<RecurringExpenseParticipant> participants = List.of(

                context.getParticipants()
                        .get(0)
                        .toBuilder()
                        .value(BigDecimal.valueOf(40))
                        .build(),

                context.getParticipants()
                        .get(1)
                        .toBuilder()
                        .value(BigDecimal.valueOf(40))
                        .build());

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateSplitConfiguration(
                        recurringExpense,
                        participants));
    }

    @Test
    void shouldThrowWhenPercentageExceeds100() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .splitType(SplitType.PERCENTAGE)
                        .build();

        List<RecurringExpenseParticipant> participants = List.of(

                context.getParticipants()
                        .get(0)
                        .toBuilder()
                        .value(BigDecimal.valueOf(60))
                        .build(),

                context.getParticipants()
                        .get(1)
                        .toBuilder()
                        .value(BigDecimal.valueOf(60))
                        .build());

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateSplitConfiguration(
                        recurringExpense,
                        participants));
    }

    @Test
    void shouldValidateFixedSplitConfiguration() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .splitType(SplitType.FIXED)
                        .amount(BigDecimal.valueOf(20))
                        .build();

        List<RecurringExpenseParticipant> participants = List.of(

                context.getParticipants()
                        .get(0)
                        .toBuilder()
                        .value(BigDecimal.TEN)
                        .build(),

                context.getParticipants()
                        .get(1)
                        .toBuilder()
                        .value(BigDecimal.TEN)
                        .build());

        assertDoesNotThrow(() ->
                validator.validateSplitConfiguration(
                        recurringExpense,
                        participants));
    }

    @Test
    void shouldThrowWhenFixedAmountsDoNotMatchExpenseAmount() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .splitType(SplitType.FIXED)
                        .amount(BigDecimal.valueOf(20))
                        .build();

        List<RecurringExpenseParticipant> participants = List.of(

                context.getParticipants()
                        .get(0)
                        .toBuilder()
                        .value(BigDecimal.valueOf(5))
                        .build(),

                context.getParticipants()
                        .get(1)
                        .toBuilder()
                        .value(BigDecimal.valueOf(5))
                        .build());

        assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateSplitConfiguration(
                        recurringExpense,
                        participants));
    }

    @Test
    void shouldValidateFixedSplitWithThreeParticipants() {

        RecurringExpense recurringExpense =
                context.getRecurringExpense()
                        .toBuilder()
                        .splitType(SplitType.FIXED)
                        .amount(BigDecimal.valueOf(30))
                        .build();

        User thirdUser = UserMother.user()
                .toBuilder()
                .username("third")
                .email("third@test.com")
                .build();

        List<RecurringExpenseParticipant> participants = List.of(

                context.getParticipants()
                        .get(0)
                        .toBuilder()
                        .value(BigDecimal.TEN)
                        .build(),

                context.getParticipants()
                        .get(1)
                        .toBuilder()
                        .value(BigDecimal.TEN)
                        .build(),

                RecurringExpenseParticipant.builder()
                        .recurringExpense(recurringExpense)
                        .user(thirdUser)
                        .value(BigDecimal.TEN)
                        .build());

        assertDoesNotThrow(() ->
                validator.validateSplitConfiguration(
                        recurringExpense,
                        participants));
    }

    // ============================================================
// validateCanCancel()
// ============================================================

    @Test
    void shouldAllowAdminToCancelPendingOccurrence() {

        assertDoesNotThrow(() ->
                validator.validateCanCancel(
                        context.getOccurrence(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenMemberIsNotAdminToCancel() {

        assertThrows(
                InsufficientPermissionsException.class,
                () -> validator.validateCanCancel(
                        context.getOccurrence(),
                        context.getMember()));
    }

    @Test
    void shouldThrowWhenOccurrenceAlreadyCancelled() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.CANCELLED)
                .build();

        assertThrows(
                OccurrenceAlreadyCancelledException.class,
                () -> validator.validateCanCancel(
                        occurrence,
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenOccurrenceAlreadyPaid() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PAID)
                .build();

        assertThrows(
                CannotModifyPaidOccurrenceException.class,
                () -> validator.validateCanCancel(
                        occurrence,
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenOccurrencePartiallyPaid() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PARTIALLY_PAID)
                .build();

        assertThrows(
                CannotModifyPartiallyPaidOccurrenceException.class,
                () -> validator.validateCanCancel(
                        occurrence,
                        context.getAdmin()));
    }

// ============================================================
// validateCanChangePaidBy()
// ============================================================

    @Test
    void shouldAllowChangingPaidBy() {

        assertDoesNotThrow(() ->
                validator.validateCanChangePaidBy(
                        context.getOccurrence(),
                        context.getSecondUser(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenMemberIsNotAdminToChangePaidBy() {

        assertThrows(
                InsufficientPermissionsException.class,
                () -> validator.validateCanChangePaidBy(
                        context.getOccurrence(),
                        context.getSecondUser(),
                        context.getMember()));
    }

    @Test
    void shouldThrowWhenNewPaidByIsTheSame() {

        assertThrows(
                SamePaidByException.class,
                () -> validator.validateCanChangePaidBy(
                        context.getOccurrence(),
                        context.getOwner(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenChangingPaidByOfPaidOccurrence() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PAID)
                .build();

        assertThrows(
                CannotModifyPaidOccurrenceException.class,
                () -> validator.validateCanChangePaidBy(
                        occurrence,
                        context.getSecondUser(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenChangingPaidByOfPartiallyPaidOccurrence() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PARTIALLY_PAID)
                .build();

        assertThrows(
                CannotModifyPartiallyPaidOccurrenceException.class,
                () -> validator.validateCanChangePaidBy(
                        occurrence,
                        context.getSecondUser(),
                        context.getAdmin()));
    }

    @Test
    void shouldAllowChangingPaidByWhenOccurrenceIsPending() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PENDING)
                .build();

        assertDoesNotThrow(() ->
                validator.validateCanChangePaidBy(
                        occurrence,
                        context.getSecondUser(),
                        context.getAdmin()));
    }

// ============================================================
// validateCanPause()
// ============================================================

    @Test
    void shouldAllowPauseActiveRecurringExpense() {

        assertDoesNotThrow(() ->
                validator.validateCanPause(
                        context.getRecurringExpense(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenRecurringExpenseAlreadyPaused() {

        RecurringExpense recurringExpense = context.getRecurringExpense()
                .toBuilder()
                .status(RecurringExpenseStatus.PAUSED)
                .build();

        assertThrows(
                RecurringExpenseAlreadyPausedException.class,
                () -> validator.validateCanPause(
                        recurringExpense,
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenPausingWithoutAdminPermissions() {

        assertThrows(
                InsufficientPermissionsException.class,
                () -> validator.validateCanPause(
                        context.getRecurringExpense(),
                        context.getMember()));
    }

// ============================================================
// validateCanResume()
// ============================================================

    @Test
    void shouldAllowResumePausedRecurringExpense() {

        RecurringExpense recurringExpense = context.getRecurringExpense()
                .toBuilder()
                .status(RecurringExpenseStatus.PAUSED)
                .build();

        assertDoesNotThrow(() ->
                validator.validateCanResume(
                        recurringExpense,
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenRecurringExpenseAlreadyActive() {

        assertThrows(
                RecurringExpenseAlreadyActiveException.class,
                () -> validator.validateCanResume(
                        context.getRecurringExpense(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenResumingWithoutAdminPermissions() {

        RecurringExpense recurringExpense = context.getRecurringExpense()
                .toBuilder()
                .status(RecurringExpenseStatus.PAUSED)
                .build();

        assertThrows(
                InsufficientPermissionsException.class,
                () -> validator.validateCanResume(
                        recurringExpense,
                        context.getMember()));
    }

// ============================================================
// validateCanReopen()
// ============================================================

    @Test
    void shouldAllowReopenCancelledOccurrence() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.CANCELLED)
                .build();

        assertDoesNotThrow(() ->
                validator.validateCanReopen(
                        occurrence,
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenOccurrenceIsNotCancelled() {

        assertThrows(
                OccurrenceNotCancelledException.class,
                () -> validator.validateCanReopen(
                        context.getOccurrence(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenReopeningWithoutAdminPermissions() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.CANCELLED)
                .build();

        assertThrows(
                InsufficientPermissionsException.class,
                () -> validator.validateCanReopen(
                        occurrence,
                        context.getMember()));
    }

// ============================================================
// validateCanCancelFuture()
// ============================================================

    @Test
    void shouldAllowCancelFutureOccurrence() {

        assertDoesNotThrow(() ->
                validator.validateCanCancelFuture(
                        context.getOccurrence(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenFutureOccurrenceAlreadyCancelled() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.CANCELLED)
                .build();

        assertThrows(
                OccurrenceAlreadyCancelledException.class,
                () -> validator.validateCanCancelFuture(
                        occurrence,
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenCancellingPaidFutureOccurrence() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PAID)
                .build();

        assertThrows(
                CannotModifyPaidOccurrenceException.class,
                () -> validator.validateCanCancelFuture(
                        occurrence,
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenCancellingPartiallyPaidFutureOccurrence() {

        ExpenseOccurrence occurrence = context.getOccurrence()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PARTIALLY_PAID)
                .build();

        assertThrows(
                CannotModifyPartiallyPaidOccurrenceException.class,
                () -> validator.validateCanCancelFuture(
                        occurrence,
                        context.getAdmin()));
    }

// ============================================================
// validateCanDelete()
// ============================================================

    @Test
    void shouldAllowDeleteActiveRecurringExpense() {

        assertDoesNotThrow(() ->
                validator.validateCanDelete(
                        context.getRecurringExpense(),
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenRecurringExpenseIsInactive() {

        RecurringExpense recurringExpense = context.getRecurringExpense()
                .toBuilder()
                .status(RecurringExpenseStatus.PAUSED)
                .build();

        assertThrows(
                RecurringExpenseInactiveException.class,
                () -> validator.validateCanDelete(
                        recurringExpense,
                        context.getAdmin()));
    }

    @Test
    void shouldThrowWhenDeletingWithoutAdminPermissions() {

        assertThrows(
                InsufficientPermissionsException.class,
                () -> validator.validateCanDelete(
                        context.getRecurringExpense(),
                        context.getMember()));
    }
}
