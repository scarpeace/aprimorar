ALTER TABLE atendimentos_individuais
  ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'AGENDADO';

ALTER TABLE atendimentos_individuais
  ADD CONSTRAINT ck_atendimentos_individuais_status
  CHECK (status IN ('AGENDADO', 'REALIZADO', 'CANCELADO'));

ALTER TABLE atendimentos_individuais
  ALTER COLUMN status DROP DEFAULT;
