ALTER TABLE cobrancas_individuais
  DROP CONSTRAINT IF EXISTS cobrancas_individuais_status_check;

ALTER TABLE cobrancas_individuais
  ADD CONSTRAINT ck_cobrancas_individuais_status
  CHECK (status IN ('PENDENTE', 'PAGO', 'CANCELADO'));

ALTER TABLE repasses_individuais
  DROP CONSTRAINT IF EXISTS repasses_individuais_status_check;

ALTER TABLE repasses_individuais
  ADD CONSTRAINT ck_repasses_individuais_status
  CHECK (status IN ('PENDENTE', 'PAGO', 'CANCELADO'));
