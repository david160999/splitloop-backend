-- V6__create_expense_occurrences_table.sql

CREATE TABLE expense_occurrences (
    id UUID PRIMARY KEY,

    recurring_expense_id UUID NOT NULL,
    group_id UUID NOT NULL,
    paid_by_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,

    due_date DATE NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,

    status VARCHAR(20) NOT NULL
            CHECK (status IN (
                'PENDING',
                'PARTIALLY_PAID',
                'PAID',
                'CANCELLED'
            )),

     created_at TIMESTAMP NOT NULL,
     updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_expense_occurrences_recurring_expense
        FOREIGN KEY (recurring_expense_id)
        REFERENCES recurring_expenses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_expense_occurrences_group
        FOREIGN KEY (group_id)
        REFERENCES groups(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_expense_occurrences_paid_by
        FOREIGN KEY (paid_by_id)
        REFERENCES users(id),

    CONSTRAINT uq_expense_occurrence_period
        UNIQUE (recurring_expense_id, period_start, period_end)
);

CREATE INDEX idx_expense_occurrences_group_id
    ON expense_occurrences(group_id);

CREATE INDEX idx_expense_occurrences_paid_by_id
    ON expense_occurrences(paid_by_id);

CREATE INDEX idx_expense_occurrences_recurring_expense_id
    ON expense_occurrences(recurring_expense_id);

CREATE INDEX idx_expense_occurrences_due_date
    ON expense_occurrences(due_date);

CREATE INDEX idx_expense_occurrences_status
    ON expense_occurrences(status);