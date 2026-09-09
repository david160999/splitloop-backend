CREATE TABLE groups (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_by UUID,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_groups_created_by
        FOREIGN KEY (created_by)
        REFERENCES users(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_groups_created_by
    ON groups(created_by);