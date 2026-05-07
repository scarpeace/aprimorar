CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE tb_transactions (
    id UUID PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    origin VARCHAR(50) NOT NULL,
    origin_id UUID NOT NULL,
    settled_at TIMESTAMP WITH TIME ZONE,
    category VARCHAR(50) NOT NULL
);

CREATE UNIQUE INDEX uk_transactions_origin_origin_id ON tb_transactions (origin, origin_id);
CREATE INDEX idx_transactions_category ON tb_transactions (category);
CREATE INDEX idx_transactions_status ON tb_transactions (status);
CREATE INDEX idx_transactions_settled_at ON tb_transactions (settled_at);
