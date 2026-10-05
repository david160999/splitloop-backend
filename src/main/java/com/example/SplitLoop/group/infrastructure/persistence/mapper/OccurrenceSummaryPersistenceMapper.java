package com.example.SplitLoop.group.infrastructure.persistence.mapper;

import com.example.SplitLoop.group.domain.model.OccurrenceSummaryData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OccurrenceSummaryPersistenceMapper {

    default OccurrenceSummaryData toData(int pendingCount, BigDecimal totalAmount, LocalDate nextDueDate) {
        return new OccurrenceSummaryData(
                pendingCount,
                totalAmount != null ? totalAmount : BigDecimal.ZERO,
                nextDueDate
        );
    }
}
