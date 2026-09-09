ALTER TABLE alunos
  ADD COLUMN endereco_rua VARCHAR(255),
  ADD COLUMN endereco_numero VARCHAR(10),
  ADD COLUMN endereco_bairro VARCHAR(255),
  ADD COLUMN endereco_cidade VARCHAR(255),
  ADD COLUMN endereco_estado VARCHAR(2),
  ADD COLUMN endereco_cep VARCHAR(8),
  ADD COLUMN endereco_complemento VARCHAR(255);

ALTER TABLE colaboradores
  ADD COLUMN endereco_rua VARCHAR(255),
  ADD COLUMN endereco_numero VARCHAR(10),
  ADD COLUMN endereco_bairro VARCHAR(255),
  ADD COLUMN endereco_cidade VARCHAR(255),
  ADD COLUMN endereco_estado VARCHAR(2),
  ADD COLUMN endereco_cep VARCHAR(8),
  ADD COLUMN endereco_complemento VARCHAR(255);

UPDATE alunos AS aluno
SET endereco_rua = endereco.rua,
    endereco_numero = endereco.numero,
    endereco_bairro = endereco.bairro,
    endereco_cidade = endereco.cidade,
    endereco_estado = endereco.estado,
    endereco_cep = endereco.cep,
    endereco_complemento = endereco.complemento
FROM enderecos AS endereco
WHERE endereco.id = aluno.endereco_id;

UPDATE colaboradores AS colaborador
SET endereco_rua = endereco.rua,
    endereco_numero = endereco.numero,
    endereco_bairro = endereco.bairro,
    endereco_cidade = endereco.cidade,
    endereco_estado = endereco.estado,
    endereco_cep = endereco.cep,
    endereco_complemento = endereco.complemento
FROM enderecos AS endereco
WHERE endereco.id = colaborador.endereco_id;

ALTER TABLE alunos
  ALTER COLUMN endereco_rua SET NOT NULL,
  ALTER COLUMN endereco_numero SET NOT NULL,
  ALTER COLUMN endereco_bairro SET NOT NULL,
  ALTER COLUMN endereco_cidade SET NOT NULL,
  ALTER COLUMN endereco_estado SET NOT NULL,
  ALTER COLUMN endereco_cep SET NOT NULL,
  ADD CONSTRAINT ck_alunos_endereco_estado
    CHECK (endereco_estado IN ('AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'));

ALTER TABLE colaboradores
  ALTER COLUMN endereco_rua SET NOT NULL,
  ALTER COLUMN endereco_numero SET NOT NULL,
  ALTER COLUMN endereco_bairro SET NOT NULL,
  ALTER COLUMN endereco_cidade SET NOT NULL,
  ALTER COLUMN endereco_estado SET NOT NULL,
  ALTER COLUMN endereco_cep SET NOT NULL,
  ADD CONSTRAINT ck_colaboradores_endereco_estado
    CHECK (endereco_estado IN ('AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO'));
