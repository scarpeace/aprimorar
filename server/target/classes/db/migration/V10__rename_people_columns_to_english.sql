DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'nome') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN nome TO name;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'data_nascimento') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN data_nascimento TO birthdate;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'contato') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN contato TO contact;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'ativo') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN ativo TO active;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'criado_em') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN criado_em TO created_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'atualizado_em') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN atualizado_em TO updated_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'funcao') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN funcao TO duty;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'rua') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN rua TO street;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'bairro') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN bairro TO district;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'cidade') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN cidade TO city;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'estado') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN estado TO state;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'cep') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN cep TO zip;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_colaboradores' AND column_name = 'complemento') THEN
        ALTER TABLE tb_colaboradores RENAME COLUMN complemento TO complement;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'nome') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN nome TO name;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'data_nascimento') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN data_nascimento TO birthdate;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'contato') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN contato TO contact;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'ativo') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN ativo TO active;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'criado_em') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN criado_em TO created_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_responsaveis' AND column_name = 'atualizado_em') THEN
        ALTER TABLE tb_responsaveis RENAME COLUMN atualizado_em TO updated_at;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'nome') THEN
        ALTER TABLE tb_alunos RENAME COLUMN nome TO name;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'data_nascimento') THEN
        ALTER TABLE tb_alunos RENAME COLUMN data_nascimento TO birthdate;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'contato') THEN
        ALTER TABLE tb_alunos RENAME COLUMN contato TO contact;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'ativo') THEN
        ALTER TABLE tb_alunos RENAME COLUMN ativo TO active;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'criado_em') THEN
        ALTER TABLE tb_alunos RENAME COLUMN criado_em TO created_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'atualizado_em') THEN
        ALTER TABLE tb_alunos RENAME COLUMN atualizado_em TO updated_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'escola') THEN
        ALTER TABLE tb_alunos RENAME COLUMN escola TO school;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'responsavel_id') THEN
        ALTER TABLE tb_alunos RENAME COLUMN responsavel_id TO parent_id;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'rua') THEN
        ALTER TABLE tb_alunos RENAME COLUMN rua TO street;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'bairro') THEN
        ALTER TABLE tb_alunos RENAME COLUMN bairro TO district;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'cidade') THEN
        ALTER TABLE tb_alunos RENAME COLUMN cidade TO city;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'estado') THEN
        ALTER TABLE tb_alunos RENAME COLUMN estado TO state;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'cep') THEN
        ALTER TABLE tb_alunos RENAME COLUMN cep TO zip;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_alunos' AND column_name = 'complemento') THEN
        ALTER TABLE tb_alunos RENAME COLUMN complemento TO complement;
    END IF;
END $$;
