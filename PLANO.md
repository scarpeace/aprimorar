# Plano - KPIs reais na tela de detalhes do aluno

## Objetivo

Popular os KPIs de `client/src/features/students/pages/StudentDetailsPage.tsx` com dados reais para:

- total de aulas/eventos
- total pago
- total pendente

## Estado atual

- `StudentKPIs` usa valores mockados em `client/src/features/students/components/StudentKPIs.tsx`.
- O total de eventos do endpoint paginado de appointments existe, mas deve usar `totalElements`, nao `size`.
- O backend ja possui o DTO `StudentSummaryDTO`, mas os endpoints de summary do aluno estao comentados.
- As transacoes financeiras do aluno sao sincronizadas quando a cobranca do appointment e alterada, entao o modulo financeiro pode ser a fonte de verdade para pago e pendente.

## Decisao de implementacao

Implementar um resumo real do aluno no backend e consumir esse resumo no frontend, em vez de misturar:

- metadados da paginacao para total de eventos
- modulo financeiro separado para pago e pendente

Essa abordagem mantem os 3 KPIs consistentes, com o mesmo filtro de data e uma unica query no frontend.

## Plano de execucao

### 1. Reativar endpoint de resumo do aluno no backend

Arquivos-alvo provaveis:

- `server/src/main/java/aprimorar/registration/student/internal/StudentController.java`
- `server/src/main/java/aprimorar/registration/student/api/StudentService.java`
- `server/src/main/java/aprimorar/registration/student/internal/StudentServiceImpl.java`

Acao:

- reativar `GET /students/{id}/summary`
- aceitar `startDate` e `endDate` opcionais
- retornar `StudentSummaryDTO`

### 2. Implementar calculo do resumo no backend

Dados esperados:

- `totalEvents`
- `totalCharged`
- `totalPending`

Fonte sugerida:

- contagem de eventos via modulo de appointments
- totais financeiros via transacoes sincronizadas

Observacao:

- o codigo comentado do finance usa `TransactionOrigin.EVENT_STUDENT_CHARGE`, mas o enum atual e `APPOINTMENT_STUDENT_CHARGE`; isso precisa ser corrigido se esse trecho for reaproveitado

### 3. Decidir onde fica a orquestracao do resumo

Opcao recomendada:

- expor o endpoint em `students/{id}/summary`
- deixar a implementacao delegar internamente para appointments/finance, se necessario

Motivo:

- a tela e de detalhe do aluno, entao faz sentido o frontend consumir o resumo a partir do modulo de aluno

### 4. Gerar hooks/tipos do frontend

Depois da mudanca no backend:

- rodar `npm run sync` em `client/`

Resultado esperado:

- hook gerado para buscar o resumo do aluno
- tipos atualizados para `StudentSummaryDTO`

### 5. Trocar KPIs mockados por query real

Arquivos-alvo provaveis:

- `client/src/features/students/components/StudentKPIs.tsx`
- `client/src/features/students/pages/StudentDetailsPage.tsx`

Acao:

- remover `mockKpis`
- consumir a query real de summary
- exibir loading/erro de forma consistente com o resto da pagina

### 6. Centralizar filtros e queries da pagina

Refatoracao recomendada:

- criar um hook de pagina, por exemplo `useStudentPageData(studentId)`

Responsabilidades:

- ler `startDate` e `endDate`
- disparar query da tabela
- disparar query dos KPIs
- garantir que tabela e KPIs usem os mesmos filtros

### 7. Corrigir inconsistencias relacionadas

Ponto identificado:

- a tabela do aluno usa `hideCharged` no frontend
- o tipo gerado do endpoint de appointments por aluno hoje expoe `hidePaid`
- o backend atualmente nao aplica esse filtro no service

Acao:

- revisar esse contrato antes ou durante a implementacao para evitar comportamento incoerente

## Ordem recomendada

1. Reativar e implementar o summary no backend
2. Gerar os hooks com `npm run sync`
3. Ligar `StudentKPIs` ao endpoint real
4. Refatorar a pagina para centralizar filtros e queries
5. Revisar o filtro `hideCharged` x `hidePaid`

## Critrios de pronto

- `StudentKPIs` nao usa mais mocks
- total de eventos reflete o total real do aluno no periodo filtrado
- total pago reflete transacoes liquidadas do aluno no periodo filtrado
- total pendente reflete transacoes pendentes do aluno no periodo filtrado
- KPIs e tabela respondem ao mesmo intervalo de datas
- atualizacoes de cobranca invalidam e recarregam os dados corretamente

## Observacoes

- Se precisarmos de uma entrega incremental, a etapa mais curta e usar `totalElements` para o KPI de eventos primeiro.
- Mesmo nessa entrega incremental, a versao final recomendada continua sendo consolidar os 3 KPIs em um endpoint unico de summary.
