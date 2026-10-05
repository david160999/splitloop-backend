package com.example.SplitLoop.group.domain.port;

import java.util.UUID;

public interface RecurringExpenseClientPort {
    int getActiveCountByGroupId(UUID groupId);
}
