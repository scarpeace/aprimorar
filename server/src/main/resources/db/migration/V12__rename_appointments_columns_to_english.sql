DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'criado_em') THEN
        ALTER TABLE tb_appointments RENAME COLUMN criado_em TO created_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'atualizado_em') THEN
        ALTER TABLE tb_appointments RENAME COLUMN atualizado_em TO updated_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'titulo') THEN
        ALTER TABLE tb_appointments RENAME COLUMN titulo TO title;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'descricao') THEN
        ALTER TABLE tb_appointments RENAME COLUMN descricao TO description;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'inicio_em') THEN
        ALTER TABLE tb_appointments RENAME COLUMN inicio_em TO start_date;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'fim_em') THEN
        ALTER TABLE tb_appointments RENAME COLUMN fim_em TO end_date;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'pagamento_professor') THEN
        ALTER TABLE tb_appointments RENAME COLUMN pagamento_professor TO payment;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'cobranca_aluno') THEN
        ALTER TABLE tb_appointments RENAME COLUMN cobranca_aluno TO price;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'tipo_atendimento') THEN
        ALTER TABLE tb_appointments RENAME COLUMN tipo_atendimento TO content;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'data_pagamento_colaborador') THEN
        ALTER TABLE tb_appointments RENAME COLUMN data_pagamento_colaborador TO employee_payment_date;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'data_cobranca_aluno') THEN
        ALTER TABLE tb_appointments RENAME COLUMN data_cobranca_aluno TO student_charge_date;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'aluno_id') THEN
        ALTER TABLE tb_appointments RENAME COLUMN aluno_id TO student_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'aluno_nome') THEN
        ALTER TABLE tb_appointments RENAME COLUMN aluno_nome TO student_name;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'colaborador_id') THEN
        ALTER TABLE tb_appointments RENAME COLUMN colaborador_id TO employee_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_appointments' AND column_name = 'colaborador_nome') THEN
        ALTER TABLE tb_appointments RENAME COLUMN colaborador_nome TO employee_name;
    END IF;
END $$;
