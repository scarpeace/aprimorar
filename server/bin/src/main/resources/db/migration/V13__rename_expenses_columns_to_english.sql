DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_expenses' AND column_name = 'valor') THEN
        ALTER TABLE tb_expenses RENAME COLUMN valor TO amount;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_expenses' AND column_name = 'data') THEN
        ALTER TABLE tb_expenses RENAME COLUMN data TO date;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_expenses' AND column_name = 'categoria') THEN
        ALTER TABLE tb_expenses RENAME COLUMN categoria TO category;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_expenses' AND column_name = 'descricao') THEN
        ALTER TABLE tb_expenses RENAME COLUMN descricao TO description;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_expenses' AND column_name = 'data_pagamento') THEN
        ALTER TABLE tb_expenses RENAME COLUMN data_pagamento TO payment_date;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_expenses' AND column_name = 'criado_em') THEN
        ALTER TABLE tb_expenses RENAME COLUMN criado_em TO created_at;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_expenses' AND column_name = 'atualizado_em') THEN
        ALTER TABLE tb_expenses RENAME COLUMN atualizado_em TO updated_at;
    END IF;
END $$;
