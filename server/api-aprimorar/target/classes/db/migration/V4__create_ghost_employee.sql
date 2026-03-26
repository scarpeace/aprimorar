INSERT INTO tb_employees (id, name, email, birthdate, cpf, duty, pix, contact, archived_at, created_at, updated_at)
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
)
ON CONFLICT (id) DO NOTHING;
