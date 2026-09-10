ALTER TABLE despesas
  ADD COLUMN tipo VARCHAR(20),
  ADD COLUMN data_vencimento DATE,
  ADD COLUMN status VARCHAR(20);

UPDATE despesas
SET tipo = 'SAIDA',
    data_vencimento = COALESCE(data_pagamento, CURRENT_DATE),
    status = CASE
      WHEN data_pagamento IS NOT NULL THEN 'PAGA'
      WHEN COALESCE(data_pagamento, CURRENT_DATE) < CURRENT_DATE THEN 'ATRASADA'
      ELSE 'PENDENTE'
    END;

ALTER TABLE despesas
  ALTER COLUMN tipo SET NOT NULL,
  ALTER COLUMN data_vencimento SET NOT NULL,
  ALTER COLUMN status SET NOT NULL,
  ADD CONSTRAINT despesas_tipo_check CHECK (tipo IN ('ENTRADA', 'SAIDA')),
  ADD CONSTRAINT despesas_status_check CHECK (status IN ('PENDENTE', 'PAGA', 'ATRASADA'));
