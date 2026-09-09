package com.example.SplitLoop.balance.domain.modelo;

import com.example.SplitLoop.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Balance {

    private User user;

    private BigDecimal amount;
}
