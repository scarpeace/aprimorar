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
  cpf VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
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
  updated_at TIMESTAMP,
  CONSTRAINT uk_alunos_cpf UNIQUE (cpf),
  CONSTRAINT uk_alunos_email UNIQUE (email)
);

CREATE INDEX idx_alunos_nome ON alunos(nome);
CREATE INDEX idx_alunos_escola ON alunos(escola);

CREATE TABLE colaboradores (
  id UUID NOT NULL PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  cpf VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
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
  updated_at TIMESTAMP,
  CONSTRAINT uk_colaboradores_cpf UNIQUE (cpf),
  CONSTRAINT uk_colaboradores_email UNIQUE (email)
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
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT ck_atendimentos_individuais_periodo
    CHECK (data_hora_fim >= data_hora_inicio),
  CONSTRAINT ck_atendimentos_individuais_status
    CHECK (status IN ('AGENDADO', 'REALIZADO', 'CANCELADO'))
);

CREATE INDEX idx_atendimentos_individuais_tipo ON atendimentos_individuais(tipo);

CREATE TABLE pagamentos_alunos (
  id UUID NOT NULL PRIMARY KEY,
  aluno_id UUID NOT NULL REFERENCES alunos(id),
  data_pagamento DATE NOT NULL,
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
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pagamentos_alunos_aluno_data
  ON pagamentos_alunos(aluno_id, data_pagamento);

CREATE TABLE cobrancas_alunos (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  aluno_id UUID NOT NULL REFERENCES alunos(id),
  origem_id BIGINT NOT NULL,
  origem_tipo VARCHAR(40) NOT NULL CHECK (
    origem_tipo IN ('ATENDIMENTO_INDIVIDUAL', 'ATENDIMENTO_TURMA')
  ),
  valor_total NUMERIC(10, 2) NOT NULL CHECK (valor_total >= 50),
  status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT ck_cobrancas_alunos_status
    CHECK (status IN ('PENDENTE', 'PARCIALMENTE_PAGA', 'PAGA', 'CANCELADA'))
);

CREATE UNIQUE INDEX uk_cobrancas_alunos_origem
  ON cobrancas_alunos(aluno_id, origem_tipo, origem_id);
CREATE INDEX idx_cobrancas_alunos_aluno_status
  ON cobrancas_alunos(aluno_id, status);
CREATE INDEX idx_cobrancas_alunos_origem
  ON cobrancas_alunos(origem_tipo, origem_id);

CREATE TABLE parcelas_alunos (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  cobranca_id BIGINT NOT NULL REFERENCES cobrancas_alunos(id) ON DELETE CASCADE,
  pagamento_id UUID REFERENCES pagamentos_alunos(id),
  numero_parcela INTEGER NOT NULL CHECK (numero_parcela > 0),
  valor NUMERIC(10, 2) NOT NULL CHECK (valor > 0),
  data_vencimento DATE,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT ck_parcelas_alunos_status
    CHECK (status IN ('PENDENTE', 'PAGA', 'CANCELADA')),
  CONSTRAINT ck_parcelas_alunos_pagamento
    CHECK (
      (status = 'PAGA' AND pagamento_id IS NOT NULL)
      OR
      (status IN ('PENDENTE', 'CANCELADA') AND pagamento_id IS NULL)
    ),
  CONSTRAINT uk_parcelas_alunos_cobranca_numero
    UNIQUE (cobranca_id, numero_parcela)
);

CREATE INDEX idx_parcelas_alunos_pagamento_id
  ON parcelas_alunos(pagamento_id);
CREATE INDEX idx_parcelas_alunos_cobranca_status
  ON parcelas_alunos(cobranca_id, status);

CREATE TABLE pagamentos_particular (
  id UUID NOT NULL PRIMARY KEY,
  data_pagamento DATE NOT NULL,
  valor_total NUMERIC(10, 2) NOT NULL,
  forma_pagamento VARCHAR(40) NOT NULL,
  comprovante_url VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT ck_pagamentos_particular_valor_total
    CHECK (valor_total >= 0),
  CONSTRAINT ck_pagamentos_particular_forma_pagamento
    CHECK (
      forma_pagamento IN (
        'PIX',
        'DINHEIRO',
        'CARTAO_CREDITO',
        'CARTAO_DEBITO',
        'BOLETO',
        'TRANSFERENCIA'
      )
    )
);

CREATE TABLE repasses_particular (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  atendimento_id BIGINT NOT NULL REFERENCES atendimentos_individuais(id),
  colaborador_id UUID NOT NULL REFERENCES colaboradores(id),
  valor NUMERIC(10, 2) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
  pagamento_id UUID REFERENCES pagamentos_particular(id) ON DELETE SET NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  CONSTRAINT uk_repasses_particular_atendimento UNIQUE (atendimento_id),
  CONSTRAINT ck_repasses_particular_valor CHECK (valor >= 0),
  CONSTRAINT ck_repasses_particular_status
    CHECK (status IN ('PENDENTE', 'PAGO', 'CANCELADO')),
  CONSTRAINT ck_repasses_particular_pagamento
    CHECK (
      (status = 'PAGO' AND pagamento_id IS NOT NULL)
      OR
      (status IN ('PENDENTE', 'CANCELADO') AND pagamento_id IS NULL)
    )
);

CREATE INDEX idx_repasses_particular_colaborador_status
  ON repasses_particular(colaborador_id, status);
CREATE INDEX idx_repasses_particular_pagamento_id
  ON repasses_particular(pagamento_id);

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
