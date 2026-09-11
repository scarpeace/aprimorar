DROP VIEW IF EXISTS vw_consultas_atendimentos;

ALTER TABLE atendimentos
    RENAME TO atendimentos_individuais;

ALTER SEQUENCE atendimentos_id_seq
    RENAME TO atendimentos_individuais_id_seq;

ALTER TABLE atendimentos_individuais
    RENAME CONSTRAINT atendimentos_pkey
    TO atendimentos_individuais_pkey;

ALTER INDEX idx_atendimentos_tipo
    RENAME TO idx_atendimentos_individuais_tipo;

ALTER TABLE cobrancas_alunos
    RENAME TO cobrancas_individuais;

ALTER SEQUENCE cobrancas_alunos_id_seq
    RENAME TO cobrancas_individuais_id_seq;

ALTER TABLE cobrancas_individuais
    RENAME CONSTRAINT cobrancas_alunos_pkey
    TO cobrancas_individuais_pkey;

ALTER INDEX uk_cobrancas_alunos_atendimento_id
    RENAME TO uk_cobrancas_individuais_atendimento_id;

ALTER INDEX idx_cobrancas_alunos_aluno_status
    RENAME TO idx_cobrancas_individuais_aluno_status;

DROP INDEX uk_cobrancas_individuais_atendimento_id;

ALTER TABLE cobrancas_individuais
    ALTER COLUMN atendimento_id SET NOT NULL,
    ALTER COLUMN aluno_id SET NOT NULL,
    ALTER COLUMN valor SET NOT NULL;

ALTER TABLE cobrancas_individuais
    ADD CONSTRAINT cobrancas_individuais_atendimento_fk
        FOREIGN KEY (atendimento_id) REFERENCES atendimentos_individuais(id),
    ADD CONSTRAINT cobrancas_individuais_aluno_fk
        FOREIGN KEY (aluno_id) REFERENCES alunos(id);

CREATE UNIQUE INDEX uk_cobrancas_individuais_atendimento_id
    ON cobrancas_individuais (atendimento_id);

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
    ON repasses_individuais (colaborador_id, status);

INSERT INTO repasses_individuais (atendimento_id, colaborador_id, valor)
SELECT id, colaborador_id, repasse_colaborador
FROM atendimentos_individuais;

ALTER TABLE atendimentos_individuais
    DROP COLUMN repasse_colaborador;

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
