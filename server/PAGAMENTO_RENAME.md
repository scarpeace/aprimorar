# Plano — Renomeação de pagamentos para cobranças de alunos

## Objetivo

Renomear o conceito de `pagamento_aluno` para `cobranca_aluno` em todo o
backend, incluindo banco de dados, pacote Java, entidade, serviços, rotas,
DTOs, exceções e documentação.

O comportamento atual será preservado: a cobrança nasce pendente, pode ser
paga em lote e pode retornar para pendente quando o lote for cancelado.

## Convenções

Como identificadores Java e SQL não usam acentos:

- Banco: `pagamentos_alunos` → `cobrancas_alunos`;
- Pacote: `pagamento_aluno` → `cobranca_aluno`;
- Entidade: `PagamentoAlunoEntity` → `CobrancaAlunoEntity`.

Os campos relacionados à liquidação continuam com seus nomes atuais:
`data_pagamento`, `forma_pagamento`, `pagar` e `cancelarPagamento`.

O agrupador atualmente chamado `lote` passará a se chamar `pagamento_id`.

## Fases

### Fase 1 — Renomeação do banco

Criar a próxima migration Flyway disponível, sem editar migrations já
aplicadas.

A migration deverá:

- renomear `pagamentos_alunos` para `cobrancas_alunos`;
- renomear a coluna `lote` para `pagamento_id`;
- renomear a sequence usada para gerar os `pagamento_id`;
- renomear índices;
- renomear constraints;
- preservar todos os dados existentes.

Não haverá cópia de dados nem criação de tabela paralela.

### Fase 2 — Renomeação do domínio

Mover:

```text
financeiro/pagamento_aluno/
→ financeiro/cobranca_aluno/
```

Renomear:

- `PagamentoAlunoEntity` para `CobrancaAlunoEntity`;
- `StatusPagamentoAluno` para `StatusCobrancaAluno`;
- `PagamentoAlunoRepository` para `CobrancaAlunoRepository`;
- a propriedade `lote` para `pagamentoId`;
- exceções e imports relacionados.

### Fase 3 — Listener de atendimento

Renomear `PagamentoAlunoEventListener` para
`CobrancaAlunoEventListener`.

O evento público `AtendimentoCreatedEvent` permanecerá em
`atendimentos/individuais/api`. O listener do financeiro continuará criando
uma cobrança pendente após a criação do atendimento.

A fronteira do Spring Modulith não será ampliada.

### Fase 4 — API HTTP

Renomear controller, serviços e DTOs para `CobrancaAluno`.

As rotas passarão a ser:

```text
POST  /v1/cobrancas-alunos/pagar
PATCH /v1/cobrancas-alunos/pagamentos/{pagamentoId}/cancelar
```

Também serão atualizados os `operationId`, descrições OpenAPI, títulos dos
handlers e mensagens relacionadas ao domínio.

Como a aplicação está em desenvolvimento para a versão 2.0, não haverá alias
para as rotas antigas.

### Fase 5 — Limpeza

Remover:

- a pasta `pagamento_aluno`;
- classes com o prefixo `PagamentoAluno`;
- referências a `pagamentos_alunos`;
- referências à sequence antiga;
- imports e configurações antigas;
- documentação desatualizada.

Verificar referências residuais com:

```bash
rg "PagamentoAluno|pagamento_aluno|pagamentos_alunos"
```

### Fase 6 — Validação

Executar:

```bash
./mvnw clean compile
./mvnw test-compile
```

Depois validar no banco sem reset:

1. criar um atendimento;
2. confirmar a criação de uma cobrança pendente;
3. pagar a cobrança;
4. conferir `pagamento_id`, status e data de pagamento;
5. cancelar o agrupamento pelo `pagamento_id`;
6. conferir que a cobrança voltou para `PENDENTE`.

## Fora deste plano

- alteração do fluxo de repasses de colaboradores;
- alteração dos valores do atendimento;
- mudança para uma tabela de lotes;
- alterações no frontend;
- compatibilidade com as rotas antigas.
