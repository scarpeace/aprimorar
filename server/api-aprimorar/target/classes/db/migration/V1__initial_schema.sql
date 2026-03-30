CREATE TABLE tb_parent (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    contact VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    archived_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE tb_employees (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    pix VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    duty VARCHAR(50) NOT NULL,
    archived_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE tb_students (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    birthdate DATE NOT NULL,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    school VARCHAR(255) NOT NULL,
    parent_id UUID NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    district VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip VARCHAR(255) NOT NULL,
    complement VARCHAR(255),
    archived_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_students_parent FOREIGN KEY (parent_id) REFERENCES tb_parent (id)
);

CREATE TABLE tb_events (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    payment NUMERIC(19, 2) NOT NULL,
    content VARCHAR(50) NOT NULL,
    student_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    archived_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT fk_events_student FOREIGN KEY (student_id) REFERENCES tb_students (id),
    CONSTRAINT fk_events_employee FOREIGN KEY (employee_id) REFERENCES tb_employees (id)
);

CREATE INDEX idx_students_parent_id ON tb_students (parent_id);
CREATE INDEX idx_events_student_id ON tb_events (student_id);
CREATE INDEX idx_events_employee_id ON tb_events (employee_id);
CREATE INDEX idx_events_start_date_time ON tb_events (start_date_time);
