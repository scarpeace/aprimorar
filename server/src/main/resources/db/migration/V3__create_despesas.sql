CREATE TABLE despesas (
  id BIGSERIAL NOT NULL PRIMARY KEY,
  titulo VARCHAR(120) NOT NULL,
  categoria VARCHAR(40) NOT NULL CHECK (categoria IN ('CONTAS','PROFESSORES','FUNCIONARIOS','DESPENSA','MANUTENCAO','SERVICOS','ASSINATURAS')),
  valor NUMERIC(10,2) NOT NULL CHECK (valor > 0),
  data_pagamento DATE NOT NULL,
  forma_pagamento VARCHAR(40) NOT NULL CHECK (forma_pagamento IN ('PIX','DINHEIRO','CARTAO_CREDITO','CARTAO_DEBITO','BOLETO','TRANSFERENCIA')),
  descricao VARCHAR(500),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE INDEX idx_despesas_categoria ON despesas(categoria);
CREATE INDEX idx_despesas_data_pagamento ON despesas(data_pagamento);
