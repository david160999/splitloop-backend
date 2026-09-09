package com.example.SplitLoop.expense.domain.service.splitStrategy;

import com.example.SplitLoop.expense.domain.entity.SplitType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SplitStrategyFactory {

    private final EqualSplitStrategy equal;
    private final PercentageSplitStrategy percentage;
    private final FixedSplitStrategy fixed;

    public SplitStrategy getStrategy(SplitType splitType){

        return switch (splitType){

            case EQUAL -> equal;
            case PERCENTAGE -> percentage;
            case FIXED -> fixed;
        };
    }

}