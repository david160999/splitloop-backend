package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.expense.domain.entity.Frequency;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.entity.SplitType;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.user.domain.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class RecurringExpenseMother {

    private RecurringExpenseMother() {
    }

    public static RecurringExpense active() {

        UserEntity userEntity = UserMother.userEntity();
        Group group = GroupMother.group(userEntity);

        return active(group, userEntity);
    }

    public static RecurringExpense active(Group group, UserEntity userEntity) {

        return RecurringExpense.builder()
                .id(UUID.randomUUID())
                .group(group)
                .name("Netflix")
                .description("Netflix subscription")
                .amount(BigDecimal.TEN)
                .frequency(Frequency.MONTHLY)
                .splitType(SplitType.EQUAL)
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(null)
                .paidBy(userEntity)
                .createdBy(userEntity)
                .status(RecurringExpenseStatus.ACTIVE)
                .build();
    }

    public static RecurringExpense paused() {

        return active()
                .toBuilder()
                .status(RecurringExpenseStatus.PAUSED)
                .build();
    }

    public static RecurringExpense deleted() {

        return active()
                .toBuilder()
                .status(RecurringExpenseStatus.DELETED)
                .build();
    }
}
