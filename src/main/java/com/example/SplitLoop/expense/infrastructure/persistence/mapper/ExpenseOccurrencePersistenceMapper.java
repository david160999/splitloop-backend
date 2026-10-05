package com.example.SplitLoop.expense.infrastructure.persistence.mapper;

import com.example.SplitLoop.expense.application.dto.request.UpdateOccurrenceRequest;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceSplitResponse;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseOccurrencePersistenceMapper {

    @Mapping(target = "recurringExpenseId", source = "recurringExpense.id")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "paidBy", source = "paidBy.id")
    @Mapping(target = "splits", ignore = true)
    ExpenseOccurrenceResponse toResponse(
            ExpenseOccurrenceEntity occurrence);

    @Mapping(target = "userId", source = "user.id")
    ExpenseOccurrenceSplitResponse toSplitResponse(
            ExpenseOccurrenceSplitEntity split);

    List<ExpenseOccurrenceSplitResponse> toSplitResponses(
            List<ExpenseOccurrenceSplitEntity> splits);

//    @Mapping(target = "paidBy", source = "paidBy")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "recurringExpense", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "periodStart", ignore = true)
    @Mapping(target = "periodEnd", ignore = true)
    @Mapping(target = "paidBy", source = "paidBy")
    void updateEntity(
            UpdateOccurrenceRequest request,
            @MappingTarget ExpenseOccurrenceEntity occurrence,
            UserEntity paidBy);
}