package com.example.SplitLoop.expense.mapper;

import com.example.SplitLoop.expense.controller.command.UpdateOccurrenceCommand;
import com.example.SplitLoop.expense.controller.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.controller.response.ExpenseOccurrenceSplitResponse;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseOccurrenceMapper {

    @Mapping(target = "recurringExpenseId", source = "recurringExpense.id")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "paidBy", source = "paidBy.id")
    @Mapping(target = "splits", ignore = true)
    ExpenseOccurrenceResponse toResponse(
            ExpenseOccurrence occurrence);

    @Mapping(target = "userId", source = "user.id")
    ExpenseOccurrenceSplitResponse toSplitResponse(
            ExpenseOccurrenceSplit split);

    List<ExpenseOccurrenceSplitResponse> toSplitResponses(
            List<ExpenseOccurrenceSplit> splits);

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
            UpdateOccurrenceCommand request,
            @MappingTarget ExpenseOccurrence occurrence,
            User paidBy);
}