DROP INDEX IF EXISTS idx_atendimentos_status;

ALTER TABLE atendimentos
  DROP COLUMN IF EXISTS status,
  DROP COLUMN IF EXISTS data_pagamento_aluno,
  DROP COLUMN IF EXISTS data_repasse_colaborador;
