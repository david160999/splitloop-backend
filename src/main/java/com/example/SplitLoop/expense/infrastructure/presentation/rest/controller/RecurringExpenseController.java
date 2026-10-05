package com.example.SplitLoop.expense.infrastructure.presentation.rest.controller;

import com.example.SplitLoop.expense.application.usecase.command.*;
import com.example.SplitLoop.expense.application.usecase.query.GetOccurrenceUseCase;
import com.example.SplitLoop.expense.application.usecase.query.GetOccurrencesUseCase;
import com.example.SplitLoop.expense.application.usecase.query.GetRecurringExpenseUseCase;
import com.example.SplitLoop.expense.application.usecase.query.GetActiveRecurringExpensesUseCase;
import com.example.SplitLoop.expense.application.dto.request.CreateRecurringExpenseRequest;
import com.example.SplitLoop.expense.application.dto.request.UpdateOccurrenceRequest;
import com.example.SplitLoop.expense.application.dto.request.UpdateRecurringExpenseRequest;
import com.example.SplitLoop.expense.application.dto.query.GetOccurrencesQuery;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.application.dto.response.RecurringExpenseResponse;
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
    private final GetActiveRecurringExpensesUseCase getActiveRecurringExpensesUseCase;
    private final GetRecurringExpenseUseCase getRecurringExpenseUseCase;

    private final CancelOccurrenceUseCase cancelOccurrenceUseCase;
    private final ChangePaidByUseCase changePaidByUseCase;
    private final DuplicateRecurringExpenseUseCase duplicateRecurringExpenseUseCase;
    private final PauseRecurringExpenseUseCase pauseRecurringExpenseUseCase;
    private final ResumeRecurringExpenseUseCase resumeRecurringExpenseUseCase;

    @PostMapping
    @Operation(summary = "Create recurring expense")
    public ResponseEntity<RecurringExpenseResponse> createRecurringExpense(@Valid @RequestBody CreateRecurringExpenseRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(createRecurringExpenseUseCase.execute(request));
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "Obtener gastos recurrentes activos del grupo")
    public ResponseEntity<List<RecurringExpenseResponse>> getActiveRecurringExpenses(@PathVariable UUID groupId) {

        return ResponseEntity.ok(getActiveRecurringExpensesUseCase.execute(groupId));
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
            @Valid @RequestBody UpdateRecurringExpenseRequest request) {

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
            @Valid @RequestBody UpdateOccurrenceRequest request) {

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