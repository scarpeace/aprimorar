CREATE SEQUENCE pagamentos_alunos_lote_seq;

ALTER TABLE pagamentos_alunos
    ADD COLUMN atendimento_id BIGINT,
    ADD COLUMN aluno_id UUID,
    ADD COLUMN valor NUMERIC(10, 2),
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    ADD COLUMN lote BIGINT,
    ADD COLUMN comprovante_url VARCHAR(500);

ALTER TABLE pagamentos_alunos
    ALTER COLUMN data_pagamento DROP NOT NULL,
    ALTER COLUMN forma_pagamento DROP NOT NULL;

UPDATE pagamentos_alunos
SET valor = total,
    status = CASE WHEN data_pagamento IS NULL THEN 'PENDENTE' ELSE 'PAGO' END
WHERE valor IS NULL;

ALTER TABLE pagamentos_alunos
    ADD CONSTRAINT pagamentos_alunos_status_check
        CHECK (status IN ('PENDENTE', 'PAGO')),
    ADD CONSTRAINT pagamentos_alunos_valor_check
        CHECK (valor IS NULL OR valor > 0),
    ADD CONSTRAINT pagamentos_alunos_desconto_valor_check
        CHECK (valor IS NULL OR desconto IS NULL OR desconto <= valor);

CREATE UNIQUE INDEX uk_pagamentos_alunos_atendimento_id
    ON pagamentos_alunos (atendimento_id)
    WHERE atendimento_id IS NOT NULL;

CREATE INDEX idx_pagamentos_alunos_lote
    ON pagamentos_alunos (lote);

CREATE INDEX idx_pagamentos_alunos_aluno_status
    ON pagamentos_alunos (aluno_id, status);
