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
    ('11111111-1111-1111-1111-111111111111', 'Joao Silva Sauro', 'joao_sauro@email.com', '61988881111', '11122233344', NOW(), NOW()),
    ('22222222-2222-2222-2222-222222222222', 'Mauricio Pereira Souza', 'mauricio_souza@email.com', '11977772222', '55566677788', NOW(), NOW())
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
        '33333333-3333-3333-3333-333333333333',
        'Joao Silva',
        '61994562345',
        'joao.silva@email.com',
        DATE '2001-03-22',
        '12345678900',
        'Escola Estadual Sao Paulo',
        'ENEM',
        TRUE,
        '11111111-1111-1111-1111-111111111111',
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
        '44444444-4444-4444-4444-444444444444',
        'Marcelo Carvalho',
        '61994354221',
        'marcelo.carvalho@email.com',
        DATE '1999-05-18',
        '12344378900',
        'Leonardo da Vinci',
        'MENTORIA',
        TRUE,
        '22222222-2222-2222-2222-222222222222',
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
