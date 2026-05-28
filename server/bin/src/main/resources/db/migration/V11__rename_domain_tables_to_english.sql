DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_responsaveis')
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_parents') THEN
        ALTER TABLE tb_responsaveis RENAME TO tb_parents;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_parent')
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_parents') THEN
        ALTER TABLE tb_parent RENAME TO tb_parents;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_alunos')
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_students') THEN
        ALTER TABLE tb_alunos RENAME TO tb_students;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_colaboradores')
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_employees') THEN
        ALTER TABLE tb_colaboradores RENAME TO tb_employees;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_atendimentos')
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_appointments') THEN
        ALTER TABLE tb_atendimentos RENAME TO tb_appointments;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_despesas')
       AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_expenses') THEN
        ALTER TABLE tb_despesas RENAME TO tb_expenses;
    END IF;
END $$;
