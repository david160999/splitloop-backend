package com.example.SplitLoop.payment.application.dto.mapper;

import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentDtoMapper {

    @Mapping(target = "occurrenceId", source = "occurrence.id")
    @Mapping(target = "splitId", source = "split.id")
    @Mapping(target = "fromUserId", source = "fromUser.id")
    @Mapping(target = "toUserId", source = "toUser.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    PaymentResponse toResponse(Payment payment);
}
