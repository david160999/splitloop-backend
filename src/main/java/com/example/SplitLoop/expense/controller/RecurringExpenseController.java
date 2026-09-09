package com.example.SplitLoop.expense.controller;

import com.example.SplitLoop.expense.application.command.CreateRecurringExpenseUseCase;
import com.example.SplitLoop.expense.application.command.DeleteRecurringExpenseUseCase;
import com.example.SplitLoop.expense.application.command.UpdateOccurrenceUseCase;
import com.example.SplitLoop.expense.application.command.UpdateRecurringExpenseUseCase;
import com.example.SplitLoop.expense.application.query.GetOccurrenceUseCase;
import com.example.SplitLoop.expense.application.query.GetOccurrencesUseCase;
import com.example.SplitLoop.expense.application.query.GetRecurringExpenseUseCase;
import com.example.SplitLoop.expense.application.query.GetRecurringExpensesUseCase;
import com.example.SplitLoop.expense.application.usecase.*;
import com.example.SplitLoop.expense.controller.command.CreateRecurringExpenseCommand;
import com.example.SplitLoop.expense.controller.command.UpdateOccurrenceCommand;
import com.example.SplitLoop.expense.controller.command.UpdateRecurringExpenseCommand;
import com.example.SplitLoop.expense.controller.query.GetOccurrencesQuery;
import com.example.SplitLoop.expense.controller.query.GetRecurringExpensesQuery;
import com.example.SplitLoop.expense.controller.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.controller.response.RecurringExpenseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/recurring-expenses")
@RequiredArgsConstructor
@Tag(name = "Recurring Expenses")
public class RecurringExpenseController {

    private final CreateRecurringExpenseUseCase createRecurringExpenseUseCase;
    private final DeleteRecurringExpenseUseCase deleteRecurringExpenseUseCase;
    private final UpdateOccurrenceUseCase updateOccurrenceUseCase;
    private final UpdateRecurringExpenseUseCase updateRecurringExpenseUseCase;

    private final GetOccurrencesUseCase getOccurrencesUseCase;
    private final GetOccurrenceUseCase getOccurrenceUseCase;
    private final GetRecurringExpensesUseCase getRecurringExpensesUseCase;
    private final GetRecurringExpenseUseCase getRecurringExpenseUseCase;

    private final CancelOccurrenceUseCase cancelOccurrenceUseCase;
    private final ChangePaidByUseCase changePaidByUseCase;
    private final DuplicateRecurringExpenseUseCase duplicateRecurringExpenseUseCase;
    private final PauseRecurringExpenseUseCase pauseRecurringExpenseUseCase;
    private final ResumeRecurringExpenseUseCase resumeRecurringExpenseUseCase;

    @PostMapping
    @Operation(summary = "Create recurring expense")
    public ResponseEntity<RecurringExpenseResponse> createRecurringExpense(@Valid @RequestBody CreateRecurringExpenseCommand request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createRecurringExpenseUseCase.execute(request));
    }

    @GetMapping
    @Operation(summary = "Get recurring expenses")
    public ResponseEntity<List<RecurringExpenseResponse>> getRecurringExpenses(@Valid GetRecurringExpensesQuery query) {

        return ResponseEntity.ok(getRecurringExpensesUseCase.execute(query));
    }

    @GetMapping("/{recurringExpenseId}")
    @Operation(summary = "Get recurring expense")
    public ResponseEntity<RecurringExpenseResponse> getRecurringExpense(@PathVariable UUID recurringExpenseId) {

        return ResponseEntity.ok(getRecurringExpenseUseCase.execute(recurringExpenseId));
    }

    @PutMapping("/{recurringExpenseId}")
    @Operation(summary = "Update recurring expense")
    public ResponseEntity<RecurringExpenseResponse> updateRecurringExpense(
            @PathVariable UUID recurringExpenseId,
            @Valid @RequestBody UpdateRecurringExpenseCommand request) {

        return ResponseEntity.ok(updateRecurringExpenseUseCase.execute(recurringExpenseId, request));
    }

    @DeleteMapping("/{recurringExpenseId}")
    @Operation(summary = "Delete recurring expense")
    public ResponseEntity<Void> deleteRecurringExpense(@PathVariable UUID recurringExpenseId) {

        deleteRecurringExpenseUseCase.execute(recurringExpenseId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{recurringExpenseId}/pause")
    @Operation(summary = "Pause recurring expense")
    public ResponseEntity<Void> pauseRecurringExpense(@PathVariable UUID recurringExpenseId) {

        pauseRecurringExpenseUseCase.execute(recurringExpenseId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{recurringExpenseId}/resume")
    @Operation(summary = "Resume recurring expense")
    public ResponseEntity<Void> resumeRecurringExpense(@PathVariable UUID recurringExpenseId) {

        resumeRecurringExpenseUseCase.execute(recurringExpenseId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{recurringExpenseId}/duplicate")
    @Operation(summary = "Duplicate recurring expense")
    public ResponseEntity<RecurringExpenseResponse> duplicateRecurringExpense(@PathVariable UUID recurringExpenseId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(duplicateRecurringExpenseUseCase.execute(recurringExpenseId));
    }

    // ---------- OCCURRENCES ----------

    @GetMapping("/{recurringExpenseId}/occurrences")
    @Operation(summary = "Get occurrences")
    public ResponseEntity<List<ExpenseOccurrenceResponse>> getOccurrences(@Valid GetOccurrencesQuery query) {

        return ResponseEntity.ok(getOccurrencesUseCase.execute(query));
    }

    @GetMapping("/occurrences/{occurrenceId}")
    @Operation(summary = "Get occurrence")
    public ResponseEntity<ExpenseOccurrenceResponse> getOccurrence(@PathVariable UUID occurrenceId) {

        return ResponseEntity.ok(getOccurrenceUseCase.execute(occurrenceId));
    }

    @PutMapping("/occurrences/{occurrenceId}")
    @Operation(summary = "Update occurrence")
    public ResponseEntity<ExpenseOccurrenceResponse> updateOccurrence(
            @PathVariable UUID occurrenceId,
            @Valid @RequestBody UpdateOccurrenceCommand request) {

        return ResponseEntity.ok(updateOccurrenceUseCase.execute(occurrenceId, request));
    }

    @PatchMapping("/occurrences/{occurrenceId}/cancel")
    @Operation(summary = "Cancel occurrence")
    public ResponseEntity<Void> cancelOccurrence(@PathVariable UUID occurrenceId) {

        cancelOccurrenceUseCase.execute(occurrenceId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/occurrences/{occurrenceId}/paid-by")
    @Operation(summary = "Change paid by")
    public ResponseEntity<ExpenseOccurrenceResponse> changePaidBy(
            @PathVariable UUID occurrenceId,
            @RequestParam UUID paidById) {

        return ResponseEntity.ok(changePaidByUseCase.execute(occurrenceId, paidById));
    }
}