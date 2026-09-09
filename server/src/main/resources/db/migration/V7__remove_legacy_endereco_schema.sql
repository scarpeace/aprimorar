ALTER TABLE alunos
  DROP COLUMN IF EXISTS endereco_id;

ALTER TABLE colaboradores
  DROP COLUMN IF EXISTS endereco_id;

DROP TABLE IF EXISTS enderecos;
