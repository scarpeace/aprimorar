DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uk_employee_name') THEN
        ALTER TABLE tb_employee ADD CONSTRAINT uk_employee_name UNIQUE (name);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uk_employee_contact') THEN
        ALTER TABLE tb_employee ADD CONSTRAINT uk_employee_contact UNIQUE (contact);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uk_employee_email') THEN
        ALTER TABLE tb_employee ADD CONSTRAINT uk_employee_email UNIQUE (email);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uk_employee_cpf') THEN
        ALTER TABLE tb_employee ADD CONSTRAINT uk_employee_cpf UNIQUE (cpf);
    END IF;
END $$;
