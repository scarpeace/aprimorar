-- Development seed for Aprimorar.
-- Keeps the dataset small and aligned with the current schema.

DELETE FROM atendimentos;
DELETE FROM alunos WHERE id <> '00000000-0000-4000-8000-000000000002';
DELETE FROM responsaveis WHERE id <> 'ffffffff-ffff-ffff-ffff-ffffffffffff';
DELETE FROM colaboradores WHERE id <> '00000000-0000-4000-8000-000000000001';
DELETE FROM enderecos WHERE id NOT IN (-1, -2);

INSERT INTO enderecos (id, rua, numero, bairro, cidade, estado, cep, complemento) VALUES
  (101, 'Avenida Paulista', '1500', 'Bela Vista', 'Sao Paulo', 'SP', '01310200', 'Conjunto 801'),
  (102, 'Rua Harmonia', '245', 'Vila Madalena', 'Sao Paulo', 'SP', '05435000', 'Casa'),
  (103, 'Rua das Acacias', '88', 'Jardim Paulista', 'Sao Paulo', 'SP', '01407002', 'Apto 32'),
  (104, 'Rua Vergueiro', '2100', 'Vila Mariana', 'Sao Paulo', 'SP', '04102000', 'Bloco B'),
  (105, 'Alameda Santos', '900', 'Cerqueira Cesar', 'Sao Paulo', 'SP', '01418002', 'Sala 5'),
  (106, 'Rua Cardeal Arcoverde', '1200', 'Pinheiros', 'Sao Paulo', 'SP', '05408001', 'Casa 2'),
  (107, 'Rua Bela Cintra', '640', 'Consolacao', 'Sao Paulo', 'SP', '01415000', 'Apto 91'),
  (108, 'Rua Itapeva', '410', 'Bela Vista', 'Sao Paulo', 'SP', '01332000', NULL);

INSERT INTO colaboradores (
  id, user_id, endereco_id, nome, cpf, email, data_nascimento, telefone, funcao, pix, ativo, created_at, updated_at
) VALUES
  ('890322e5-6327-53c6-a9a7-726765d704d8', NULL, 101, 'Lucas Almeida', '123.456.789-09', 'lucas.almeida@example.com', DATE '2002-01-14', '11990000001', 'PROFESSOR', '11990000001', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('9e79c84d-d10a-59ca-8196-3963139e8096', NULL, 102, 'Mariana Costa', '987.654.321-00', 'mariana.costa@example.com', DATE '2001-05-27', '11990000002', 'MENTOR', '11990000002', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('46b8b924-f738-5a05-930c-1be3b563773e', NULL, 103, 'Aline Souza', '951.753.852-91', 'aline.souza@example.com', DATE '2003-10-24', '11990000003', 'TERAPEUTA', '11990000003', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO responsaveis (
  id, user_id, nome, data_nascimento, cpf, telefone, email, created_at, updated_at
) VALUES
  ('cbbe29c8-3177-548f-b3d3-24dd0463a495', NULL, 'Carlos Eduardo Ramos', DATE '1981-02-12', '204.681.357-07', '11980000001', 'carlos.ramos@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('7db99ca8-59f6-5b7d-8e85-465b2f62cb38', NULL, 'Marcos Vinicius Barros', DATE '1979-07-25', '315.792.468-00', '11980000002', 'marcos.barros@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('576f4f56-3232-5af8-a610-caa7a5b6f9dc', NULL, 'Felipe Andrade', DATE '1984-11-08', '426.813.579-00', '11980000003', 'felipe.andrade@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO alunos (
  id, user_id, responsavel_id, endereco_id, nome, cpf, email, data_nascimento, telefone, escola, ativo, created_at, updated_at
) VALUES
  ('df351208-d6c8-5c8a-a2ce-19cea64ac3a8', NULL, 'cbbe29c8-3177-548f-b3d3-24dd0463a495', 104, 'Joao Pedro Ramos', '120.345.678-62', 'joao.pedro.ramos@example.com', DATE '2008-04-12', '11970000001', 'Escola Monteiro Lobato', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('852b7c98-e262-51e7-88ab-84b93ea99f09', NULL, 'cbbe29c8-3177-548f-b3d3-24dd0463a495', 105, 'Ana Clara Ramos', '231.456.789-73', 'ana.clara.ramos@example.com', DATE '2012-09-03', '11970000002', 'Colegio Brasil', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('f0af156a-fbe5-5b29-9acb-5fe22508eb62', NULL, '7db99ca8-59f6-5b7d-8e85-465b2f62cb38', 106, 'Miguel Barros', '342.567.890-65', 'miguel.barros@example.com', DATE '2009-06-21', '11970000003', 'Escola Parque Aprimorar', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', NULL, '576f4f56-3232-5af8-a610-caa7a5b6f9dc', 107, 'Arthur Andrade', '564.789.012-11', 'arthur.andrade@example.com', DATE '2010-11-05', '11970000004', 'Colegio Sao Paulo', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('41e8e58f-124a-5969-b16f-ac57993d7a00', NULL, '7db99ca8-59f6-5b7d-8e85-465b2f62cb38', 108, 'Sofia Barros', '453.678.901-48', 'sofia.barros@example.com', DATE '2014-01-17', '11970000005', 'Colegio Sao Paulo', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO atendimentos (
  id,
  aluno_id,
  colaborador_id,
  data_hora_inicio,
  data_hora_fim,
  tipo,
  status,
  pagamento_aluno,
  repasse_colaborador,
  data_pagamento_aluno,
  data_pagamento_colaborador,
  created_at,
  updated_at
) VALUES
  (1001, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-07-02 14:00:00', TIMESTAMP '2026-07-02 15:30:00', 'AULA', 'AGENDADO', 150.00, 90.00, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1002, '852b7c98-e262-51e7-88ab-84b93ea99f09', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-07-03 10:00:00', TIMESTAMP '2026-07-03 11:00:00', 'TERAPIA', 'AGENDADO', 180.00, 120.00, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1003, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-07-04 16:00:00', TIMESTAMP '2026-07-04 17:30:00', 'MENTORIA', 'AGENDADO', 170.00, 110.00, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1004, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-07-05 09:00:00', TIMESTAMP '2026-07-05 10:30:00', 'ENEM', 'CONCLUIDO', 200.00, 130.00, TIMESTAMP '2026-07-05 11:00:00', TIMESTAMP '2026-07-06 12:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1005, '41e8e58f-124a-5969-b16f-ac57993d7a00', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-07-06 15:00:00', TIMESTAMP '2026-07-06 16:00:00', 'ORIENTACAO_VOCACIONAL', 'AGENDADO', 160.00, 100.00, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1006, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-07-08 18:00:00', TIMESTAMP '2026-07-08 19:00:00', 'PAS', 'CANCELADO', 140.00, 95.00, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1007, '852b7c98-e262-51e7-88ab-84b93ea99f09', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-07-10 14:00:00', TIMESTAMP '2026-07-10 15:00:00', 'OUTRO', 'AGENDADO', 130.00, 80.00, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1008, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-07-11 11:00:00', TIMESTAMP '2026-07-11 12:00:00', 'AULA', 'CONCLUIDO', 155.00, 90.00, TIMESTAMP '2026-07-11 12:30:00', TIMESTAMP '2026-07-12 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('enderecos_id_seq', (SELECT MAX(id) FROM enderecos), TRUE);
SELECT setval('atendimentos_id_seq', (SELECT MAX(id) FROM atendimentos), TRUE);
