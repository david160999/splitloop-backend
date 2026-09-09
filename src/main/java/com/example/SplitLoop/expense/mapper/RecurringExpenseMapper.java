package com.example.SplitLoop.expense.mapper;

import com.example.SplitLoop.expense.controller.response.ParticipantResponse;
import com.example.SplitLoop.expense.controller.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.controller.command.CreateRecurringExpenseCommand;
import com.example.SplitLoop.expense.controller.command.UpdateRecurringExpenseCommand;
import com.example.SplitLoop.expense.controller.request.ParticipantRequest;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecurringExpenseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", source = "group")
    @Mapping(target = "paidBy", source = "paidBy")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "participants", ignore = true)
    RecurringExpense toEntity(
            CreateRecurringExpenseCommand request,
            Group group,
            User paidBy,
            User createdBy);

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "participants", ignore = true)
    void updateEntity(
            UpdateRecurringExpenseCommand request,
            @MappingTarget RecurringExpense recurringExpense,
            User paidBy);

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "paidBy", source = "paidBy.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    RecurringExpenseResponse toResponse(RecurringExpense recurringExpense);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurringExpense", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "value", source = "request.splitValue")
    RecurringExpenseParticipant toParticipant(
            ParticipantRequest request,
            User user);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    ParticipantResponse toParticipantResponse(
            RecurringExpenseParticipant participant);

    List<ParticipantResponse> toParticipantResponses(
            List<RecurringExpenseParticipant> participants);

}