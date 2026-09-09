-- V4__create_recurring_expenses_table.sql

CREATE TABLE recurring_expenses (
    id UUID PRIMARY KEY,

    group_id UUID NOT NULL,
    paid_by_id UUID NOT NULL,
    created_by_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),

    amount DECIMAL(19,2) NOT NULL,

    frequency VARCHAR(50) NOT NULL CHECK (frequency IN ('DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY')),
    split_type VARCHAR(50) CHECK (split_type IN ('EQUAL', 'PERCENTAGE', 'FIXED')),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',

    start_date DATE NOT NULL,
    end_date DATE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_recurring_expenses_group
        FOREIGN KEY (group_id)
        REFERENCES groups(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_recurring_expenses_paid_by
        FOREIGN KEY (paid_by_id)
        REFERENCES users(id),

    CONSTRAINT fk_recurring_expenses_created_by
        FOREIGN KEY (created_by_id)
        REFERENCES users(id)
);

CREATE INDEX idx_recurring_expenses_group_id
    ON recurring_expenses(group_id);

CREATE INDEX idx_recurring_expenses_paid_by_id
    ON recurring_expenses(paid_by_id);

CREATE INDEX idx_recurring_expenses_created_by_id
    ON recurring_expenses(created_by_id);