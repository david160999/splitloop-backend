package com.example.SplitLoop.expense.infrastructure.persistence.mapper;

import com.example.SplitLoop.expense.application.dto.response.ParticipantResponse;
import com.example.SplitLoop.expense.application.dto.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.application.dto.request.CreateRecurringExpenseRequest;
import com.example.SplitLoop.expense.application.dto.request.UpdateRecurringExpenseRequest;
import com.example.SplitLoop.expense.application.dto.request.ParticipantRequest;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecurringExpensePersistenceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", source = "groupEntity")
    @Mapping(target = "paidBy", source = "paidBy")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "participants", ignore = true)
    RecurringExpenseEntity toEntity(
            CreateRecurringExpenseRequest request,
            GroupEntity groupEntity,
            UserEntity paidBy,
            UserEntity createdBy);

    @Mapping(target = "group", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "participants", ignore = true)
    void updateEntity(
            UpdateRecurringExpenseRequest request,
            @MappingTarget RecurringExpenseEntity recurringExpenseEntity,
            UserEntity paidBy);

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "paidBy", source = "paidBy.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    RecurringExpenseResponse toResponse(RecurringExpenseEntity recurringExpenseEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurringExpense", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "value", source = "request.splitValue")
    RecurringExpenseParticipantEntity toParticipant(
            ParticipantRequest request,
            UserEntity user);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    ParticipantResponse toParticipantResponse(
            RecurringExpenseParticipantEntity participant);

    List<ParticipantResponse> toParticipantResponses(
            List<RecurringExpenseParticipantEntity> participants);

}