package com.example.SplitLoop.expense.application.dto.mapper;

import com.example.SplitLoop.expense.application.dto.request.CreateRecurringExpenseRequest;
import com.example.SplitLoop.expense.application.dto.request.ParticipantRequest;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceSplitResponse;
import com.example.SplitLoop.expense.application.dto.response.ParticipantResponse;
import com.example.SplitLoop.expense.application.dto.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.model.*;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = { RecurringExpenseStatus.class }
)
public interface ExpenseDtoMapper {


    // --- ExpenseOccurrence ---

    @Mapping(target = "id", source = "domain.id")
    @Mapping(target = "name", source = "domain.name")
    @Mapping(target = "amount", source = "domain.amount")
    @Mapping(target = "status", source = "domain.status")
    @Mapping(target = "createdAt", source = "domain.createdAt")
    @Mapping(target = "recurringExpenseId", source = "domain.recurringExpense.id")
    @Mapping(target = "groupId", source = "domain.group.id")
    @Mapping(target = "paidBy", source = "domain.paidBy.id")
    @Mapping(target = "splits", source = "splits")
    ExpenseOccurrenceResponse toResponse(ExpenseOccurrence domain, List<ExpenseOccurrenceSplit> splits);

    @Mapping(target = "recurringExpenseId", source = "recurringExpense.id")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "paidBy", source = "paidBy.id")
    @Mapping(target = "splits", ignore = true)
    ExpenseOccurrenceResponse toResponse(ExpenseOccurrence domain);

    // Método auxiliar para indicarle a MapStruct cómo convertir Money a BigDecimal
    default BigDecimal mapMoneyToBigDecimal(Money money) {
        if (money == null) {
            return null;
        }
        // Ajusta getAmount() según el método getter que tenga tu clase Money
        return money.amount();

        // NOTA: Si getAmount() devuelve un String o double en lugar de BigDecimal, usa:
        // return new BigDecimal(money.getAmount().toString());
    }
    Money map(BigDecimal value);

    // --- ExpenseOccurrenceSplit ---

    @Mapping(target = "userId", source = "user.id")
    ExpenseOccurrenceSplitResponse toResponse(ExpenseOccurrenceSplit domain);

    List<ExpenseOccurrenceSplitResponse> toSplitResponseList(List<ExpenseOccurrenceSplit> domainList);

    // --- RecurringExpense ---

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "paidBy", source = "paidBy.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    RecurringExpenseResponse toResponse(RecurringExpense domain);

    //Command -> Record de Dominio
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "amount", source = "request.amount") // Usa el método helper mapMoney
    @Mapping(target = "frequency", source = "request.frequency")
    @Mapping(target = "splitType", source = "request.splitType")
    @Mapping(target = "startDate", source = "request.startDate")
    @Mapping(target = "endDate", source = "request.endDate")
    @Mapping(target = "group", source = "group")
    @Mapping(target = "paidBy", source = "paidBy")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "status", expression = "java(RecurringExpenseStatus.ACTIVE)")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RecurringExpense toDomain(
            CreateRecurringExpenseRequest request,
            Group group,
            User paidBy,
            User createdBy
    );

    // --- RecurringExpenseParticipant ---

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    ParticipantResponse toResponse(RecurringExpenseParticipant domain);

    List<ParticipantResponse> toParticipantResponseList(List<RecurringExpenseParticipant> domainList);

    //ParticipantRequest -> Record de Dominio del Participante
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recurringExpenseId", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "value", source = "request.splitValue")
    RecurringExpenseParticipant toParticipantDomain(ParticipantRequest request, User user);

}
