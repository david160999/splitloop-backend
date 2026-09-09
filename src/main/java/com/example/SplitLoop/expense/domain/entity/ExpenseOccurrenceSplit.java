package com.example.SplitLoop.expense.domain.entity;

import com.example.SplitLoop.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name = "expense_occurrence_splits",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_occurrence_user",
                        columnNames = {"occurrence_id", "user_id"}
                )
        }
)@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ExpenseOccurrenceSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "occurrence_id", nullable = false)
    private ExpenseOccurrence occurrence;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amountOwed;

    @Column(nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseOccurrenceSplitStatus status;
}