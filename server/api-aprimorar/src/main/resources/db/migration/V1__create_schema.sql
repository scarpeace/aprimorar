CREATE TABLE IF NOT EXISTS tb_parent (
    parent_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    CONSTRAINT uk_parent_name UNIQUE (name),
    CONSTRAINT uk_parent_contact UNIQUE (contact),
    CONSTRAINT uk_parent_email UNIQUE (email),
    CONSTRAINT uk_parent_cpf UNIQUE (cpf)
);

CREATE TABLE IF NOT EXISTS tb_student (
    student_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    cpf VARCHAR(255) NOT NULL,
    school VARCHAR(255) NOT NULL,
    archived_at TIMESTAMPTZ,
    last_reactivated_at TIMESTAMPTZ,
    parent_id UUID NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    district VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip VARCHAR(255) NOT NULL,
    complement VARCHAR(255),
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    CONSTRAINT uk_student_name UNIQUE (name),
    CONSTRAINT uk_student_contact UNIQUE (contact),
    CONSTRAINT uk_student_email UNIQUE (email),
    CONSTRAINT uk_student_cpf UNIQUE (cpf),
    CONSTRAINT fk_student_parent FOREIGN KEY (parent_id) REFERENCES tb_parent(parent_id)
);

CREATE TABLE IF NOT EXISTS tb_employee (
    employee_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birthdate DATE,
    pix VARCHAR(255),
    contact VARCHAR(255),
    email VARCHAR(255),
    cpf VARCHAR(255),
    role VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    CONSTRAINT uk_employee_name UNIQUE (name),
    CONSTRAINT uk_employee_contact UNIQUE (contact),
    CONSTRAINT uk_employee_email UNIQUE (email),
    CONSTRAINT uk_employee_cpf UNIQUE (cpf)
);

CREATE TABLE IF NOT EXISTS tb_events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    start_date_time TIMESTAMP,
    end_date_time TIMESTAMP,
    price NUMERIC(19,2),
    payment NUMERIC(19,2),
    content VARCHAR(50) NOT NULL,
    student_id UUID,
    employee_id UUID,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    CONSTRAINT fk_event_student FOREIGN KEY (student_id) REFERENCES tb_student(student_id),
    CONSTRAINT fk_event_employee FOREIGN KEY (employee_id) REFERENCES tb_employee(employee_id)
);

CREATE INDEX IF NOT EXISTS idx_tb_student_archived_at ON tb_student (archived_at);
