CREATE TABLE protocols (
    id         UUID         PRIMARY KEY,
    title      VARCHAR(512) NOT NULL,
    author     VARCHAR(256),
    status     VARCHAR(32)  NOT NULL,
    content    JSONB        NOT NULL DEFAULT '{"sections": []}'::jsonb,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_protocols_updated_at ON protocols (updated_at DESC);
