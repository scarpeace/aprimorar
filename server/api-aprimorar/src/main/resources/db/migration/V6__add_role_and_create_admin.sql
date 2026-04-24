-- Migration to add role to users and create the first ADMIN
ALTER TABLE tb_users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE';

-- Create employee for Gustavo
-- Columns based on V1: id, name, birthdate, pix, contact, cpf, email, duty, created_at, updated_at
INSERT INTO tb_employees (id, name, birthdate, pix, contact, cpf, email, duty, created_at, updated_at)
VALUES (
    'a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6',
    'Gustavo Scarpellini',
    '1990-01-01',
    '00000000000',
    '00000000000',
    '00000000000',
    'gustavo.scarpellini@aprimorar.dev',
    'SYSTEM',
    NOW(),
    NOW()
);

-- Create ADMIN user (password: freerider)
-- Columns: id, employee_id, username, password_hash, active, role, created_at, updated_at
INSERT INTO tb_users (id, employee_id, username, password_hash, active, role, created_at, updated_at)
VALUES (
    'f1e2d3c4-b5a6-9f8e-7d6c-5b4a3f2e1d0c',
    'a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6',
    'gustavo.scarpellini',
    '$2a$10$N0kH6jD1KpAMqEwwoNN.QOjFVF3IctMqF8EptiYUEHkk5tLlSoJj.', -- freerider
    true,
    'ADMIN',
    NOW(),
    NOW()
);
