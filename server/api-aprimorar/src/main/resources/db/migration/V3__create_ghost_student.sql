-- Insert Ghost Parent (Max UUID: ffff...ffff)
INSERT INTO tb_parent (id, name, email, contact, cpf, created_at)
VALUES ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'SISTEMA', 'sistema@aprimorar.com', '000000000', '00000000001', NOW())
ON CONFLICT (id) DO NOTHING;

-- Insert Ghost Student (Nil UUID: 0000...0000)
-- Using placeholders for address fields to satisfy NOT NULL constraints
INSERT INTO tb_students (id, name, contact, email, birthdate, cpf, school, parent_id, street, number, district, city, state, zip, created_at)
VALUES ('00000000-0000-0000-0000-000000000000', 'ALUNO ARQUIVADO', '000000000', 'arquivado@aprimorar.com', '2000-01-01', '00000000000', 'SISTEMA', 'ffffffff-ffff-ffff-ffff-ffffffffffff', 'SISTEMA', '0', 'SISTEMA', 'SISTEMA', 'DF', '00000000', NOW())
ON CONFLICT (id) DO NOTHING;
