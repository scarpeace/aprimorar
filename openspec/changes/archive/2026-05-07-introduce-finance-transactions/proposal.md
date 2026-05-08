## Why

O fluxo financeiro atual depende demais do módulo de eventos para registrar fatos, calcular resumos e alimentar consultas de aluno, colaborador e negócio. Isso dificulta a futura migração para um modulith porque o domínio financeiro ainda não possui uma linguagem e uma fronteira próprias.

## What Changes

- Introduzir uma entidade mínima de `Transaction` para representar entradas e saídas financeiras do sistema.
- Criar duas transactions automaticamente na criação de um evento: uma cobrança de aluno (`IN`) e um pagamento de colaborador (`OUT`), ambas inicialmente `PENDING`.
- Fazer com que as ações do evento para marcar cobrança e pagamento liquidem a transaction correspondente e mantenham `studentChargeDate` e `employeePaymentDate` sincronizados.
- Substituir o cadastro de `GeneralExpense` por transactions de saída com `origin = GENERAL_EXPENSE`.
- Mover a responsabilidade dos relatórios e resumos financeiros para o módulo `finance`.
- Separar indicadores operacionais e financeiros no dashboard, mantendo métricas de eventos no módulo `event` e consolidações monetárias no módulo `finance`.

## Capabilities

### New Capabilities
- `finance-transactions`: Introduz transactions mínimas como registro financeiro principal para eventos, despesas gerais e consolidação monetária.

### Modified Capabilities
- `student-financial-summary`: O resumo financeiro do aluno passa a ser fornecido pelo módulo `finance`, mantendo o comportamento exibido na tela de detalhes do aluno.
- `employee-financial-summary`: O resumo financeiro do colaborador passa a ser fornecido pelo módulo `finance`, mantendo o comportamento exibido na tela de detalhes do colaborador.

## Impact

- Backend Spring: novos modelos, repositórios, serviços e endpoints no domínio `finance`.
- Backend de eventos: criação e liquidação de transactions em operações de create e toggle financeiro.
- Remoção da entidade e do fluxo de `GeneralExpense` como modelo financeiro separado.
- Frontend: ajustes nos hooks/endpoints de resumo financeiro e de despesas gerais.
- Dashboard: separação explícita entre consultas operacionais e financeiras.
