package com.example.SplitLoop.expense.application.command;


import com.example.SplitLoop.expense.controller.command.CreateRecurringExpenseCommand;
import com.example.SplitLoop.expense.controller.request.ParticipantRequest;
import com.example.SplitLoop.expense.controller.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.entity.*;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import com.example.SplitLoop.util.TestData.ExpenseContext;
import com.example.SplitLoop.util.TestData.TestDataFactory;
import com.example.SplitLoop.util.integration.BaseIntegrationTest;
import com.example.SplitLoop.util.security.SecurityTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
class CreateRecurringExpenseUseCaseTest extends BaseIntegrationTest {

    @Autowired
    private CreateRecurringExpenseUseCase useCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private RecurringExpenseRepository recurringExpenseRepository;

    @Autowired
    private RecurringExpenseParticipantRepository participantRepository;

    @Autowired
    private ExpenseOccurrenceRepository occurrenceRepository;

    @Autowired
    private ExpenseOccurrenceSplitRepository splitRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    private ExpenseContext context;

    @BeforeEach
    void setUp() {
        context = testDataFactory.defaultContext();

        SecurityTestUtils.login(context.getOwner());
    }

    @Test
    void shouldCreateRecurringExpense() {

        CreateRecurringExpenseCommand command = createCommand();

        RecurringExpenseResponse response = useCase.execute(command);

        assertNotNull(response);

        assertEquals(1, recurringExpenseRepository.count());

        assertEquals(2, participantRepository.count());

        assertEquals(1, occurrenceRepository.count());

        assertEquals(2, splitRepository.count());

        RecurringExpense recurringExpense = recurringExpenseRepository.findAll().getFirst();

        assertEquals("Netflix", recurringExpense.getName());

        assertEquals(BigDecimal.valueOf(20), recurringExpense.getAmount());

        assertEquals(SplitType.EQUAL, recurringExpense.getSplitType());

        List<ExpenseOccurrence> occurrences = occurrenceRepository.findByRecurringExpense(recurringExpense);

        assertEquals(1, occurrences.size());

        ExpenseOccurrence occurrence = occurrences.getFirst();

        assertEquals(ExpenseOccurrenceStatus.PENDING, occurrence.getStatus());

        List<ExpenseOccurrenceSplit> splits = splitRepository.findByOccurrence(occurrence);

        assertEquals(2, splits.size());

        assertTrue(splits.stream().allMatch(split ->
                split.getAmountOwed().compareTo(BigDecimal.TEN) == 0));
    }

    private CreateRecurringExpenseCommand createCommand() {

        return CreateRecurringExpenseCommand.builder()
                .groupId(context.getGroup().getId())
                .name("Netflix")
                .description("Monthly subscription")
                .amount(BigDecimal.valueOf(20))
                .frequency(Frequency.MONTHLY)
                .splitType(SplitType.EQUAL)
                .startDate(LocalDate.now())
                .paidById(context.getOwner().getId())
                .participants(List.of(

                        ParticipantRequest.builder()
                                .userId(context.getOwner().getId())
                                .splitValue(BigDecimal.valueOf(10))
                                .build(),

                        ParticipantRequest.builder()
                                .userId(context.getSecondUser().getId())
                                .splitValue(BigDecimal.valueOf(10))
                                .build()

                ))
                .build();
    }
}