## Context

Hoje o sistema registra fatos financeiros dentro de `Event` por meio de `price`, `payment`, `studentChargeDate` e `employeePaymentDate`, enquanto o módulo `finance` consolida números usando queries sobre `Event` e uma entidade separada de `GeneralExpense`. Isso cria acoplamento entre operação e leitura financeira, além de dificultar a evolução para um modulith com fronteiras mais claras.

A mudança proposta cria um modelo financeiro mínimo baseado em `Transaction`, sem introduzir parcelamento, descontos, pagamentos parciais ou outras regras mais complexas. O objetivo é reorganizar o domínio, não sofisticá-lo.

## Goals / Non-Goals

**Goals:**
- Introduzir `Transaction` como registro financeiro principal.
- Fazer com que eventos gerem transactions automaticamente no momento da criação.
- Permitir que o evento continue controlando as ações de marcar cobrança e pagamento.
- Centralizar no módulo `finance` os resumos e consultas monetárias de aluno, colaborador e negócio.
- Unificar despesas gerais no mesmo modelo financeiro, removendo `GeneralExpense`.

**Non-Goals:**
- Implementar parcelamento, desconto, estorno, pagamento parcial ou reconciliação bancária.
- Transformar `finance` no dono do fluxo operacional de eventos.
- Remover imediatamente `studentChargeDate` e `employeePaymentDate` do `Event`.
- Reestruturar todo o dashboard em um único módulo de leitura.

## Decisions

### 1. `Transaction` será uma entidade mínima e persistida
O módulo `finance` terá uma entidade própria com os campos `type`, `status`, `amount`, `origin`, `originId`, `settledAt` e `category`.

**Rationale:** isso cria uma linguagem de domínio financeira sem introduzir uma modelagem genérica demais.

**Alternatives considered:**
- Manter `finance` apenas como agregador de queries: resolve parte do acoplamento, mas não cria um domínio financeiro próprio.
- Criar uma transaction mais rica: aumentaria a complexidade antes de haver necessidade de negócio.

### 2. `Event` continua sendo a origem operacional e a fonte de ação
Ao criar um evento, o sistema cria imediatamente duas transactions `PENDING`: uma `IN` para cobrança do aluno e uma `OUT` para pagamento do colaborador. Ao marcar cobrado/pago no evento, a transaction correspondente é liquidada e o campo de data no evento continua sendo atualizado.

**Rationale:** preserva a UX e o fluxo atual, reduzindo o impacto na operação.

**Alternatives considered:**
- Tornar `finance` o dono das ações de liquidação: aumentaria o acoplamento entre tela operacional e módulo financeiro.
- Tornar `Transaction` a única fonte da verdade já nesta etapa: exigiria uma migração maior e mais arriscada.

### 3. `GeneralExpense` será substituída por transactions
Despesas gerais passarão a ser cadastradas diretamente como transactions `OUT` com `origin = GENERAL_EXPENSE`. Para esse caso, `originId` será o próprio `id` da transaction.

**Rationale:** mantém o modelo uniforme para todas as saídas financeiras e evita uma entidade paralela.

**Alternatives considered:**
- Manter `GeneralExpense` e gerar uma transaction espelho: duplicaria conceitos sem necessidade.
- Permitir `originId` nulo para despesas gerais: tornaria o modelo menos consistente.

### 4. Resumos financeiros passam a ser contratos do módulo `finance`
Os resumos de aluno e colaborador continuarão existindo na UI atual, mas serão fornecidos por endpoints do módulo `finance`, calculados a partir de transactions.

**Rationale:** a tela pode continuar composta por múltiplas queries, mas a responsabilidade do dado financeiro passa a ser explícita.

**Alternatives considered:**
- Manter os resumos em `student` e `employee`: preserva o acoplamento atual com queries financeiras fora do módulo `finance`.

### 5. Dashboard terá separação entre métricas operacionais e financeiras
Indicadores como quantidade e tipos de eventos permanecem em `event`. Totais monetários e lucro passam a ser calculados por `finance`.

**Rationale:** evita forçar o módulo `finance` a assumir métricas que não são financeiras.

## Risks / Trade-offs

- [Sincronização entre `Event` e `Transaction`] → Garantir criação e liquidação dentro da mesma transação de aplicação.
- [Migração de dados existentes] → Criar migration que materialize transactions a partir de eventos e despesas gerais já persistidos.
- [Duplicidade temporária de verdade financeira] → Manter `studentChargeDate` e `employeePaymentDate` apenas como reflexo operacional, e fazer os relatórios passarem a ler transactions.
- [Mudança de contratos de API] → Atualizar frontend e clients gerados junto com a mudança dos endpoints de resumo financeiro.
