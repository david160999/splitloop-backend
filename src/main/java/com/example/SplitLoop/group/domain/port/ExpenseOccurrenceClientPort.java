package com.example.SplitLoop.group.domain.port;

import com.example.SplitLoop.group.domain.model.OccurrenceSummaryData;

import java.util.UUID;

public interface ExpenseOccurrenceClientPort {
    OccurrenceSummaryData getOccurrenceSummaryByGroupId(UUID groupId);
}
