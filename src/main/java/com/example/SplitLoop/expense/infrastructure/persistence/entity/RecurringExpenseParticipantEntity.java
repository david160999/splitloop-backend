package com.example.SplitLoop.expense.infrastructure.persistence.entity;

import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "recurring_expense_participants")
public class RecurringExpenseParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurring_expense_id", nullable = false)
    private RecurringExpenseEntity recurringExpense;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * Solo se usa cuando el split es
     * PERCENTAGE o FIXED.
     *
     * En EQUAL será null.
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal value;
}
