DROP INDEX idx_cobrancas_alunos_pagamento_id;

ALTER TABLE cobrancas_alunos
    DROP COLUMN pagamento_id;

DROP SEQUENCE cobrancas_alunos_pagamento_id_seq;
