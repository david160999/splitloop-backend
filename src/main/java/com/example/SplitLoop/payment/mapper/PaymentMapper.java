package com.example.SplitLoop.payment.mapper;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.payment.application.dto.request.RegisterPaymentRequest;
import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentEntity;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "occurrence", source = "occurrence")
    @Mapping(target = "split", source = "split")
    @Mapping(target = "fromUser", source = "fromUser")
    @Mapping(target = "toUser", source = "toUser")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "paidAt", ignore = true)
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "type", constant = "PAYMENT")
    PaymentEntity toEntity(
            RegisterPaymentRequest request,
            ExpenseOccurrenceEntity occurrence,
            ExpenseOccurrenceSplitEntity split,
            UserEntity fromUser,
            UserEntity toUser,
            UserEntity createdBy);

    @Mapping(target = "occurrenceId", source = "occurrence.id")
    @Mapping(target = "splitId", source = "split.id")
    @Mapping(target = "fromUserId", source = "fromUser.id")
    @Mapping(target = "toUserId", source = "toUser.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    PaymentResponse toResponse(PaymentEntity paymentEntity);
}

