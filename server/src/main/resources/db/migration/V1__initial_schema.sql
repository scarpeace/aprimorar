CREATE TABLE IF NOT EXISTS users (
  id UUID NOT NULL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(32) NOT NULL CHECK (role in ('STUDENT', 'EMPLOYEE', 'PARENT', 'ADMIN')),
  active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS responsaveis (
  id UUID NOT NULL PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(255) NOT NULL UNIQUE,
  data_nascimento DATE,
  telefone VARCHAR(20) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  user_id UUID UNIQUE REFERENCES users(id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS alunos (
  id UUID NOT NULL PRIMARY KEY,
  responsavel_id UUID NOT NULL REFERENCES responsaveis(id),
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  data_nascimento DATE NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  escola VARCHAR(255),
  ativo boolean NOT NULL DEFAULT TRUE,
  user_id UUID UNIQUE REFERENCES users(id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,

  rua VARCHAR(255) NOT NULL,
  numero VARCHAR(10) NOT NULL,
  bairro VARCHAR(255) NOT NULL,
  cidade VARCHAR(255) NOT NULL,
  estado VARCHAR(2) NOT NULL CHECK (estado in ('AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO')),
  cep VARCHAR(8) NOT NULL,
  complemento VARCHAR(255)
);

CREATE INDEX idx_alunos_nome ON alunos(nome);
CREATE INDEX idx_alunos_escola ON alunos(escola);
CREATE INDEX idx_alunos_responsavel_id ON alunos(responsavel_id);


CREATE TABLE IF NOT EXISTS colaboradores (
  id UUID NOT NULL PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  data_nascimento DATE NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  funcao VARCHAR(100) NOT NULL CHECK (funcao in ('PROFESSOR', 'ADMINISTRATIVO', 'TERAPEUTA', 'MENTOR', 'SISTEMA')),
  pix VARCHAR(255) NOT NULL,
  ativo boolean NOT NULL DEFAULT TRUE,
  user_id UUID UNIQUE REFERENCES users(id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,

  rua VARCHAR(255) NOT NULL,
  numero VARCHAR(10) NOT NULL,
  bairro VARCHAR(255) NOT NULL,
  cidade VARCHAR(255) NOT NULL,
  estado VARCHAR(2) NOT NULL CHECK (estado in ('AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO')),
  cep VARCHAR(8) NOT NULL,
  complemento VARCHAR(255)
);

CREATE INDEX idx_colaboradores_nome ON colaboradores(nome);
CREATE INDEX idx_colaboradores_funcao ON colaboradores(funcao);

CREATE TABLE IF NOT EXISTS atendimentos (
  id UUID NOT NULL PRIMARY KEY,
  aluno_id UUID NOT NULL REFERENCES alunos(id),
  colaborador_id UUID NOT NULL REFERENCES colaboradores(id),
  titulo VARCHAR(255),
  descricao TEXT,
  inicio DATE NOT NULL,
  fim DATE NOT NULL,
  pagamento_aluno NUMERIC(10,2) NOT NULL CHECK (pagamento_aluno >= 0),
  repasse_professor NUMERIC(10,2) NOT NULL CHECK (repasse_professor >= 0),
  tipo VARCHAR(255) NOT NULL CHECK (tipo in ('AULA','MENTORIA','TERAPIA','ORIENTACAO_VOCACIONAL','ENEM','PAS','OUTRO')),
  status VARCHAR(255) NOT NULL CHECK (status in ('AGENDADO','REALIZADO','CANCELADO')),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_atendimentos_tipo ON atendimentos(tipo);
CREATE INDEX idx_atendimentos_status ON atendimentos(status);
CREATE INDEX idx_atendimentos_inicio ON atendimentos(inicio);
CREATE INDEX idx_atendimentos_fim ON atendimentos(fim);

CREATE INDEX idx_atendimentos_aluno_inicio ON atendimentos(aluno_id, inicio);
CREATE INDEX idx_atendimentos_colaborador_inicio ON atendimentos(colaborador_id, inicio);

CREATE TABLE IF NOT EXISTS transacoes (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  pagador_id UUID NOT NULL,
  recebedor_id UUID NOT NULL,
  valor NUMERIC(10,2) NOT NULL CHECK (valor >= 0),
  data_efetivada DATE NOT NULL,
  tipo VARCHAR(20) NOT NULL CHECK (tipo in ('ENTRADA','SAIDA')),
  forma_pagamento VARCHAR(20) NOT NULL CHECK (forma_pagamento in ('CREDITO','DEBITO','PIX', 'TRANSFERENCIA', 'DINHEIRO')),
  status VARCHAR(255) NOT NULL CHECK (status in ('PAGO','PENDENTE','ATRASADO','CANCELADO')),
  categoria VARCHAR(255) NOT NULL CHECK (categoria in ('PGTO_ALUNO','INVESTIMENTO','PGTO_COLABORADOR', 'CONTAS', 'MATERIAL','DESPENSA','MANUTENCAO')),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_transacoes_tipo ON transacoes(tipo);
CREATE INDEX idx_transacoes_status ON transacoes(status);
CREATE INDEX idx_transacoes_categoria ON transacoes(categoria);

INSERT INTO responsaveis (id, nome, cpf, data_nascimento, telefone, email, created_at, updated_at)
SELECT
  'ffffffff-ffff-ffff-ffff-ffffffffffff',
  'Responsavel Removido',
  '000.000.000-00',
  DATE '2000-01-01',
  '00000000000',
  'responsavel.removido@aprimorar.local',
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM responsaveis WHERE id = 'ffffffff-ffff-ffff-ffff-ffffffffffff'
);

INSERT INTO alunos (id, responsavel_id, nome, cpf, email, data_nascimento, telefone, escola, ativo, created_at, updated_at, rua, numero, bairro, cidade, estado, cep, complemento)
SELECT
  '00000000-0000-4000-8000-000000000002',
  'ffffffff-ffff-ffff-ffff-ffffffffffff',
  'Aluno Removido',
  '000.000.000-02',
  'aluno.removido@aprimorar.local',
  DATE '2000-01-01',
  '00000000002',
  'Sistema',
  false,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  'Sistema',
  '0',
  'Sistema',
  'Sistema',
  'SP',
  '00000000',
  NULL
WHERE NOT EXISTS (
  SELECT 1 FROM alunos WHERE id = '00000000-0000-4000-8000-000000000002'
);

INSERT INTO colaboradores (id, nome, cpf, data_nascimento, telefone, email, pix, ativo, created_at, updated_at, funcao, rua, numero, bairro, cidade, estado, cep, complemento)
SELECT
  '00000000-0000-4000-8000-000000000001',
  'Colaborador Removido',
  '000.000.000-01',
  DATE '2000-01-01',
  '00000000001',
  'colaborador.removido@aprimorar.local',
  '00000000001',
  false,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  'SISTEMA',
  'Sistema',
  '0',
  'Sistema',
  'Sistema',
  'SP',
  '00000000',
  NULL
WHERE NOT EXISTS (
  SELECT 1 FROM colaboradores WHERE id = '00000000-0000-4000-8000-000000000001'
);
