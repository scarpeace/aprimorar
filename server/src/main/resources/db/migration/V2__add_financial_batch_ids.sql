ALTER TABLE cobrancas_individuais
  ADD COLUMN lote_id UUID;

ALTER TABLE repasses_individuais
  ADD COLUMN lote_id UUID;

CREATE INDEX idx_cobrancas_individuais_lote_id
  ON cobrancas_individuais(lote_id);

CREATE INDEX idx_repasses_individuais_lote_id
  ON repasses_individuais(lote_id);

CREATE OR REPLACE VIEW vw_atendimentos_individuais AS
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
  atendimento.updated_at,
  cobranca.lote_id AS cobranca_lote_id,
  repasse.lote_id AS repasse_lote_id
FROM atendimentos_individuais atendimento
JOIN alunos aluno ON aluno.id = atendimento.aluno_id
JOIN colaboradores colaborador ON colaborador.id = atendimento.colaborador_id
JOIN cobrancas_individuais cobranca ON cobranca.atendimento_id = atendimento.id
JOIN repasses_individuais repasse ON repasse.atendimento_id = atendimento.id;
