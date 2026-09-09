-- V7__create_expense_occurrence_splits_table.sql

CREATE TABLE expense_occurrence_splits (
    id UUID PRIMARY KEY,

    occurrence_id UUID NOT NULL,
    user_id UUID NOT NULL,

    amount_owed DECIMAL(19,2) NOT NULL,
    amount_paid DECIMAL(19,2) NOT NULL DEFAULT 0,

    status VARCHAR(20) NOT NULL
        CHECK (status IN (
            'PENDING',
            'PARTIALLY_PAID',
            'PAID'
        )),

    CONSTRAINT fk_expense_occurrence_splits_occurrence
        FOREIGN KEY (occurrence_id)
        REFERENCES expense_occurrences(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_expense_occurrence_splits_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT uk_occurrence_user
        UNIQUE (occurrence_id, user_id)
);

CREATE INDEX idx_expense_occurrence_splits_user_id
    ON expense_occurrence_splits(user_id);