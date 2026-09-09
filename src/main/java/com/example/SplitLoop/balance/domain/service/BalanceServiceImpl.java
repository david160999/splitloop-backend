package com.example.SplitLoop.balance.domain.service;

import com.example.SplitLoop.balance.domain.modelo.Balance;
import com.example.SplitLoop.balance.domain.modelo.Debt;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final ExpenseOccurrenceSplitRepository splitRepository;


    @Override
    public List<Balance> calculateBalances(List<ExpenseOccurrenceSplit> splits) {

        Map<UUID, Balance> balances = new HashMap<>();

        for (ExpenseOccurrenceSplit split : splits) {

            BigDecimal remaining = split.getAmountOwed()
                    .subtract(split.getAmountPaid());

            if (remaining.signum() == 0) {
                continue;
            }

            addBalance(balances, split.getUser(), remaining.negate());
            addBalance(balances, split.getOccurrence().getPaidBy(), remaining);
        }

        return new ArrayList<>(balances.values());
    }

    @Override
    public List<Debt> calculateDebts(List<ExpenseOccurrenceSplit> splits) {

        List<Balance> balances = calculateBalances(splits);

        List<Balance> creditors = balances.stream()
                .filter(balance -> balance.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(Balance::getAmount).reversed())
                .toList();

        List<Balance> debtors = balances.stream()
                .filter(balance -> balance.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(balance -> Balance.builder()
                        .user(balance.getUser())
                        .amount(balance.getAmount().abs())
                        .build())
                .sorted(Comparator.comparing(Balance::getAmount).reversed())
                .toList();

        List<Debt> debts = new ArrayList<>();

        int creditorIndex = 0;
        int debtorIndex = 0;

        while (creditorIndex < creditors.size() && debtorIndex < debtors.size()) {

            Balance creditor = creditors.get(creditorIndex);
            Balance debtor = debtors.get(debtorIndex);

            BigDecimal amount = creditor.getAmount().min(debtor.getAmount());

            debts.add(Debt.builder()
                    .creditor(creditor.getUser())
                    .debtor(debtor.getUser())
                    .amount(amount)
                    .build());

            creditor.setAmount(creditor.getAmount().subtract(amount));
            debtor.setAmount(debtor.getAmount().subtract(amount));

            if (creditor.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                creditorIndex++;
            }

            if (debtor.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                debtorIndex++;
            }
        }

        return debts;
    }

    private void addBalance(
            Map<UUID, Balance> balances,
            User user,
            BigDecimal amount) {

        balances.computeIfAbsent(
                user.getId(),
                id -> Balance.builder()
                        .user(user)
                        .amount(BigDecimal.ZERO)
                        .build());

        Balance balance = balances.get(user.getId());

        balance.setAmount(balance.getAmount().add(amount));
    }
}
