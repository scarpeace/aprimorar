ALTER TABLE tb_employees
    ADD COLUMN street varchar(255),
    ADD COLUMN district varchar(255),
    ADD COLUMN city varchar(255),
    ADD COLUMN state varchar(255),
    ADD COLUMN zip varchar(255),
    ADD COLUMN complement varchar(255);

UPDATE tb_employees
SET street = 'Rua do Colaborador',
    district = 'Centro',
    city = 'Sao Paulo',
    state = 'SP',
    zip = '01000000',
    complement = 'Cadastro existente'
WHERE street IS NULL;

ALTER TABLE tb_employees
    ALTER COLUMN street SET NOT NULL,
    ALTER COLUMN district SET NOT NULL,
    ALTER COLUMN city SET NOT NULL,
    ALTER COLUMN state SET NOT NULL,
    ALTER COLUMN zip SET NOT NULL;
