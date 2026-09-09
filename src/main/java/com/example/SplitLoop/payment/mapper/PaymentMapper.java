package com.example.SplitLoop.payment.mapper;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.payment.controller.command.RegisterPaymentCommand;
import com.example.SplitLoop.payment.controller.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.user.domain.entity.User;
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
    Payment toEntity(
            RegisterPaymentCommand request,
            ExpenseOccurrence occurrence,
            ExpenseOccurrenceSplit split,
            User fromUser,
            User toUser,
            User createdBy);

    @Mapping(target = "occurrenceId", source = "occurrence.id")
    @Mapping(target = "splitId", source = "split.id")
    @Mapping(target = "fromUserId", source = "fromUser.id")
    @Mapping(target = "toUserId", source = "toUser.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    PaymentResponse toResponse(Payment payment);
}

