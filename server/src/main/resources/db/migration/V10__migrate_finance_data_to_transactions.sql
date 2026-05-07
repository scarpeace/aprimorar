INSERT INTO tb_transactions (
    id,
    type,
    status,
    amount,
    origin,
    origin_id,
    settled_at,
    category
)
SELECT
    gen_random_uuid(),
    'IN',
    CASE WHEN e.student_charge_date IS NULL THEN 'PENDING' ELSE 'SETTLED' END,
    e.price,
    'EVENT_STUDENT_CHARGE',
    e.id,
    e.student_charge_date,
    'COBRANCA_ALUNO'
FROM tb_events e;

INSERT INTO tb_transactions (
    id,
    type,
    status,
    amount,
    origin,
    origin_id,
    settled_at,
    category
)
SELECT
    gen_random_uuid(),
    'OUT',
    CASE WHEN e.employee_payment_date IS NULL THEN 'PENDING' ELSE 'SETTLED' END,
    e.payment,
    'EVENT_EMPLOYEE_PAYMENT',
    e.id,
    e.employee_payment_date,
    'PAGAMENTO_COLABORADOR'
FROM tb_events e;

INSERT INTO tb_transactions (
    id,
    type,
    status,
    amount,
    origin,
    origin_id,
    settled_at,
    category
)
SELECT
    g.id,
    'OUT',
    'SETTLED',
    g.amount,
    'GENERAL_EXPENSE',
    g.id,
    g.date::timestamp AT TIME ZONE 'UTC',
    CASE g.category
        WHEN 'CONTAS' THEN 'CONTAS'
        WHEN 'ADMINISTRATIVO' THEN 'ADMINISTRATIVO'
        WHEN 'DESPENSA' THEN 'DESPENSA'
        WHEN 'MANUTENCAO' THEN 'MANUTENCAO'
        WHEN 'SERVICOS' THEN 'SERVICOS'
        WHEN 'MATERIAIS' THEN 'MATERIAIS'
        ELSE 'CONTAS'
    END
FROM tb_general_expenses g;

DROP TABLE tb_general_expenses;
