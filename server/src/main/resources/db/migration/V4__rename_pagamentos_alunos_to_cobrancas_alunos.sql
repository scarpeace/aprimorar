ALTER TABLE pagamentos_alunos
    RENAME TO cobrancas_alunos;

ALTER TABLE cobrancas_alunos
    RENAME COLUMN lote TO pagamento_id;

ALTER SEQUENCE pagamentos_alunos_id_seq
    RENAME TO cobrancas_alunos_id_seq;

ALTER SEQUENCE pagamentos_alunos_lote_seq
    RENAME TO cobrancas_alunos_pagamento_id_seq;

ALTER TABLE cobrancas_alunos
    RENAME CONSTRAINT pagamentos_alunos_pkey
    TO cobrancas_alunos_pkey;

ALTER TABLE cobrancas_alunos
    RENAME CONSTRAINT pagamentos_alunos_desconto_check
    TO cobrancas_alunos_desconto_check;

ALTER TABLE cobrancas_alunos
    RENAME CONSTRAINT pagamentos_alunos_forma_pagamento_check
    TO cobrancas_alunos_forma_pagamento_check;

ALTER TABLE cobrancas_alunos
    RENAME CONSTRAINT pagamentos_alunos_status_check
    TO cobrancas_alunos_status_check;

ALTER TABLE cobrancas_alunos
    RENAME CONSTRAINT pagamentos_alunos_valor_check
    TO cobrancas_alunos_valor_check;

ALTER TABLE cobrancas_alunos
    RENAME CONSTRAINT pagamentos_alunos_desconto_valor_check
    TO cobrancas_alunos_desconto_valor_check;

ALTER INDEX uk_pagamentos_alunos_atendimento_id
    RENAME TO uk_cobrancas_alunos_atendimento_id;

ALTER INDEX idx_pagamentos_alunos_lote
    RENAME TO idx_cobrancas_alunos_pagamento_id;

ALTER INDEX idx_pagamentos_alunos_aluno_status
    RENAME TO idx_cobrancas_alunos_aluno_status;
