-- Migration to rename tb_internal_users to tb_users
ALTER TABLE tb_internal_users RENAME TO tb_users;

-- Rename constraints
ALTER TABLE tb_users RENAME CONSTRAINT fk_internal_users_employee TO fk_users_employee;

-- Rename indices
ALTER INDEX idx_internal_users_employee_id RENAME TO idx_users_employee_id;
ALTER INDEX idx_internal_users_username RENAME TO idx_users_username;
