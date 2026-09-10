ALTER TABLE alunos
  ADD COLUMN responsavel_nome VARCHAR(50),
  ADD COLUMN responsavel_data_nascimento DATE,
  ADD COLUMN responsavel_cpf VARCHAR(255),
  ADD COLUMN responsavel_telefone VARCHAR(20),
  ADD COLUMN responsavel_email VARCHAR(255);

UPDATE alunos AS aluno
SET responsavel_nome = responsavel.nome,
    responsavel_data_nascimento = responsavel.data_nascimento,
    responsavel_cpf = responsavel.cpf,
    responsavel_telefone = responsavel.telefone,
    responsavel_email = responsavel.email
FROM responsaveis AS responsavel
WHERE responsavel.id = aluno.responsavel_id;

ALTER TABLE alunos
  ALTER COLUMN responsavel_nome SET NOT NULL,
  ALTER COLUMN responsavel_cpf SET NOT NULL,
  ALTER COLUMN responsavel_telefone SET NOT NULL,
  ALTER COLUMN responsavel_email SET NOT NULL;
