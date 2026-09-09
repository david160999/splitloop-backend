-- V10__create_tokens_table.sql

CREATE TABLE tokens (
    id UUID PRIMARY KEY,

    token VARCHAR(512) NOT NULL UNIQUE,

    token_type VARCHAR(20) NOT NULL
        CHECK (token_type IN ('ACCESS', 'REFRESH')),

    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,

    user_id UUID NOT NULL,

    CONSTRAINT fk_tokens_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_tokens_user_id
    ON tokens(user_id);