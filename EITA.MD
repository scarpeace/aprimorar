# Plano: Absorver Finance em Appointment

## Objetivo

Eliminar o modulo `finance` e concentrar a logica financeira operacional em `appointment`, mantendo apenas uma estrutura minima para despesas gerais.

## Ideia Central

O `Appointment` ja possui os dados necessarios para representar o financeiro de alunos e colaboradores:

- `price`: valor cobrado do aluno
- `payment`: valor pago ao colaborador
- `studentChargeDate`: define se a cobranca foi realizada
- `employeePaymentDate`: define se o pagamento foi realizado
- `studentId` / `studentName`
- `employeeId` / `employeeName`
- `startDate`: data-base para filtros por periodo

Com isso, as consultas de resumo, pendencias e agregados por aluno/colaborador podem sair direto de `tb_appointments`, sem `Transaction`, sem eventos e sem `JOIN` com outra tabela.

## O que permanece no dominio

### Appointment

Manter o `Appointment` como fonte de verdade para:

- cobrancas de alunos
- pagamentos de colaboradores
- status pendente/liquidado
- consultas agregadas por periodo
- consultas agregadas por aluno
- consultas agregadas por colaborador

### Expense

Criar uma entidade minima para despesas gerais, por exemplo `Expense`, para cobrir gastos que nao pertencem a um atendimento.

Campos sugeridos:

- `id`
- `amount`
- `description`
- `category`
- `date`

Observacao: `Expense` substitui apenas a parte de despesas gerais do modulo atual. Todo o resto migra para `appointment`.

## Estrutura Enxuta Proposta

### Entidades

- `Appointment`
  - continua como entidade principal
  - sem dependencia em `Transaction`
- `Expense`
  - nova entidade leve para saidas gerais

### Repositorios

- `AppointmentRepository`
  - adicionar queries agregadas com `GROUP BY`
  - usar `price` + `studentChargeDate` para alunos
  - usar `payment` + `employeePaymentDate` para colaboradores
- `ExpenseRepository`
  - CRUD simples
  - agregados por categoria e por periodo

### Servicos

- mover resumos financeiros para `AppointmentService`
- mover consultas por aluno/colaborador para `AppointmentService`
- criar `ExpenseService` simples para despesas gerais

### Controllers

- expor endpoints financeiros a partir de `appointment` ou de um facade leve se quisermos manter a API organizada
- expor despesas gerais por um controller simples de `Expense`

## Queries Principais

### Resumo consolidado

Calcular a partir de `Appointment` + `Expense`:

- `totalStudentCharged`: `sum(price)` onde `studentChargeDate is not null`
- `totalStudentPending`: `sum(price)` onde `studentChargeDate is null`
- `totalEmployeePaid`: `sum(payment)` onde `employeePaymentDate is not null`
- `totalEmployeePending`: `sum(payment)` onde `employeePaymentDate is null`
- `totalGeneralExpenses`: `sum(amount)` em `Expense`
- `balance`: `totalStudentCharged - totalEmployeePaid - totalGeneralExpenses`

### Resumo por aluno

Query em `AppointmentRepository` com `GROUP BY a.studentId, a.studentName`:

- `sum(price)` com `studentChargeDate is not null`
- `sum(price)` com `studentChargeDate is null`

### Resumo por colaborador

Query em `AppointmentRepository` com `GROUP BY a.employeeId, a.employeeName`:

- `sum(payment)` com `employeePaymentDate is not null`
- `sum(payment)` com `employeePaymentDate is null`

## DTOs Sugeridos

### Alunos

- `StudentFinanceSummaryDTO`
  - `studentId`
  - `studentName`
  - `totalCharged`
  - `totalPending`

- `StudentsFinanceSummaryResponseDTO`
  - `startDate`
  - `endDate`
  - `students`

### Colaboradores

- `EmployeeFinanceSummaryDTO`
  - `employeeId`
  - `employeeName`
  - `totalPaid`
  - `totalPending`

- `EmployeesFinanceSummaryResponseDTO`
  - `startDate`
  - `endDate`
  - `employees`

### Despesas gerais

- `ExpenseRequestDTO`
- `ExpenseResponseDTO`

## Plano de Migracao

1. Criar `Expense` + repository + service + controller.
2. Replicar os resumos consolidados usando `AppointmentRepository` + `ExpenseRepository`.
3. Criar queries agregadas por aluno e por colaborador em `AppointmentRepository`.
4. Atualizar frontend para consumir os novos endpoints.
5. Remover dependencia do frontend em transacoes antigas.
6. Remover `Transaction` e seus fluxos.
7. Remover o modulo `finance` por completo.
8. Rodar sync do OpenAPI e limpar codigo gerado obsoleto.

## Limpeza de Codigo Legado

Precisamos lembrar de remover o que nao fizer mais sentido:

### Backend

- entidade `Transaction`
- `TransactionRepository`
- `TransactionMapper`
- `TransactionServiceImpl`
- `FinanceService`
- `FinanceServiceImpl`
- `FinanceController`
- DTOs de transacao atuais
- enums ligados a `Transaction`:
  - `TransactionOrigin`
  - `TransactionType`
  - `TransactionStatus`
  - avaliar renomear `TransactionCategory` para `ExpenseCategory`
- listeners/eventos ligados a sincronizacao financeira
- excecoes e handlers exclusivos do modulo `finance`
- migracoes Flyway que nao fizerem mais sentido no desenho final

### Frontend

- hooks Kubb de transacoes e finance legacy
- paginas, componentes e tabelas que assumem `TransactionResponseDTO`
- filtros e estados que so existiam para listagem de transacoes
- summaries baseados no backend antigo

### Verificacoes cruzadas

- invalidacoes de query React Query apontando para chaves antigas de finance
- imports quebrados apos o `npm run sync`
- testes que dependem do modulo `finance`
- documentacao OpenSpec e specs relacionadas ao financeiro

## Riscos e Cuidados

- despesas gerais nao podem ser perdidas na simplificacao
- migracao de dados de `tb_transactions` para a nova estrutura precisa ser planejada
- qualquer endpoint publico usado pelo frontend precisa ter substituicao clara antes da remocao
- verificar impacto no `PendingFinancialBalanceChecker`
- revisar dependencias de Modulith antes de mover responsabilidades

## Resultado Esperado

Ao final, o sistema deve ter:

- um fluxo financeiro operacional concentrado em `appointment`
- uma estrutura minima e clara para despesas gerais
- menos sincronizacao entre modulos
- menos duplicacao de estado
- queries mais simples
- menos superficie de manutencao
