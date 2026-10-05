package com.example.SplitLoop.payment.infrastructure.persistence.mapper;

import com.example.SplitLoop.expense.infrastructure.persistence.mapper.ExpensePersistenceMapper;
import com.example.SplitLoop.group.infrastructure.persistence.mapper.GroupPersistenceMapper;
import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentEntity;
import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentObligationEntity;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.model.PaymentObligation;
import com.example.SplitLoop.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                UserPersistenceMapper.class,
                GroupPersistenceMapper.class,
                ExpensePersistenceMapper.class // Contiene el mapeo de ExpenseOccurrence
        }
)
public interface PaymentPersistenceMapper {

    Payment toDomain(PaymentEntity entity);
    PaymentEntity toEntity(Payment domain);

    @Mapping(target = "groupEntity", source = "group")
    PaymentObligationEntity toEntity(PaymentObligation domain);

    @Mapping(target = "group", source = "groupEntity")
    PaymentObligation toDomain(PaymentObligationEntity entity);
}
