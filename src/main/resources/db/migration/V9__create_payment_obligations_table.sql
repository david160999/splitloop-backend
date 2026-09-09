-- V9__create_payment_obligations_table.sql

CREATE TABLE payment_obligations (
    id UUID PRIMARY KEY,

    group_id UUID NOT NULL,
    debtor_id UUID NOT NULL,
    creditor_id UUID NOT NULL,

    amount DECIMAL(19,2) NOT NULL,

    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'COMPLETED', 'CANCELLED', 'REFUNDED')),

    created_at TIMESTAMP NOT NULL,
    paid_at TIMESTAMP,

    CONSTRAINT fk_payment_obligations_group
        FOREIGN KEY (group_id)
        REFERENCES groups(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_payment_obligations_debtor
        FOREIGN KEY (debtor_id)
        REFERENCES users(id),

    CONSTRAINT fk_payment_obligations_creditor
        FOREIGN KEY (creditor_id)
        REFERENCES users(id),

    CONSTRAINT chk_payment_obligation_amount
        CHECK (amount > 0),

    CONSTRAINT chk_payment_obligation_users
        CHECK (debtor_id <> creditor_id)
);

CREATE INDEX idx_payment_obligations_group_id
    ON payment_obligations(group_id);

CREATE INDEX idx_payment_obligations_debtor_id
    ON payment_obligations(debtor_id);

CREATE INDEX idx_payment_obligations_creditor_id
    ON payment_obligations(creditor_id);

CREATE INDEX idx_payment_obligations_status
    ON payment_obligations(status);