-- Development seed for Aprimorar.
-- Keeps the dataset small and aligned with the current schema.

DELETE FROM repasses_individuais;
DELETE FROM cobrancas_individuais;
DELETE FROM atendimentos_individuais;
DELETE FROM despesas;
DELETE FROM alunos;
DELETE FROM colaboradores;

INSERT INTO colaboradores (
  id,
  endereco_rua, endereco_numero, endereco_bairro, endereco_cidade, endereco_estado, endereco_cep, endereco_complemento,
  nome, cpf, email, data_nascimento, telefone, funcao, pix, ativo, created_at, updated_at
) VALUES
  ('890322e5-6327-53c6-a9a7-726765d704d8', 'Avenida Paulista', '1500', 'Bela Vista', 'Sao Paulo', 'SP', '01310200', 'Conjunto 801', 'Lucas Almeida', '123.456.789-09', 'lucas.almeida@example.com', DATE '2002-01-14', '11990000001', 'PROFESSOR', '11990000001', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('9e79c84d-d10a-59ca-8196-3963139e8096', 'Rua Harmonia', '245', 'Vila Madalena', 'Sao Paulo', 'SP', '05435000', 'Casa', 'Mariana Costa', '987.654.321-00', 'mariana.costa@example.com', DATE '2001-05-27', '11990000002', 'MENTOR', '11990000002', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('46b8b924-f738-5a05-930c-1be3b563773e', 'Rua das Acacias', '88', 'Jardim Paulista', 'Sao Paulo', 'SP', '01407002', 'Apto 32', 'Aline Souza', '951.753.852-91', 'aline.souza@example.com', DATE '2003-10-24', '11990000003', 'TERAPEUTA', '11990000003', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO alunos (
  id,
  responsavel_nome, responsavel_cpf, responsavel_telefone, responsavel_email,
  endereco_rua, endereco_numero, endereco_bairro, endereco_cidade, endereco_estado, endereco_cep, endereco_complemento,
  nome, cpf, email, data_nascimento, telefone, escola, ativo, created_at, updated_at
) VALUES
  ('df351208-d6c8-5c8a-a2ce-19cea64ac3a8', 'Carlos Eduardo Ramos', '204.681.357-07', '11980000001', 'carlos.ramos@example.com', 'Rua Vergueiro', '2100', 'Vila Mariana', 'Sao Paulo', 'SP', '04102000', 'Bloco B', 'Joao Pedro Ramos', '120.345.678-62', 'joao.pedro.ramos@example.com', DATE '2008-04-12', '11970000001', 'Escola Monteiro Lobato', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('852b7c98-e262-51e7-88ab-84b93ea99f09', 'Carlos Eduardo Ramos', '204.681.357-07', '11980000001', 'carlos.ramos@example.com', 'Alameda Santos', '900', 'Cerqueira Cesar', 'Sao Paulo', 'SP', '01418002', 'Sala 5', 'Ana Clara Ramos', '231.456.789-73', 'ana.clara.ramos@example.com', DATE '2012-09-03', '11970000002', 'Colegio Brasil', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('f0af156a-fbe5-5b29-9acb-5fe22508eb62', 'Marcos Vinicius Barros', '315.792.468-00', '11980000002', 'marcos.barros@example.com', 'Rua Cardeal Arcoverde', '1200', 'Pinheiros', 'Sao Paulo', 'SP', '05408001', 'Casa 2', 'Miguel Barros', '342.567.890-65', 'miguel.barros@example.com', DATE '2009-06-21', '11970000003', 'Escola Parque Aprimorar', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', 'Felipe Andrade', '426.813.579-00', '11980000003', 'felipe.andrade@example.com', 'Rua Bela Cintra', '640', 'Consolacao', 'Sao Paulo', 'SP', '01415000', 'Apto 91', 'Arthur Andrade', '564.789.012-11', 'arthur.andrade@example.com', DATE '2010-11-05', '11970000004', 'Colegio Sao Paulo', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('41e8e58f-124a-5969-b16f-ac57993d7a00', 'Marcos Vinicius Barros', '315.792.468-00', '11980000002', 'marcos.barros@example.com', 'Rua Itapeva', '410', 'Bela Vista', 'Sao Paulo', 'SP', '01332000', NULL, 'Sofia Barros', '453.678.901-48', 'sofia.barros@example.com', DATE '2014-01-17', '11970000005', 'Colegio Sao Paulo', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

DROP TABLE IF EXISTS seed_atendimentos;

CREATE TEMP TABLE seed_atendimentos (
  id BIGINT,
  aluno_id UUID,
  colaborador_id UUID,
  data_hora_inicio TIMESTAMP,
  data_hora_fim TIMESTAMP,
  tipo VARCHAR(255),
  repasse NUMERIC(10, 2),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);

INSERT INTO seed_atendimentos (
  id,
  aluno_id,
  colaborador_id,
  data_hora_inicio,
  data_hora_fim,
  tipo,
  repasse,
  created_at,
  updated_at
) VALUES
  (1001, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-07-02 14:00:00', TIMESTAMP '2026-07-02 15:30:00', 'AULA', 90.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1002, '852b7c98-e262-51e7-88ab-84b93ea99f09', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-07-03 10:00:00', TIMESTAMP '2026-07-03 11:00:00', 'TERAPIA', 120.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1003, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-07-04 16:00:00', TIMESTAMP '2026-07-04 17:30:00', 'MENTORIA', 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1004, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-07-05 09:00:00', TIMESTAMP '2026-07-05 10:30:00', 'ENEM', 130.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1005, '41e8e58f-124a-5969-b16f-ac57993d7a00', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-07-06 15:00:00', TIMESTAMP '2026-07-06 16:00:00', 'ORIENTACAO_VOCACIONAL', 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1006, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-07-08 18:00:00', TIMESTAMP '2026-07-08 19:00:00', 'PAS', 95.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1007, '852b7c98-e262-51e7-88ab-84b93ea99f09', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-07-10 14:00:00', TIMESTAMP '2026-07-10 15:00:00', 'OUTRO', 80.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1008, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-07-11 11:00:00', TIMESTAMP '2026-07-11 12:00:00', 'AULA', 90.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1009, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-07-15 13:00:00', TIMESTAMP '2026-07-15 14:00:00', 'MENTORIA', 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1010, '41e8e58f-124a-5969-b16f-ac57993d7a00', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-07-18 08:00:00', TIMESTAMP '2026-07-18 09:00:00', 'AULA', 90.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1011, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-07-22 17:00:00', TIMESTAMP '2026-07-22 18:00:00', 'TERAPIA', 120.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1012, '852b7c98-e262-51e7-88ab-84b93ea99f09', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-07-25 09:30:00', TIMESTAMP '2026-07-25 10:30:00', 'PAS', 95.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1013, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-08-01 10:00:00', TIMESTAMP '2026-08-01 11:30:00', 'ENEM', 130.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1014, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-08-03 14:00:00', TIMESTAMP '2026-08-03 15:00:00', 'TERAPIA', 120.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1015, '41e8e58f-124a-5969-b16f-ac57993d7a00', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-08-06 16:00:00', TIMESTAMP '2026-08-06 17:00:00', 'MENTORIA', 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1016, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-08-08 11:00:00', TIMESTAMP '2026-08-08 12:00:00', 'AULA', 90.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1017, '852b7c98-e262-51e7-88ab-84b93ea99f09', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-08-12 15:00:00', TIMESTAMP '2026-08-12 16:30:00', 'ORIENTACAO_VOCACIONAL', 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1018, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-08-18 18:00:00', TIMESTAMP '2026-08-18 19:00:00', 'OUTRO', 80.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1019, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-08-24 08:30:00', TIMESTAMP '2026-08-24 10:00:00', 'ENEM', 130.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1020, '41e8e58f-124a-5969-b16f-ac57993d7a00', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-08-29 10:00:00', TIMESTAMP '2026-08-29 11:00:00', 'AULA', 90.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1021, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-09-02 14:00:00', TIMESTAMP '2026-09-02 15:00:00', 'MENTORIA', 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1022, '852b7c98-e262-51e7-88ab-84b93ea99f09', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-09-05 09:00:00', TIMESTAMP '2026-09-05 10:30:00', 'AULA', 90.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1023, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-09-09 13:00:00', TIMESTAMP '2026-09-09 14:00:00', 'TERAPIA', 120.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1024, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-09-14 17:00:00', TIMESTAMP '2026-09-14 18:00:00', 'PAS', 95.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1025, '41e8e58f-124a-5969-b16f-ac57993d7a00', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-09-18 15:00:00', TIMESTAMP '2026-09-18 16:00:00', 'OUTRO', 80.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1026, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-09-23 10:00:00', TIMESTAMP '2026-09-23 11:30:00', 'ORIENTACAO_VOCACIONAL', 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1027, '852b7c98-e262-51e7-88ab-84b93ea99f09', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-10-01 08:00:00', TIMESTAMP '2026-10-01 09:00:00', 'MENTORIA', 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1028, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-10-05 11:00:00', TIMESTAMP '2026-10-05 12:30:00', 'ENEM', 130.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1029, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-10-09 16:00:00', TIMESTAMP '2026-10-09 17:00:00', 'TERAPIA', 120.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1030, '41e8e58f-124a-5969-b16f-ac57993d7a00', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-10-14 14:00:00', TIMESTAMP '2026-10-14 15:00:00', 'PAS', 95.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1031, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-10-21 09:30:00', TIMESTAMP '2026-10-21 10:30:00', 'AULA', 90.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1032, '852b7c98-e262-51e7-88ab-84b93ea99f09', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-10-27 18:00:00', TIMESTAMP '2026-10-27 19:00:00', 'OUTRO', 80.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1033, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-11-03 10:00:00', TIMESTAMP '2026-11-03 11:00:00', 'MENTORIA', 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1034, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-11-07 13:00:00', TIMESTAMP '2026-11-07 14:30:00', 'ENEM', 130.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1035, '41e8e58f-124a-5969-b16f-ac57993d7a00', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-11-12 15:00:00', TIMESTAMP '2026-11-12 16:00:00', 'TERAPIA', 120.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1036, 'df351208-d6c8-5c8a-a2ce-19cea64ac3a8', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-11-18 17:00:00', TIMESTAMP '2026-11-18 18:00:00', 'PAS', 95.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1037, '852b7c98-e262-51e7-88ab-84b93ea99f09', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-11-24 08:00:00', TIMESTAMP '2026-11-24 09:00:00', 'AULA', 90.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1038, 'f0af156a-fbe5-5b29-9acb-5fe22508eb62', '46b8b924-f738-5a05-930c-1be3b563773e', TIMESTAMP '2026-12-02 11:00:00', TIMESTAMP '2026-12-02 12:00:00', 'ORIENTACAO_VOCACIONAL', 100.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1039, 'de87ab23-c4f6-5cdb-88e4-c1e524f9f5b3', '9e79c84d-d10a-59ca-8196-3963139e8096', TIMESTAMP '2026-12-08 14:00:00', TIMESTAMP '2026-12-08 15:00:00', 'MENTORIA', 110.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1040, '41e8e58f-124a-5969-b16f-ac57993d7a00', '890322e5-6327-53c6-a9a7-726765d704d8', TIMESTAMP '2026-12-15 16:00:00', TIMESTAMP '2026-12-15 17:30:00', 'ENEM', 130.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO atendimentos_individuais (
  id,
  aluno_id,
  colaborador_id,
  data_hora_inicio,
  data_hora_fim,
  tipo,
  created_at,
  updated_at
)
SELECT id, aluno_id, colaborador_id, data_hora_inicio, data_hora_fim, tipo,
  created_at, updated_at
FROM seed_atendimentos;

INSERT INTO repasses_individuais (
  atendimento_id,
  colaborador_id,
  valor,
  created_at,
  updated_at
)
SELECT id, colaborador_id, repasse, created_at, updated_at
FROM seed_atendimentos;

DROP TABLE seed_atendimentos;

-- Cobranças fictícias para os atendimentos acima.
-- Parte delas fica paga para permitir testar os dois estados na consulta composta.
INSERT INTO cobrancas_individuais (
  atendimento_id,
  aluno_id,
  valor,
  status,
  comprovante_url,
  data_pagamento,
  forma_pagamento,
  created_at,
  updated_at
)
SELECT
  atendimento.id,
  atendimento.aluno_id,
  repasse.valor + 40.00,
  CASE WHEN atendimento.id % 4 = 0 THEN 'PAGO' ELSE 'PENDENTE' END,
  CASE WHEN atendimento.id % 4 = 0
    THEN 'https://example.com/comprovantes/cobranca-' || atendimento.id || '.pdf'
    ELSE NULL
  END,
  CASE WHEN atendimento.id % 4 = 0
    THEN atendimento.data_hora_fim + INTERVAL '1 hour'
    ELSE NULL
  END,
  CASE WHEN atendimento.id % 4 = 0 THEN 'PIX' ELSE NULL END,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
FROM atendimentos_individuais atendimento
JOIN repasses_individuais repasse ON repasse.atendimento_id = atendimento.id;

INSERT INTO despesas (
  id,
  titulo,
  tipo,
  categoria,
  valor,
  data_vencimento,
  data_pagamento,
  status,
  forma_pagamento,
  descricao,
  created_at,
  updated_at
) VALUES
  (1, 'Conta de energia - julho', 'SAIDA', 'CONTAS', 620.45, DATE '2026-07-05', DATE '2026-07-05', 'PAGA', 'PIX', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'Conta de agua - julho', 'SAIDA', 'CONTAS', 185.30, DATE '2026-07-06', DATE '2026-07-06', 'PAGA', 'BOLETO', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'Internet - julho', 'SAIDA', 'ASSINATURAS', 149.90, DATE '2026-07-07', DATE '2026-07-07', 'PAGA', 'CARTAO_CREDITO', 'Plano fibra', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'Material de limpeza', 'SAIDA', 'DESPENSA', 238.70, DATE '2026-07-09', DATE '2026-07-09', 'PAGA', 'CARTAO_DEBITO', 'Reposicao mensal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, 'Manutencao ar condicionado', 'SAIDA', 'MANUTENCAO', 480.00, DATE '2026-07-10', NULL, 'ATRASADA', 'PIX', 'Orcamento aprovado', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (6, 'Material de apoio para professores', 'SAIDA', 'PROFESSORES', 720.00, DATE '2026-07-15', DATE '2026-07-15', 'PAGA', 'TRANSFERENCIA', 'Compra de materiais pedagogicos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (7, 'Treinamento da equipe docente', 'SAIDA', 'PROFESSORES', 620.00, DATE '2026-07-18', DATE '2026-07-18', 'PAGA', 'TRANSFERENCIA', 'Capacitacao mensal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (8, 'Servico de contabilidade', 'SAIDA', 'SERVICOS', 350.00, DATE '2026-07-15', NULL, 'ATRASADA', 'BOLETO', 'Mensalidade julho', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (9, 'Uniformes da equipe', 'SAIDA', 'FUNCIONARIOS', 1800.00, DATE '2026-07-30', DATE '2026-07-30', 'PAGA', 'TRANSFERENCIA', 'Compra de uniformes internos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (10, 'Conta de energia - agosto', 'SAIDA', 'CONTAS', 645.10, DATE '2026-08-10', NULL, 'ATRASADA', 'PIX', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (11, 'Conta de agua - agosto', 'SAIDA', 'CONTAS', 192.80, DATE '2026-08-06', DATE '2026-08-06', 'PAGA', 'BOLETO', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (12, 'Internet - agosto', 'SAIDA', 'ASSINATURAS', 149.90, DATE '2026-08-07', DATE '2026-08-07', 'PAGA', 'CARTAO_CREDITO', 'Plano fibra', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (13, 'Cafe e descartaveis', 'SAIDA', 'DESPENSA', 312.40, DATE '2026-08-10', DATE '2026-08-10', 'PAGA', 'CARTAO_DEBITO', 'Consumo interno', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (14, 'Pintura sala de estudos', 'SAIDA', 'MANUTENCAO', 950.00, DATE '2026-08-25', NULL, 'ATRASADA', 'PIX', 'Melhoria de ambiente', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (15, 'Livros para sala dos professores', 'SAIDA', 'PROFESSORES', 540.00, DATE '2026-08-20', DATE '2026-08-20', 'PAGA', 'TRANSFERENCIA', 'Acervo de apoio pedagogico', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (16, 'Sistema de gestao', 'SAIDA', 'ASSINATURAS', 89.90, DATE '2026-09-01', DATE '2026-09-01', 'PAGA', 'CARTAO_CREDITO', 'Assinatura mensal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (17, 'Conta de energia - setembro', 'SAIDA', 'CONTAS', 610.25, DATE '2026-09-05', DATE '2026-09-05', 'PAGA', 'PIX', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (18, 'Conta de agua - setembro', 'SAIDA', 'CONTAS', 176.45, DATE '2026-09-12', NULL, 'PENDENTE', 'BOLETO', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (19, 'Produtos de copa', 'SAIDA', 'DESPENSA', 220.15, DATE '2026-09-11', DATE '2026-09-11', 'PAGA', 'CARTAO_DEBITO', 'Reposicao quinzenal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (20, 'Servico eletricista', 'SAIDA', 'SERVICOS', 420.00, DATE '2026-09-16', DATE '2026-09-16', 'PAGA', 'PIX', 'Ajuste em tomadas', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (21, 'Material administrativo da equipe', 'SAIDA', 'FUNCIONARIOS', 2100.00, DATE '2026-09-20', NULL, 'PENDENTE', 'TRANSFERENCIA', 'Itens de uso interno', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (22, 'Workshop para professores', 'SAIDA', 'PROFESSORES', 810.00, DATE '2026-09-25', DATE '2026-09-25', 'PAGA', 'TRANSFERENCIA', 'Evento de capacitacao', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (23, 'Conta de energia - outubro', 'SAIDA', 'CONTAS', 635.90, DATE '2026-10-05', NULL, 'PENDENTE', 'PIX', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (24, 'Conta de agua - outubro', 'SAIDA', 'CONTAS', 181.20, DATE '2026-10-06', DATE '2026-10-06', 'PAGA', 'BOLETO', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (25, 'Licenca de software', 'SAIDA', 'ASSINATURAS', 129.90, DATE '2026-10-08', DATE '2026-10-08', 'PAGA', 'CARTAO_CREDITO', 'Ferramenta administrativa', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (26, 'Conserto fechadura', 'SAIDA', 'MANUTENCAO', 160.00, DATE '2026-10-13', DATE '2026-10-13', 'PAGA', 'PIX', 'Porta da sala 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (27, 'Servico de limpeza extra', 'SAIDA', 'SERVICOS', 300.00, DATE '2026-10-15', NULL, 'PENDENTE', 'PIX', 'Limpeza pos-evento', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (28, 'Equipamentos para equipe administrativa', 'SAIDA', 'FUNCIONARIOS', 1700.00, DATE '2026-10-30', DATE '2026-10-30', 'PAGA', 'TRANSFERENCIA', 'Reposicao de perifericos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (29, 'Conta de energia - novembro', 'SAIDA', 'CONTAS', 658.75, DATE '2026-11-05', NULL, 'PENDENTE', 'PIX', 'Unidade principal', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (30, 'Materiais pedagogicos', 'SAIDA', 'DESPENSA', 410.60, DATE '2026-11-09', DATE '2026-11-09', 'PAGA', 'CARTAO_DEBITO', 'Apoio as aulas', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('atendimentos_individuais_id_seq', (SELECT MAX(id) FROM atendimentos_individuais), TRUE);
SELECT setval('cobrancas_individuais_id_seq', (SELECT MAX(id) FROM cobrancas_individuais), TRUE);
SELECT setval('repasses_individuais_id_seq', (SELECT MAX(id) FROM repasses_individuais), TRUE);
SELECT setval('despesas_id_seq', (SELECT MAX(id) FROM despesas), TRUE);
