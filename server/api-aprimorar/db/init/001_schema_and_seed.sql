CREATE TABLE IF NOT EXISTS tb_parent (
    parent_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL,
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
    activity VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
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

INSERT INTO tb_parent (parent_id, name, email, contact, cpf, created_at, updated_at)
VALUES
    ('3f1c2d4e-5a6b-7c8d-9e01-23456789ab10',
     'Joao Silva Sauro',
     'joao_sauro@email.com',
     '61988881111',
     '11122233344',
     NOW(),
     NOW()),
    ('7a8b9c0d-1e2f-3a4b-5c6d-7e8f9012ab34',
     'Mauricio Pereira Souza',
     'mauricio_souza@email.com',
     '11977772222',
     '55566677788',
     NOW(),
     NOW())
ON CONFLICT (parent_id) DO NOTHING;

INSERT INTO tb_student (
    student_id,
    name,
    contact,
    email,
    birthdate,
    cpf,
    school,
    activity,
    active,
    parent_id,
    street,
    number,
    district,
    city,
    state,
    zip,
    complement,
    created_at,
    updated_at
)
VALUES
    (
        'b12c3d4e-5f67-4890-9abc-def012345678',
        'Joao Silva',
        '61994562345',
        'joao.silva@email.com',
        DATE '2001-03-22',
        '12345678900',
        'Escola Estadual Sao Paulo',
        'ENEM',
        TRUE,
        '3f1c2d4e-5a6b-7c8d-9e01-23456789ab10',
        'Rua das Flores',
        '123',
        'Centro',
        'Sao Paulo',
        'SP',
        '01234567',
        'Apto 45',
        NOW(),
        NOW()
    ),
    (
        'c98d7e6f-5a4b-3c2d-1e0f-abcdef123456',
        'Marcelo Carvalho',
        '61994354221',
        'marcelo.carvalho@email.com',
        DATE '1999-05-18',
        '12344378900',
        'Leonardo da Vinci',
        'MENTORIA',
        TRUE,
        '7a8b9c0d-1e2f-3a4b-5c6d-7e8f9012ab34',
        'Servidao Bertolina',
        '230',
        'Barra da Lagoa',
        'Florianopolis',
        'SC',
        '01234245',
        'Casa',
        NOW(),
        NOW()
    )
ON CONFLICT (student_id) DO NOTHING;

CREATE TABLE IF NOT EXISTS tb_employee (
    employee_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birthdate VARCHAR(255),
    pix VARCHAR(255),
    contact VARCHAR(255),
    cpf VARCHAR(255),
    role VARCHAR(255),
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS tb_events (
    id BIGSERIAL PRIMARY KEY,
    start_date_time TIMESTAMP,
    end_date_time TIMESTAMP,
    price NUMERIC(19,2),
    payment NUMERIC(19,2),
    student_id UUID,
    employee_id UUID,
    CONSTRAINT fk_event_student FOREIGN KEY (student_id) REFERENCES tb_student(student_id),
    CONSTRAINT fk_event_employee FOREIGN KEY (employee_id) REFERENCES tb_employee(employee_id)
);

INSERT INTO tb_employee (employee_id, name, birthdate, contact, pix, cpf, role, created_at, updated_at)
VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890',
     'Marcelo Carvalho',
     '22-03-1992',
     '(61) 99963-5543',
     '023.205.102-23',
     '023.205.102-23',
     'EMPLOYEE',
     NOW(),
     NOW())
ON CONFLICT (employee_id) DO NOTHING;

INSERT INTO tb_events (start_date_time, end_date_time, price, payment, student_id, employee_id)
VALUES
    ('2026-12-11 12:30:00',
     '2026-12-11 13:30:00',
     250.75,
     200.00,
     'b12c3d4e-5f67-4890-9abc-def012345678',
     'a1b2c3d4-e5f6-7890-abcd-ef1234567890');
