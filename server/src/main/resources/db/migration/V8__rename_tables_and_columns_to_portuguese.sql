DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_appointments') THEN
        ALTER TABLE tb_appointments RENAME TO tb_atendimentos;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_employees') THEN
        ALTER TABLE tb_employees RENAME TO tb_colaboradores;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_parent') THEN
        ALTER TABLE tb_parent RENAME TO tb_responsaveis;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_students') THEN
        ALTER TABLE tb_students RENAME TO tb_alunos;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_expenses') THEN
        ALTER TABLE tb_expenses RENAME TO tb_despesas;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'employee_id') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN employee_id TO colaborador_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'employee_name') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN employee_name TO colaborador_nome;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'employee_payment_date') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN employee_payment_date TO data_pagamento_colaborador;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'student_id') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN student_id TO aluno_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'student_name') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN student_name TO aluno_nome;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'student_charge_date') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN student_charge_date TO data_cobranca_aluno;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'start_date') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN start_date TO inicio_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'end_date') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN end_date TO fim_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'created_at') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN created_at TO criado_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'updated_at') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN updated_at TO atualizado_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'description') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN description TO descricao;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'title') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN title TO titulo;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'content') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN content TO tipo_atendimento;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'price') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN price TO cobranca_aluno;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_atendimentos' AND column_name = 'payment') THEN
        ALTER TABLE tb_atendimentos RENAME COLUMN payment TO pagamento_professor;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'name') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN name TO nome;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'birthdate') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN birthdate TO data_nascimento;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'contact') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN contact TO contato;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'duty') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN duty TO funcao;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'active') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN active TO ativo;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'created_at') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN created_at TO criado_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'updated_at') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN updated_at TO atualizado_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'street') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN street TO rua;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'district') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN district TO bairro;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'city') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN city TO cidade;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'state') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN state TO estado;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'zip') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN zip TO cep;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'complement') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN complement TO complemento;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'name') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN name TO nome;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'birthdate') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN birthdate TO data_nascimento;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'contact') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN contact TO contato;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'active') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN active TO ativo;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'created_at') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN created_at TO criado_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'updated_at') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN updated_at TO atualizado_em;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'name') THEN
        ALTER TABLE tb_alunos RENAME COLUMN name TO nome;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'birthdate') THEN
        ALTER TABLE tb_alunos RENAME COLUMN birthdate TO data_nascimento;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'contact') THEN
        ALTER TABLE tb_alunos RENAME COLUMN contact TO contato;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'school') THEN
        ALTER TABLE tb_alunos RENAME COLUMN school TO escola;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'parent_id') THEN
        ALTER TABLE tb_alunos RENAME COLUMN parent_id TO responsavel_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'active') THEN
        ALTER TABLE tb_alunos RENAME COLUMN active TO ativo;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'created_at') THEN
        ALTER TABLE tb_alunos RENAME COLUMN created_at TO criado_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'updated_at') THEN
        ALTER TABLE tb_alunos RENAME COLUMN updated_at TO atualizado_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'street') THEN
        ALTER TABLE tb_alunos RENAME COLUMN street TO rua;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'district') THEN
        ALTER TABLE tb_alunos RENAME COLUMN district TO bairro;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'city') THEN
        ALTER TABLE tb_alunos RENAME COLUMN city TO cidade;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'state') THEN
        ALTER TABLE tb_alunos RENAME COLUMN state TO estado;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'zip') THEN
        ALTER TABLE tb_alunos RENAME COLUMN zip TO cep;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'complement') THEN
        ALTER TABLE tb_alunos RENAME COLUMN complement TO complemento;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_despesas' AND column_name = 'amount') THEN
        ALTER TABLE tb_despesas RENAME COLUMN amount TO valor;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_despesas' AND column_name = 'description') THEN
        ALTER TABLE tb_despesas RENAME COLUMN description TO descricao;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_despesas' AND column_name = 'category') THEN
        ALTER TABLE tb_despesas RENAME COLUMN category TO categoria;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_despesas' AND column_name = 'payment_date') THEN
        ALTER TABLE tb_despesas RENAME COLUMN payment_date TO data_pagamento;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_despesas' AND column_name = 'created_at') THEN
        ALTER TABLE tb_despesas RENAME COLUMN created_at TO criado_em;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_despesas' AND column_name = 'updated_at') THEN
        ALTER TABLE tb_despesas RENAME COLUMN updated_at TO atualizado_em;
    END IF;
END $$;
