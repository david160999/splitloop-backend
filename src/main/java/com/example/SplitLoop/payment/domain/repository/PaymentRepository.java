package com.example.SplitLoop.payment.domain.repository;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment> {

    Page<Payment> findByOccurrence(ExpenseOccurrence occurrence, Pageable pageable);

    List<Payment> findBySplit(ExpenseOccurrenceSplit split);

    List<Payment> findByFromUser(User fromUser);

    List<Payment> findByToUser(User toUser);

    Page<Payment> findByFromUserOrToUser(User fromUser, User toUser, Pageable pageable);

    @Query("""
            select coalesce(sum(p.amount), 0)
            from Payment p
            where p.split = :split
            """)
    BigDecimal sumAmountBySplit(@Param("split") ExpenseOccurrenceSplit split);
}
