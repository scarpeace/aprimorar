DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'tb_despesas')
       AND EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_despesas' AND column_name = 'date')
       AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'tb_despesas' AND column_name = 'data') THEN
        ALTER TABLE tb_despesas RENAME COLUMN date TO data;
    END IF;
END $$;
