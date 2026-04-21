-- V4__financial_core.sql
ALTER TABLE tb_events ADD COLUMN income_status VARCHAR(50) NOT NULL DEFAULT 'PENDING';
ALTER TABLE tb_events ADD COLUMN expense_status VARCHAR(50) NOT NULL DEFAULT 'PENDING';

CREATE TABLE tb_general_expenses (
    id UUID PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    date DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
