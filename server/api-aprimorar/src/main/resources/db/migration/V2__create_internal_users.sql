CREATE TABLE tb_internal_users (
    id UUID PRIMARY KEY,
    employee_id UUID NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_internal_users_employee FOREIGN KEY (employee_id) REFERENCES tb_employees (id)
);

CREATE INDEX idx_internal_users_employee_id ON tb_internal_users (employee_id);
CREATE INDEX idx_internal_users_username ON tb_internal_users (username);
