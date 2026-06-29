CREATE TABLE users (
  id UUID NOT NULL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(32) NOT NULL CHECK (role IN ('ALUNO', 'COLABORADOR', 'RESPONSAVEL', 'ADMIN', 'SISTEMA')),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE enderecos (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  rua VARCHAR(255) NOT NULL,
  numero VARCHAR(10) NOT NULL,
  bairro VARCHAR(255) NOT NULL,
  cidade VARCHAR(255) NOT NULL,
  estado VARCHAR(2) NOT NULL CHECK (estado IN ('AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO')),
  cep VARCHAR(8) NOT NULL,
  complemento VARCHAR(255)
);

CREATE TABLE responsaveis (
  id UUID NOT NULL PRIMARY KEY,
  user_id UUID UNIQUE REFERENCES users(id),
  nome VARCHAR(50) NOT NULL,
  data_nascimento DATE,
  cpf VARCHAR(255) NOT NULL UNIQUE,
  telefone VARCHAR(20) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE alunos (
  id UUID NOT NULL PRIMARY KEY,
  user_id UUID UNIQUE REFERENCES users(id),
  responsavel_id UUID NOT NULL REFERENCES responsaveis(id),
  endereco_id BIGINT NOT NULL UNIQUE REFERENCES enderecos(id),
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  data_nascimento DATE NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  escola VARCHAR(255),
  ativo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_alunos_nome ON alunos(nome);
CREATE INDEX idx_alunos_escola ON alunos(escola);
CREATE INDEX idx_alunos_responsavel_id ON alunos(responsavel_id);

CREATE TABLE colaboradores (
  id UUID NOT NULL PRIMARY KEY,
  user_id UUID UNIQUE REFERENCES users(id),
  endereco_id BIGINT NOT NULL UNIQUE REFERENCES enderecos(id),
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  data_nascimento DATE NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  funcao VARCHAR(100) NOT NULL CHECK (funcao IN ('PROFESSOR', 'ADMINISTRATIVO', 'TERAPEUTA', 'MENTOR')),
  pix VARCHAR(255) NOT NULL,
  ativo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_colaboradores_nome ON colaboradores(nome);
CREATE INDEX idx_colaboradores_funcao ON colaboradores(funcao);

CREATE TABLE atendimentos (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  aluno_id UUID NOT NULL REFERENCES alunos(id),
  colaborador_id UUID NOT NULL REFERENCES colaboradores(id),
  data_hora_inicio TIMESTAMP NOT NULL,
  data_hora_fim TIMESTAMP NOT NULL,
  tipo VARCHAR(255) NOT NULL CHECK (tipo IN ('AULA','MENTORIA','TERAPIA','ORIENTACAO_VOCACIONAL','ENEM','PAS','OUTRO')),
  status VARCHAR(20) NOT NULL CHECK (status IN ('AGENDADO','CONCLUIDO','CANCELADO')),
  pagamento_aluno NUMERIC(10,2) NOT NULL CHECK (pagamento_aluno >= 0),
  repasse_colaborador NUMERIC(10,2) NOT NULL CHECK (repasse_colaborador >= 0),
  data_pagamento_aluno TIMESTAMP,
  data_pagamento_colaborador TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_atendimentos_tipo ON atendimentos(tipo);
CREATE INDEX idx_atendimentos_status ON atendimentos(status);

-- Contas inativas sem hash BCrypt utilizavel, exclusivas dos registros removidos.
INSERT INTO users (id, username, password, role, active) VALUES
  ('ffffffff-ffff-ffff-ffff-fffffffffffe', 'responsavel.removido@aprimorar.local', '!', 'SISTEMA', FALSE),
  ('00000000-0000-4000-8000-000000000102', 'aluno.removido@aprimorar.local', '!', 'SISTEMA', FALSE),
  ('00000000-0000-4000-8000-000000000101', 'colaborador.removido@aprimorar.local', '!', 'SISTEMA', FALSE);

-- IDs negativos mantem os registros reservados fora da sequencia BIGSERIAL.
INSERT INTO enderecos (id, rua, numero, bairro, cidade, estado, cep, complemento) VALUES
  (-1, 'Sistema', '0', 'Sistema', 'Sistema', 'SP', '00000000', NULL),
  (-2, 'Sistema', '0', 'Sistema', 'Sistema', 'SP', '00000000', NULL);

INSERT INTO responsaveis (id, user_id, nome, data_nascimento, cpf, telefone, email) VALUES (
  'ffffffff-ffff-ffff-ffff-ffffffffffff',
  'ffffffff-ffff-ffff-ffff-fffffffffffe',
  'Responsavel Removido',
  DATE '2000-01-01',
  '000.000.000-00',
  '00000000000',
  'responsavel.removido@aprimorar.local'
);

INSERT INTO alunos (id, user_id, responsavel_id, endereco_id, nome, cpf, email, data_nascimento, telefone, escola, ativo) VALUES (
  '00000000-0000-4000-8000-000000000002',
  '00000000-0000-4000-8000-000000000102',
  'ffffffff-ffff-ffff-ffff-ffffffffffff',
  -2,
  'Aluno Removido',
  '000.000.000-02',
  'aluno.removido@aprimorar.local',
  DATE '2000-01-01',
  '00000000002',
  'Sistema',
  FALSE
);

INSERT INTO colaboradores (id, user_id, endereco_id, nome, cpf, email, data_nascimento, telefone, funcao, pix, ativo) VALUES (
  '00000000-0000-4000-8000-000000000001',
  '00000000-0000-4000-8000-000000000101',
  -1,
  'Colaborador Removido',
  '000.000.000-01',
  'colaborador.removido@aprimorar.local',
  DATE '2000-01-01',
  '00000000001',
  'ADMINISTRATIVO',
  '00000000001',
  FALSE
);
