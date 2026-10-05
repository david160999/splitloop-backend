package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.expense.domain.model.Frequency;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.model.SplitType;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class RecurringExpenseMother {

    private RecurringExpenseMother() {
    }

    public static RecurringExpenseEntity active() {

        UserEntity userEntity = UserMother.userEntity();
        GroupEntity groupEntity = GroupMother.group(userEntity);

        return active(groupEntity, userEntity);
    }

    public static RecurringExpenseEntity active(GroupEntity groupEntity, UserEntity userEntity) {

        return RecurringExpenseEntity.builder()
                .id(UUID.randomUUID())
                .group(groupEntity)
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

    public static RecurringExpenseEntity paused() {

        return active()
                .toBuilder()
                .status(RecurringExpenseStatus.PAUSED)
                .build();
    }

    public static RecurringExpenseEntity deleted() {

        return active()
                .toBuilder()
                .status(RecurringExpenseStatus.DELETED)
                .build();
    }
}
