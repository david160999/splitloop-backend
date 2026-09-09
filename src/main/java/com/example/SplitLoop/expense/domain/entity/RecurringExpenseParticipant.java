package com.example.SplitLoop.expense.domain.entity;

import com.example.SplitLoop.user.domain.entity.User;
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
public class RecurringExpenseParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurring_expense_id", nullable = false)
    private RecurringExpense recurringExpense;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Solo se usa cuando el split es
     * PERCENTAGE o FIXED.
     *
     * En EQUAL será null.
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal value;
}
