DROP INDEX IF EXISTS idx_alunos_responsavel_id;

ALTER TABLE alunos
  DROP CONSTRAINT IF EXISTS alunos_responsavel_id_fkey,
  DROP COLUMN IF EXISTS responsavel_id,
  DROP COLUMN IF EXISTS responsavel_data_nascimento;

DROP TABLE IF EXISTS responsaveis;
