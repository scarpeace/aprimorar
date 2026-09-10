CREATE TABLE users (
  id UUID NOT NULL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(32) NOT NULL CHECK (role IN ('ALUNO', 'COLABORADOR', 'SECRETARIA', 'ADMIN')),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE alunos (
  id UUID NOT NULL PRIMARY KEY,
  user_id UUID UNIQUE REFERENCES users(id),
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
  user_id UUID UNIQUE REFERENCES users(id),
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

CREATE TABLE pagamentos_alunos (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  data_pagamento TIMESTAMP NOT NULL,
  total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
  desconto NUMERIC(10, 2) CHECK (desconto >= 0),
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
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE atendimentos (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  aluno_id UUID NOT NULL REFERENCES alunos(id),
  colaborador_id UUID NOT NULL REFERENCES colaboradores(id),
  data_hora_inicio TIMESTAMP NOT NULL,
  data_hora_fim TIMESTAMP NOT NULL,
  tipo VARCHAR(255) NOT NULL CHECK (
    tipo IN ('AULA','MENTORIA','TERAPIA','ORIENTACAO_VOCACIONAL','ENEM','PAS','OUTRO')
  ),
  pagamento_aluno NUMERIC(10, 2) NOT NULL CHECK (pagamento_aluno >= 0),
  repasse_colaborador NUMERIC(10, 2) NOT NULL CHECK (repasse_colaborador >= 0),
  pagamento_aluno_id BIGINT REFERENCES pagamentos_alunos(id),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_atendimentos_tipo ON atendimentos(tipo);
CREATE INDEX idx_atendimentos_pagamento_aluno_id ON atendimentos(pagamento_aluno_id);

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
