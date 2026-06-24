ALTER TABLE transacoes
  ADD COLUMN nome_pagador VARCHAR(255),
  ADD COLUMN nome_recebedor VARCHAR(255);

UPDATE transacoes t
SET
  nome_pagador = CASE
    WHEN t.tipo = 'ENTRADA' THEN a.nome_aluno
    ELSE 'Aprimorar'
  END,
  nome_recebedor = CASE
    WHEN t.tipo = 'SAIDA' THEN a.nome_colaborador
    ELSE 'Aprimorar'
  END
FROM atendimentos a
WHERE t.atendimento_id = a.id;

UPDATE transacoes
SET
  nome_pagador = COALESCE(nome_pagador, 'Aprimorar'),
  nome_recebedor = COALESCE(nome_recebedor, categoria);

CREATE INDEX idx_transacoes_nome_pagador ON transacoes(nome_pagador);
CREATE INDEX idx_transacoes_nome_recebedor ON transacoes(nome_recebedor);
