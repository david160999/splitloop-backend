package com.example.SplitLoop.expense.domain.service;

//import com.example.SplitLoop.expense.controller.request.CreateExpenseRequest;
//import com.example.SplitLoop.expense.controller.response.ExpenseResponse;
//import com.example.SplitLoop.expense.controller.request.UpdateExpenseRequest;
//import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
//import com.example.SplitLoop.expense.entity.Expense;
//import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
//import com.example.SplitLoop.expense.mapper.ExpenseMapper;
//import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
//import com.example.SplitLoop.expense.repository.ExpenseRepository;
//import com.example.SplitLoop.expense.domain.repository.ExpenseSplitRepository;
//import com.example.SplitLoop.group.domain.entity.Group;
//import com.example.SplitLoop.group.domain.repository.GroupRepository;
//import com.example.SplitLoop.user.entity.User;
//import com.example.SplitLoop.user.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class ExpenseServiceImpl implements ExpenseService {
//
//    private final ExpenseOccurrenceRepository expenseRepository;
//    private final ExpenseSplitRepository expenseSplitRepository;
//    private final GroupRepository groupRepository;
//    private final UserRepository userRepository;
//    private final ExpenseMapper expenseMapper;
//
//    @Override
//    @Transactional
//    public ExpenseResponse createExpense(UUID groupId, CreateExpenseRequest request) {
//
//        Group group = groupRepository.findById(groupId)
//                .orElseThrow(() -> new RuntimeException("Group not found"));
//
//        User creator = userRepository.findById(request.getCreatedBy())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        validateSplitAmounts(request.getAmount(), request.getSplits());
//
//        ExpenseOccurrence expense = ExpenseOccurrence.builder()
//                .group(group)
//                .createdBy(creator)
//                .name(request.getName())
//                .amount(request.getAmount())
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        expense = expenseRepository.save(expense);
//
//        List<RecurringExpenseParticipant> splits = request.getSplits()
//                .stream()
//                .map(split -> RecurringExpenseParticipant.builder()
//                        .expense(expense)
//                        .user(userRepository.findById(split.getUserId())
//                                .orElseThrow(() -> new RuntimeException("User not found")))
//                        .amountOwed(split.getAmountOwed())
//                        .build())
//                .toList();
//
//        expenseSplitRepository.saveAll(splits);
//
//        return expenseMapper.toResponse(expense, splits);
//    }
//
//    @Override
//    public List<ExpenseResponse> getExpensesByGroup(UUID groupId) {
//
//        return expenseRepository.findByGroupId(groupId)
//                .stream()
//                .map(expense -> expenseMapper.toResponse(
//                        expense,
//                        expenseSplitRepository.findByExpenseId(expense.getId())))
//                .toList();
//    }
//
//    @Override
//    public ExpenseResponse getExpense(UUID expenseId) {
//
//        Expense expense = expenseRepository.findById(expenseId)
//                .orElseThrow(() -> new RuntimeException("Expense not found"));
//
//        List<RecurringExpenseParticipant> splits =
//                expenseSplitRepository.findByExpenseId(expenseId);
//
//        return expenseMapper.toResponse(expense, splits);
//    }
//
//    @Override
//    @Transactional
//    public ExpenseResponse updateExpense(UUID expenseId,
//                                         UpdateExpenseRequest request) {
//
//        Expense expense = expenseRepository.findById(expenseId)
//                .orElseThrow(() -> new RuntimeException("Expense not found"));
//
//        validateSplitAmounts(request.getAmount(), request.getSplits());
//
//        expense.setName(request.getName());
//        expense.setAmount(request.getAmount());
//
//        expenseRepository.save(expense);
//
//        expenseSplitRepository.deleteByExpenseId(expenseId);
//
//        List<RecurringExpenseParticipant> splits = request.getSplits()
//                .stream()
//                .map(split -> RecurringExpenseParticipant.builder()
//                        .expense(expense)
//                        .user(userRepository.findById(split.getUserId())
//                                .orElseThrow(() -> new RuntimeException("User not found")))
//                        .amountOwed(split.getAmountOwed())
//                        .build())
//                .toList();
//
//        expenseSplitRepository.saveAll(splits);
//
//        return expenseMapper.toResponse(expense, splits);
//    }
//
//    @Override
//    @Transactional
//    public void deleteExpense(UUID expenseId) {
//
//        expenseSplitRepository.deleteByExpenseId(expenseId);
//        expenseRepository.deleteById(expenseId);
//    }
//
//    private void validateSplitAmounts(BigDecimal amount,
//                                      List<ExpenseSplitRequest> splits) {
//
//        BigDecimal total = splits.stream()
//                .map(ExpenseSplitRequest::getAmountOwed)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        if (total.compareTo(amount) != 0) {
//            throw new IllegalArgumentException(
//                    "The sum of all splits must equal the expense amount");
//        }
//    }
//}