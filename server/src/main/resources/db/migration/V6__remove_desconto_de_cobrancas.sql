ALTER TABLE cobrancas_alunos
    DROP CONSTRAINT cobrancas_alunos_desconto_valor_check,
    DROP CONSTRAINT cobrancas_alunos_desconto_check,
    DROP COLUMN desconto;
