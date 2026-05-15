CREATE TABLE IF NOT EXISTS tb_expenses (
  amount numeric(19,2) NOT NULL,
  date date NOT NULL,
  created_at timestamp(6) with time zone NOT NULL,
  updated_at timestamp(6) with time zone,
  id uuid NOT NULL,
  category varchar(50) NOT NULL CHECK (category in ('CONTAS','ADMINISTRATIVO','DESPENSA','MANUTENCAO','SERVICOS','MATERIAIS','ASSINATURAS')),
  description text NOT NULL,
  PRIMARY KEY (id)
);
