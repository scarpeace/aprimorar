-- V1__initial_schema.sql

CREATE TABLE tb_parent (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    contact VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    archived_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE tb_employees (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    pix VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    duty VARCHAR(50) NOT NULL,
    archived_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE tb_students (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    birthdate DATE NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    school VARCHAR(255) NOT NULL,
    parent_id UUID NOT NULL,
    street VARCHAR(255) NOT NULL,
    district VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip VARCHAR(255) NOT NULL,
    complement VARCHAR(255),
    archived_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_students_parent FOREIGN KEY (parent_id) REFERENCES tb_parent (id)
);

-- Como Event agora usa Instant, prefira WITH TIME ZONE
CREATE TABLE tb_events (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    payment NUMERIC(19, 2) NOT NULL,
    content VARCHAR(50) NOT NULL,
    student_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    archived_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_events_student FOREIGN KEY (student_id) REFERENCES tb_students (id),
    CONSTRAINT fk_events_employee FOREIGN KEY (employee_id) REFERENCES tb_employees (id)
);

CREATE INDEX idx_students_parent_id ON tb_students (parent_id);
CREATE INDEX idx_events_student_id ON tb_events (student_id);
CREATE INDEX idx_events_employee_id ON tb_events (employee_id);
CREATE INDEX idx_events_start_date_time ON tb_events (start_date_time);

-- Ghost parent
INSERT INTO tb_parent (id, name, email, contact, cpf, created_at, archived_at)
VALUES ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'SISTEMA', 'sistema@aprimorar.com', '000000000', '00000000001', NOW(), NOW());

-- Ghost student
INSERT INTO tb_students (
    id, name, contact, email, birthdate, cpf, school, parent_id,
    street, district, city, state, zip, created_at, archived_at
)
VALUES (
    '00000000-0000-0000-0000-000000000000',
    'Aluno Removido',
    '000000000',
    'arquivado@aprimorar.com',
    '2000-01-01',
    '00000000000',
    'SISTEMA',
    'ffffffff-ffff-ffff-ffff-ffffffffffff',
    'SISTEMA',
    'SISTEMA',
    'SISTEMA',
    'DF',
    '00000000',
    NOW(),
    NULL
);

-- Ghost employee
INSERT INTO tb_employees (
    id, name, email, birthdate, cpf, duty, pix, contact, archived_at, created_at, updated_at
)
VALUES (
    '00000000-0000-4000-8000-000000000001',
    'Colaborador Removido',
    'colaborador.removido@aprimorar.com',
    '1900-01-01',
    '00000000002',
    'SYSTEM',
    'N/A',
    '000000000',
    NULL,
    NOW(),
    NOW()
);
