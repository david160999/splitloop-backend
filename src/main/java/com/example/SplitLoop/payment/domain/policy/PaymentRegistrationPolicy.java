package com.example.SplitLoop.payment.domain.policy;

import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.payment.domain.model.Payment;

public interface PaymentRegistrationPolicy {

    void validateCanRegister(Payment payment, GroupMember member);
}
