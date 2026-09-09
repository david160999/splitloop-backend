-- V8__create_payments_table.sql

create table payments (
    id UUID primary key,

    occurrence_id UUID not null,
    split_id UUID not null,

    from_user_id UUID not null,
    to_user_id UUID not null,

    created_by UUID not null,

    amount decimal(19,2) not null,

    type varchar(20) not null,

    note varchar(255),

    paid_at timestamp not null default CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_occurrence
        FOREIGN KEY (occurrence_id)
        REFERENCES expense_occurrences(id)
        ON delete CASCADE,

    CONSTRAINT fk_payments_split
        FOREIGN KEY (split_id)
        REFERENCES expense_occurrence_splits(id)
        ON delete CASCADE,

    CONSTRAINT fk_payments_from_user
        FOREIGN KEY (from_user_id)
        REFERENCES users(id),

    CONSTRAINT fk_payments_to_user
        FOREIGN KEY (to_user_id)
        REFERENCES users(id),

    CONSTRAINT fk_payments_created_by
        FOREIGN KEY (created_by)
        REFERENCES users(id),

    CONSTRAINT chk_payment_different_users
        CHECK (from_user_id <> to_user_id),

    CONSTRAINT chk_payment_amount
        CHECK (amount <> 0),

    CONSTRAINT chk_payment_type
        CHECK (type IN ('PAYMENT', 'REFUND'))
);

create index idx_payments_occurrence
    on payments (occurrence_id);

create index idx_payments_split
    on payments (split_id);

create index idx_payments_from_user
    on payments (from_user_id);

create index idx_payments_to_user
    on payments (to_user_id);

create index idx_payments_paid_at
    on payments (paid_at);

CREATE INDEX idx_payments_type
    ON payments(type);