package com.example.SplitLoop.balance.domain.modelo;

import com.example.SplitLoop.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
public class Debt {

    private User debtor;

    private User creditor;

    private BigDecimal amount;
}
