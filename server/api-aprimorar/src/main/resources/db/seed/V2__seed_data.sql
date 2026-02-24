INSERT INTO tb_parent (
    parent_id,
    name,
    email,
    contact,
    cpf,
    created_at,
    updated_at)
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
    ('b12c3d4e-5f67-4890-9abc-def012345678',
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
    ('c98d7e6f-5a4b-3c2d-1e0f-abcdef123456',
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

INSERT INTO tb_employee (employee_id, name, birthdate, contact, pix, cpf, email, role, created_at, updated_at)
VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890',
     'Marcelo Carvalho',
     DATE '1992-03-22',
     '(61) 99963-5543',
     '023.205.102-23',
     '02320510223',
     'marcelo@gmail.com',
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
