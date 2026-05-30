ALTER TABLE tb_expenses
    ALTER COLUMN date TYPE timestamp(6) with time zone
    USING date::timestamp with time zone;
