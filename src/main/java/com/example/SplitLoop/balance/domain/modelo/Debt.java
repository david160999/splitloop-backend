package com.example.SplitLoop.balance.domain.modelo;

import com.example.SplitLoop.user.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
public class Debt {

    private UserEntity debtor;

    private UserEntity creditor;

    private BigDecimal amount;
}
