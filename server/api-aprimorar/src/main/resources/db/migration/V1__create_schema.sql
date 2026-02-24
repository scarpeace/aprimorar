CREATE TABLE IF NOT EXISTS tb_parent (
                                         parent_id UUID PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    CONSTRAINT uk_parent_name UNIQUE (name),
    CONSTRAINT uk_parent_contact UNIQUE (contact),
    CONSTRAINT uk_parent_email UNIQUE (email),
    CONSTRAINT uk_parent_cpf UNIQUE (cpf)
    );