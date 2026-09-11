CREATE VIEW vw_consultas_atendimentos AS
SELECT
    a.id,
    a.tipo,
    a.data_hora_inicio,
    a.data_hora_fim,
    a.repasse_colaborador,
    a.aluno_id,
    aluno.nome AS aluno_nome,
    a.colaborador_id,
    colaborador.nome AS colaborador_nome,
    cobranca.id AS cobranca_id,
    cobranca.valor AS cobranca_valor,
    cobranca.status AS cobranca_status,
    cobranca.data_pagamento AS cobranca_data_pagamento,
    cobranca.forma_pagamento AS cobranca_forma_pagamento,
    cobranca.comprovante_url AS cobranca_comprovante_url,
    a.created_at,
    a.updated_at
FROM atendimentos a
JOIN alunos aluno ON aluno.id = a.aluno_id
JOIN colaboradores colaborador ON colaborador.id = a.colaborador_id
LEFT JOIN cobrancas_alunos cobranca ON cobranca.atendimento_id = a.id;
