CREATE TABLE users (
  id UUID NOT NULL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(32) NOT NULL CHECK (role IN ('ADMIN', 'SECRETARIA')),
  enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE refresh_tokens (
  id UUID NOT NULL PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id),
  token_hash VARCHAR(64) NOT NULL UNIQUE,
  expires_at TIMESTAMPTZ NOT NULL,
  revoked BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);

CREATE TABLE alunos (
  id UUID NOT NULL PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  data_nascimento DATE NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  escola VARCHAR(255),
  ativo BOOLEAN NOT NULL DEFAULT TRUE,
  responsavel_nome VARCHAR(50) NOT NULL,
  responsavel_cpf VARCHAR(255) NOT NULL,
  responsavel_telefone VARCHAR(20) NOT NULL,
  responsavel_email VARCHAR(255) NOT NULL,
  endereco_rua VARCHAR(255) NOT NULL,
  endereco_numero VARCHAR(10) NOT NULL,
  endereco_bairro VARCHAR(255) NOT NULL,
  endereco_cidade VARCHAR(255) NOT NULL,
  endereco_estado VARCHAR(2) NOT NULL CHECK (
    endereco_estado IN (
      'AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS',
      'MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'
    )
  ),
  endereco_cep VARCHAR(8) NOT NULL,
  endereco_complemento VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_alunos_nome ON alunos(nome);
CREATE INDEX idx_alunos_escola ON alunos(escola);

CREATE TABLE colaboradores (
  id UUID NOT NULL PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  data_nascimento DATE NOT NULL,
  telefone VARCHAR(20) NOT NULL,
  funcao VARCHAR(100) NOT NULL CHECK (
    funcao IN ('PROFESSOR', 'ADMINISTRATIVO', 'TERAPEUTA', 'MENTOR')
  ),
  pix VARCHAR(255) NOT NULL,
  ativo BOOLEAN NOT NULL DEFAULT TRUE,
  endereco_rua VARCHAR(255) NOT NULL,
  endereco_numero VARCHAR(10) NOT NULL,
  endereco_bairro VARCHAR(255) NOT NULL,
  endereco_cidade VARCHAR(255) NOT NULL,
  endereco_estado VARCHAR(2) NOT NULL CHECK (
    endereco_estado IN (
      'AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS',
      'MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'
    )
  ),
  endereco_cep VARCHAR(8) NOT NULL,
  endereco_complemento VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_colaboradores_nome ON colaboradores(nome);
CREATE INDEX idx_colaboradores_funcao ON colaboradores(funcao);

CREATE TABLE atendimentos_individuais (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  aluno_id UUID NOT NULL REFERENCES alunos(id),
  colaborador_id UUID NOT NULL REFERENCES colaboradores(id),
  data_hora_inicio TIMESTAMP NOT NULL,
  data_hora_fim TIMESTAMP NOT NULL,
  tipo VARCHAR(255) NOT NULL CHECK (
    tipo IN ('AULA','MENTORIA','TERAPIA','ORIENTACAO_VOCACIONAL','ENEM','PAS','OUTRO')
  ),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_atendimentos_individuais_tipo ON atendimentos_individuais(tipo);

CREATE TABLE cobrancas_individuais (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  atendimento_id BIGINT NOT NULL REFERENCES atendimentos_individuais(id),
  aluno_id UUID NOT NULL REFERENCES alunos(id),
  valor NUMERIC(10, 2) NOT NULL CHECK (valor > 0),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'PAGO')),
  data_pagamento TIMESTAMP,
  forma_pagamento VARCHAR(40) CHECK (
    forma_pagamento IS NULL OR forma_pagamento IN (
      'PIX',
      'DINHEIRO',
      'CARTAO_CREDITO',
      'CARTAO_DEBITO',
      'BOLETO',
      'TRANSFERENCIA'
    )
  ),
  comprovante_url VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE UNIQUE INDEX uk_cobrancas_individuais_atendimento_id
  ON cobrancas_individuais(atendimento_id);
CREATE INDEX idx_cobrancas_individuais_aluno_status
  ON cobrancas_individuais(aluno_id, status);

CREATE TABLE repasses_individuais (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  atendimento_id BIGINT NOT NULL UNIQUE REFERENCES atendimentos_individuais(id),
  colaborador_id UUID NOT NULL REFERENCES colaboradores(id),
  valor NUMERIC(10, 2) NOT NULL CHECK (valor >= 0),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'PAGO')),
  data_repasse TIMESTAMP,
  forma_pagamento VARCHAR(40) CHECK (
    forma_pagamento IS NULL OR forma_pagamento IN (
      'PIX',
      'DINHEIRO',
      'CARTAO_CREDITO',
      'CARTAO_DEBITO',
      'BOLETO',
      'TRANSFERENCIA'
    )
  ),
  comprovante_url VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_repasses_individuais_colaborador_status
  ON repasses_individuais(colaborador_id, status);

CREATE TABLE despesas (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  titulo VARCHAR(120) NOT NULL,
  tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
  categoria VARCHAR(40) NOT NULL CHECK (
    categoria IN (
      'CONTAS',
      'PROFESSORES',
      'FUNCIONARIOS',
      'DESPENSA',
      'MANUTENCAO',
      'SERVICOS',
      'ASSINATURAS'
    )
  ),
  valor NUMERIC(10, 2) NOT NULL CHECK (valor > 0),
  data_vencimento DATE NOT NULL,
  data_pagamento DATE,
  status VARCHAR(20) NOT NULL CHECK (status IN ('PENDENTE', 'PAGA', 'ATRASADA')),
  forma_pagamento VARCHAR(40) NOT NULL CHECK (
    forma_pagamento IN (
      'PIX',
      'DINHEIRO',
      'CARTAO_CREDITO',
      'CARTAO_DEBITO',
      'BOLETO',
      'TRANSFERENCIA'
    )
  ),
  descricao VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_despesas_categoria ON despesas(categoria);
CREATE INDEX idx_despesas_data_pagamento ON despesas(data_pagamento);

CREATE VIEW vw_atendimentos_individuais AS
SELECT
  atendimento.id,
  atendimento.tipo,
  atendimento.data_hora_inicio,
  atendimento.data_hora_fim,
  atendimento.aluno_id,
  aluno.nome AS aluno_nome,
  atendimento.colaborador_id,
  colaborador.nome AS colaborador_nome,
  cobranca.id AS cobranca_id,
  cobranca.valor AS cobranca_valor,
  cobranca.status AS cobranca_status,
  cobranca.data_pagamento AS cobranca_data_pagamento,
  cobranca.forma_pagamento AS cobranca_forma_pagamento,
  cobranca.comprovante_url AS cobranca_comprovante_url,
  repasse.id AS repasse_id,
  repasse.valor AS repasse_valor,
  repasse.status AS repasse_status,
  repasse.data_repasse AS repasse_data_repasse,
  repasse.forma_pagamento AS repasse_forma_pagamento,
  repasse.comprovante_url AS repasse_comprovante_url,
  atendimento.created_at,
  atendimento.updated_at
FROM atendimentos_individuais atendimento
JOIN alunos aluno ON aluno.id = atendimento.aluno_id
JOIN colaboradores colaborador ON colaborador.id = atendimento.colaborador_id
JOIN cobrancas_individuais cobranca ON cobranca.atendimento_id = atendimento.id
JOIN repasses_individuais repasse ON repasse.atendimento_id = atendimento.id;
