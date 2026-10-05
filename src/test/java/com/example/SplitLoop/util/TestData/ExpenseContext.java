package com.example.SplitLoop.util.TestData;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExpenseContext {

    // Users
    private UserEntity owner;
    private UserEntity secondUserEntity;

    // Group
    private GroupEntity groupEntity;

    // Members
    private GroupMemberEntity admin;
    private GroupMemberEntity member;

    // Expense
    private RecurringExpenseEntity recurringExpenseEntity;

    // Participants
    private List<RecurringExpenseParticipantEntity> participants;

    // Generated occurrence
    private ExpenseOccurrenceEntity occurrence;

    // Splits
    private List<ExpenseOccurrenceSplitEntity> splits;
}