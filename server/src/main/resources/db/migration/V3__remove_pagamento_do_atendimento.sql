ALTER TABLE atendimentos
    DROP CONSTRAINT IF EXISTS atendimentos_pagamento_aluno_id_fkey,
    DROP COLUMN pagamento_aluno_id,
    DROP COLUMN pagamento_aluno;

ALTER TABLE pagamentos_alunos
    DROP COLUMN total;
