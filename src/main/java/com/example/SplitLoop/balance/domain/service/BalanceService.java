package com.example.SplitLoop.balance.domain.service;

import com.example.SplitLoop.balance.domain.model.Balance;
import com.example.SplitLoop.balance.domain.model.Debt;
import com.example.SplitLoop.balance.domain.model.PendingExpenseData;
import com.example.SplitLoop.user.domain.model.User;

import java.math.BigDecimal;
import java.util.*;

public class BalanceService {

    public List<Balance> calculateBalances(List<PendingExpenseData> expenses) {
        Map<UUID, UserBalancePair> balanceMap = new HashMap<>();

        for (PendingExpenseData expense : expenses) {
            // En PendingExpenseData se accede con expense.amountOwed() y expense.amountPaid()
            BigDecimal remaining = expense.amountOwed().subtract(expense.amountPaid());

            if (remaining.signum() == 0) {
                continue;
            }

            // El usuario que debe dinero decrementa su balance (expense.user())
            addAmount(balanceMap, expense.user(), remaining.negate());
            // El usuario que pagó incrementa su balance (expense.paidBy())
            addAmount(balanceMap, expense.paidBy(), remaining);
        }

        return balanceMap.values().stream()
                .map(pair -> Balance.builder()
                        .user(pair.user)
                        .amount(pair.amount)
                        .build())
                .toList();
    }

    public List<Debt> calculateDebts(List<PendingExpenseData> expenses) {
        List<Balance> balances = calculateBalances(expenses);

        // Mutables internamente solo durante la ejecución del algoritmo de simplificación
        List<MutableBalance> creditors = balances.stream()
                .filter(b -> b.amount().compareTo(BigDecimal.ZERO) > 0)
                .map(b -> new MutableBalance(b.user(), b.amount()))
                .sorted(Comparator.comparing(MutableBalance::getAmount).reversed())
                .toList();

        List<MutableBalance> debtors = balances.stream()
                .filter(b -> b.amount().compareTo(BigDecimal.ZERO) < 0)
                .map(b -> new MutableBalance(b.user(), b.amount().abs()))
                .sorted(Comparator.comparing(MutableBalance::getAmount).reversed())
                .toList();

        List<Debt> debts = new ArrayList<>();
        int creditorIndex = 0;
        int debtorIndex = 0;

        while (creditorIndex < creditors.size() && debtorIndex < debtors.size()) {
            MutableBalance creditor = creditors.get(creditorIndex);
            MutableBalance debtor = debtors.get(debtorIndex);

            BigDecimal amountToSettle = creditor.amount.min(debtor.amount);

            debts.add(Debt.builder()
                    .creditor(creditor.user)
                    .debtor(debtor.user)
                    .amount(amountToSettle)
                    .build());

            creditor.amount = creditor.amount.subtract(amountToSettle);
            debtor.amount = debtor.amount.subtract(amountToSettle);

            if (creditor.amount.compareTo(BigDecimal.ZERO) == 0) {
                creditorIndex++;
            }
            if (debtor.amount.compareTo(BigDecimal.ZERO) == 0) {
                debtorIndex++;
            }
        }

        return debts;
    }

    private void addAmount(Map<UUID, UserBalancePair> map, User user, BigDecimal amount) {
        map.compute(user.id(), (id, existing) -> {
            if (existing == null) {
                return new UserBalancePair(user, amount);
            }
            return new UserBalancePair(user, existing.amount.add(amount));
        });
    }

    // Clases auxiliares privadas para el cálculo mutable en memoria
    private static class UserBalancePair {
        final User user;
        final BigDecimal amount;

        UserBalancePair(User user, BigDecimal amount) {
            this.user = user;
            this.amount = amount;
        }
    }

    private static class MutableBalance {
        final User user;
        BigDecimal amount;

        MutableBalance(User user, BigDecimal amount) {
            this.user = user;
            this.amount = amount;
        }

        BigDecimal getAmount() {
            return amount;
        }
    }
}