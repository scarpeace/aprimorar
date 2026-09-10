# Plano — Pagamentos de alunos por atendimento

## Objetivo

Transferir a propriedade financeira do pagamento do aluno para
`financeiro/pagamento_aluno`.

Cada atendimento individual criará um pagamento pendente próprio. O pagamento
não será mais criado manualmente por `atendimentos`, nem haverá um vínculo
`pagamento_aluno_id` na tabela `atendimentos`.

## Decisões fechadas

- Um atendimento gera exatamente um pagamento de aluno pendente.
- `pagamentos_alunos` é a fonte de verdade para valor, desconto e estado do
  pagamento.
- Um lote é um `BIGINT` sequencial gerado por uma sequence PostgreSQL, sem
  entidade ou tabela de lote.
- Um pagamento em lote exige que todos os pagamentos sejam do mesmo aluno.
- O desconto é informado individualmente para cada pagamento; não haverá
  rateio de um desconto total.
- `comprovante_url`, forma e data de pagamento são repetidos nas linhas de um
  mesmo lote. É uma desnormalização intencional e suficiente neste momento.
- Inicialmente há somente os estados `PENDENTE` e `PAGO`. Não existe
  `ATRASADO` sem uma data de vencimento, nem `CANCELADO`: cancelar um pagamento
  apenas o devolve a `PENDENTE`.
- `repasse_colaborador` permanece em atendimentos até existir seu submódulo
  financeiro próprio.
- Eventos são internos ao monólito e síncronos, dentro da mesma transação. Não
  criaremos outbox, mensageria ou processamento assíncrono agora.

## Baseline de desenvolvimento

As migrations históricas `V1` a `V15` foram condensadas em uma nova
`V1__initial_schema.sql`, equivalente ao schema que o código atual usa antes do
redesenho de pagamentos. A `V15` experimental não faz mais parte do histórico.

O banco local será recriado a partir dessa baseline. O redesenho de pagamentos
começará em uma migration posterior, junto com a alteração das entidades, para
que cada migration continue correspondendo a uma mudança implementada.

## Modelo de dados desejado

```text
pagamentos_alunos
├── id
├── atendimento_id        UNIQUE NOT NULL
├── aluno_id              NOT NULL
├── valor                 NOT NULL
├── desconto              NULL
├── status                NOT NULL  (PENDENTE | PAGO)
├── lote                  NULL      (BIGINT sequencial)
├── data_pagamento        NULL
├── forma_pagamento       NULL
├── comprovante_url       NULL
├── created_at
└── updated_at
```

`atendimento_id` e `aluno_id` são IDs escalares: não haverá relacionamento
JPA entre os módulos. A unicidade de `atendimento_id` garante que um
atendimento não terá duas cobranças pendentes ou pagas ao mesmo tempo.

## Fluxo desejado

```text
POST /v1/atendimentos
        │
        ▼
AtendimentoServiceImpl salva o atendimento
        │
        ▼
AtendimentoCreatedEvent(atendimentoId, alunoId, valor)
        │
        ▼
PagamentoAlunoEventListener cria PagamentoAlunoEntity PENDENTE
```

O listener usará `@TransactionalEventListener(BEFORE_COMMIT)`. Se a criação do
pagamento falhar, a transação inteira falha e o atendimento não é confirmado.

## Fase 1 — Migration para o novo modelo

- Criar a próxima migration livre, sem editar `V14__create_pagamentos_alunos.sql`.
- Criar a sequence de lotes.
- Evoluir `pagamentos_alunos` com `atendimento_id`, `aluno_id`, `status`,
  `lote` e `comprovante_url`.
- Adicionar `valor` como coluna de transição e permitir `data_pagamento` e
  `forma_pagamento` nulos enquanto o estado for `PENDENTE`. `total` e `valor`
  permanecem temporariamente para não quebrar o código antigo.
- Criar constraints para valor/desconto e índices para `lote` e
  `(aluno_id, status)`.
- Manter temporariamente `atendimentos.pagamento_aluno_id` e os campos antigos
  de `pagamentos_alunos` para que o código atual continue iniciando durante a
  transição. A remoção definitiva fica na Fase 4, depois da troca das
  entidades. O campo antigo `total` continua obrigatório até o serviço novo
  passar a gravar o campo `valor`.

### Dados já existentes

O modelo atual permite que um único pagamento seja vinculado a vários
atendimentos. A migration fará o backfill dos vínculos sem ambiguidade e
manterá os campos legados para os casos que ainda precisarem de tratamento na
Fase 4. O valor novo será obtido de `atendimentos.pagamento_aluno`.

Antes de escrevê-la, conferiremos se o banco de desenvolvimento contém vínculos
de pagamento. Caso contenha descontos agregados antigos, a regra de conversão
precisa ser definida explicitamente; não distribuiremos descontos automaticamente.

**Após esta fase será necessário reiniciar a aplicação** para o Flyway aplicar
a migration.

## Fase 2 — Domínio de pagamento pendente

- Criar `StatusPagamentoAluno` com `PENDENTE` e `PAGO`.
- Adaptar `PagamentoAlunoEntity` para nascer pendente com
  `atendimentoId`, `alunoId` e `valor`.
- Tornar `valor`, `atendimento_id` e `aluno_id` obrigatórios depois que o
  serviço novo estiver em uso; isso ficará em migration posterior.
- Centralizar na entidade as transições `pagar(...)` e
  `cancelarPagamento()`.
- Validar valor obrigatório e positivo, desconto individual não negativo e
  menor ou igual ao valor.
- Adicionar ao repositório as consultas necessárias para pagamentos pendentes,
  lote e bloqueio das linhas selecionadas durante o pagamento.

## Fase 3 — Evento público de atendimento

- Recriar `atendimentos/individuais/api` apenas com o contrato que será usado:
  `AtendimentoCreatedEvent`.
- Declarar o pacote como interface nomeada no `package-info.java`.
- Publicar o evento depois de persistir o atendimento.
- Criar, em `financeiro/pagamento_aluno`, o listener que consome o evento e
  cria o pagamento pendente.
- Remover a chamada direta de `AtendimentoServiceImpl` para
  `PagamentoAlunoService`.

O evento carregará todos os dados de que o financeiro precisa. O listener não
consultará entidade, repository ou service interno de atendimentos.

## Fase 4 — Remover o pagamento do atendimento

- Criar uma migration posterior para remover `pagamentoAlunoId`,
  `vincularPagamento(...)` e
  `desvincularPagamento()` de `AtendimentoEntity`.
- Remover `pagamentoAlunoId` de `AtendimentoResponse`.
- O valor de pagamento continuará no request de agendamento apenas como dado de
  entrada para o evento; ele deixará de ser coluna de `atendimentos`.
- Remover nessa etapa as colunas legadas `pagamento_aluno`, `total`,
  `data_pagamento`, `forma_pagamento` e o vínculo `atendimentos.pagamento_aluno_id`.
- Ajustar a validação atual que compara pagamento e repasse: ela continuará no
  fluxo de agendamento usando os valores recebidos, mas não dependerá de uma
  coluna financeira no atendimento.
- Publicar eventos de atualização e exclusão quando essas operações forem
  ajustadas:
  - atendimento com pagamento `PENDENTE`: o financeiro pode atualizar ou
    remover sua linha;
  - atendimento com pagamento `PAGO`: atualização de valor e exclusão devem
    ser recusadas até existir uma regra de estorno.

## Fase 5 — Operações HTTP de pagamento no financeiro

- Criar controller e DTOs em `financeiro/pagamento_aluno/web`.
- Substituir `RegistrarPagamentoRequest`, que recebe IDs de atendimentos, por
  um request que recebe IDs de pagamentos e desconto por item.
- Ao pagar:
  1. carregar e bloquear os pagamentos selecionados;
  2. confirmar que todos estão pendentes e pertencem ao mesmo aluno;
  3. obter o próximo número da sequence de lote;
  4. aplicar lote, forma, comprovante, data atual e desconto individual;
  5. mudar todos para `PAGO`.
- Ao cancelar um lote, carregar suas linhas e devolvê-las a `PENDENTE`,
  limpando lote, data, forma, comprovante e desconto aplicado.
- Remover os endpoints e DTOs de pagamento do módulo de atendimentos.

## Fase 6 — Regra de desativação do aluno

- Em `pessoas/aluno/api`, criar uma porta mínima para a pergunta que o módulo
  realmente precisa: se o aluno possui pagamento pendente.
- Implementá-la em `financeiro/pagamento_aluno` com consulta por
  `aluno_id` e `status = PENDENTE`.
- Usá-la em `AlunoServiceImpl.deactivateAluno(...)`.

Essa porta fica em pessoas porque é a necessidade de pessoas. Assim evitamos a
dependência circular `pessoas -> financeiro -> atendimentos -> pessoas`.

## Fase 7 — Limpeza e validação

- Remover contratos públicos de pagamento que não tiverem consumidor real.
- Remover imports, queries, DTOs e rotas residuais de atendimentos.
- Atualizar `AGENTS.md` com a nova propriedade dos dados financeiros.
- Executar `compile`, `test-compile` e `ModuleVerificationTest`.
- Verificar que não restaram `pagamento_aluno_id`, `registrarPagamento` ou
  `cancelarPagamento` dentro de atendimentos.

## Fora deste plano

- Criar `financeiro/repasse_colaborador`.
- Anexar ou armazenar o arquivo do comprovante; por enquanto só persistimos a
  URL.
- Criar uma entidade/tabela de lote.
- Processamento assíncrono, outbox, mensageria ou integração externa.
