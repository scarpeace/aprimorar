CREATE TABLE IF NOT EXISTS tb_appointments (
  payment numeric(19,2) NOT NULL,
  price numeric(19,2) NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  employee_payment_date timestamp(6) with time zone,
  end_date timestamp(6) with time zone NOT NULL,
  start_date timestamp(6) with time zone NOT NULL,
  student_charge_date timestamp(6) with time zone,
  updated_at timestamp(6) with time zone,
  employee_id uuid NOT NULL,
  id uuid NOT NULL,
  student_id uuid NOT NULL,
  content varchar(255) NOT NULL CHECK (content in ('AULA','MENTORIA','TERAPIA','ORIENTACAO_VOCACIONAL','ENEM','PAS','OUTRO')),
  description text,
  employee_name varchar(255) NOT NULL,
  student_name varchar(255) NOT NULL,
  title varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tb_employees (
  active boolean NOT NULL,
  birthdate date,
  role smallint NOT NULL CHECK (role between 0 and 3),
  created_at timestamp(6) with time zone NOT NULL,
  updated_at timestamp(6) with time zone,
  id uuid NOT NULL,
  name varchar(50) NOT NULL,
  contact varchar(255) NOT NULL,
  cpf varchar(255) NOT NULL UNIQUE,
  duty varchar(255) NOT NULL CHECK (duty in ('TEACHER','ADM','THERAPIST','MENTOR','SYSTEM')),
  email varchar(255) NOT NULL,
  pix varchar(255),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tb_parent (
  active boolean NOT NULL,
  birthdate date,
  role smallint NOT NULL CHECK (role between 0 and 3),
  created_at timestamp(6) with time zone NOT NULL,
  updated_at timestamp(6) with time zone,
  id uuid NOT NULL,
  name varchar(50) NOT NULL,
  contact varchar(255) NOT NULL,
  cpf varchar(255) NOT NULL UNIQUE,
  email varchar(255) NOT NULL,
  pix varchar(255),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tb_students (
  active boolean NOT NULL,
  birthdate date,
  role smallint NOT NULL CHECK (role between 0 and 3),
  created_at timestamp(6) with time zone NOT NULL,
  updated_at timestamp(6) with time zone,
  id uuid NOT NULL,
  parent_id uuid NOT NULL,
  name varchar(50) NOT NULL,
  city varchar(255) NOT NULL,
  complement varchar(255),
  contact varchar(255) NOT NULL,
  cpf varchar(255) NOT NULL UNIQUE,
  district varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  pix varchar(255),
  school varchar(255) NOT NULL,
  state varchar(255) NOT NULL CHECK (state in ('AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MT','MS','MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP','SE','TO')),
  street varchar(255) NOT NULL,
  zip varchar(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tb_transactions (
  amount numeric(19,2) NOT NULL,
  settled_at timestamp(6) with time zone,
  id uuid NOT NULL,
  origin_id uuid NOT NULL,
  status varchar(20) NOT NULL CHECK (status in ('PENDING','SETTLED')),
  type varchar(20) NOT NULL CHECK (type in ('IN','OUT')),
  category varchar(50) NOT NULL CHECK (category in ('COBRANCA_ALUNO','PAGAMENTO_COLABORADOR','CONTAS','ADMINISTRATIVO','DESPENSA','MANUTENCAO','SERVICOS','MATERIAIS','ASSINATURAS')),
  origin varchar(50) NOT NULL CHECK (origin in ('APPOINTMENT_STUDENT_CHARGE','APPOINTMENT_EMPLOYEE_PAYMENT','GENERAL_EXPENSE')),
  PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS tb_students
  DROP CONSTRAINT IF EXISTS FKcssx8ttx5ktpha07yqsjstbnd;

ALTER TABLE IF EXISTS tb_students
  ADD CONSTRAINT FKcssx8ttx5ktpha07yqsjstbnd FOREIGN KEY (parent_id) REFERENCES tb_parent;

INSERT INTO tb_parent (id, name, birthdate, cpf, contact, email, pix, role, active, created_at, updated_at)
SELECT
  'ffffffff-ffff-ffff-ffff-ffffffffffff',
  'Responsavel Removido',
  DATE '2000-01-01',
  '000.000.000-00',
  '00000000000',
  'responsavel.removido@aprimorar.local',
  NULL,
  2,
  false,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM tb_parent WHERE id = 'ffffffff-ffff-ffff-ffff-ffffffffffff'
);

INSERT INTO tb_employees (id, name, birthdate, cpf, contact, email, pix, role, active, created_at, updated_at, duty)
SELECT
  '00000000-0000-4000-8000-000000000001',
  'Colaborador Removido',
  DATE '2000-01-01',
  '000.000.000-01',
  '00000000001',
  'colaborador.removido@aprimorar.local',
  '00000000001',
  1,
  false,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  'SYSTEM'
WHERE NOT EXISTS (
  SELECT 1 FROM tb_employees WHERE id = '00000000-0000-4000-8000-000000000001'
);

INSERT INTO tb_students (
  id,
  name,
  birthdate,
  cpf,
  contact,
  email,
  pix,
  role,
  active,
  created_at,
  updated_at,
  school,
  parent_id,
  street,
  district,
  city,
  state,
  zip,
  complement
)
SELECT
  '00000000-0000-0000-0000-000000000000',
  'Aluno Removido',
  DATE '2000-01-01',
  '000.000.000-02',
  '00000000002',
  'aluno.removido@aprimorar.local',
  NULL,
  0,
  false,
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP,
  'Sistema',
  'ffffffff-ffff-ffff-ffff-ffffffffffff',
  'Sistema',
  'Sistema',
  'Sistema',
  'SP',
  '00000000',
  NULL
WHERE NOT EXISTS (
  SELECT 1 FROM tb_students WHERE id = '00000000-0000-0000-0000-000000000000'
);
