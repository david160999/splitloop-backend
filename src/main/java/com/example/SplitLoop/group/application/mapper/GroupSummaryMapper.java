package com.example.SplitLoop.group.application.mapper;

import com.example.SplitLoop.group.application.dto.response.GroupSummaryResponse;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.OccurrenceSummaryData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupSummaryMapper {

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "name", source = "group.name")
    @Mapping(target = "members", source = "membersCount")
    @Mapping(target = "activeRecurringExpenses", source = "activeRecurringExpenses")
    @Mapping(target = "pendingOccurrences", source = "occurrenceSummary.pendingCount")
    @Mapping(target = "totalExpenses", source = "occurrenceSummary.totalAmount")
    @Mapping(target = "pendingAmount", source = "pendingAmount")
    @Mapping(target = "nextDueDate", source = "occurrenceSummary.nextDueDate")
    GroupSummaryResponse toResponse(
            Group group,
            long membersCount,
            int activeRecurringExpenses,
            OccurrenceSummaryData occurrenceSummary,
            BigDecimal pendingAmount
    );
}
