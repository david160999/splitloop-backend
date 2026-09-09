package com.example.SplitLoop.util.TestData;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExpenseContext {

    // Users
    private User owner;
    private User secondUser;

    // Group
    private Group group;

    // Members
    private GroupMember admin;
    private GroupMember member;

    // Expense
    private RecurringExpense recurringExpense;

    // Participants
    private List<RecurringExpenseParticipant> participants;

    // Generated occurrence
    private ExpenseOccurrence occurrence;

    // Splits
    private List<ExpenseOccurrenceSplit> splits;
}