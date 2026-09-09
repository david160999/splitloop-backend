-- V5__create_recurring_expense_participants.sql

CREATE TABLE recurring_expense_participants (
    id UUID PRIMARY KEY,

    recurring_expense_id UUID NOT NULL,
    user_id UUID NOT NULL,

    value DECIMAL(19,2),

    CONSTRAINT fk_recurring_expense_participants_expense
        FOREIGN KEY (recurring_expense_id)
        REFERENCES recurring_expenses(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_recurring_expense_participants_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT uq_recurring_expense_participant
        UNIQUE (recurring_expense_id, user_id)
);

CREATE INDEX idx_recurring_expense_participants_user_id
    ON recurring_expense_participants(user_id);