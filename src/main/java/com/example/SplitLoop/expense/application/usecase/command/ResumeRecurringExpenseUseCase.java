package com.example.SplitLoop.expense.application.usecase.command;

import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.expense.domain.exception.UserNotMemberOfGroupException;
import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.port.GroupMemberPort;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.expense.domain.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static com.example.SplitLoop.expense.domain.service.RecurringExpenseService.GENERATION_HORIZON_MONTHS;


@Service
@RequiredArgsConstructor
public class ResumeRecurringExpenseUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final CurrentUserService currentUserService;
    private final GroupMemberPort groupMemberPort;
    private final ExpenseOccurrenceService occurrenceService;

    @Transactional
    public void execute(UUID recurringExpenseId) {

        RecurringExpense recurringExpense = recurringExpenseRepository.findById(recurringExpenseId)
                .orElseThrow(() -> new RecurringExpenseNotFoundException(recurringExpenseId));

        User currentUser = currentUserService.getCurrentUser();

        GroupMember member = groupMemberPort.findByGroupIdAndUserId(recurringExpense.group().id(), currentUser.id())
                .orElseThrow(() -> new UserNotMemberOfGroupException(currentUser.id()));

        member.ensureIsAdmin();

        // 3. Modificar el estado en el Dominio (la entidad valida sus reglas internas)
        RecurringExpense resumedExpense = recurringExpense.resume();

        // 4. Persistir el nuevo estado
        recurringExpenseRepository.save(resumedExpense);

        // 5. Orquestar la generación de ocurrencias pendientes
        LocalDate horizonDate = LocalDate.now().plusMonths(GENERATION_HORIZON_MONTHS);
        occurrenceService.generatePendingOccurrences(resumedExpense, horizonDate);
    }

}
