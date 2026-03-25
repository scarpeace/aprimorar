# Plano de Implementacao - Dashboard MVP (1 semana)

## Objetivo
Entregar um dashboard operacional com navegacao por mes, KPIs mensais e grafico de pizza por tipo de conteudo, com dados agregados no backend e atualizacao automatica a cada 60 segundos.

## Escopo do MVP (versao simplificada)
- Usuario principal: Operacao.
- Painel informativo (sem acoes transacionais).
- Navegacao temporal por mes (setas anterior/proximo).
- Sem navegacao semanal.
- Sem grafico de linha no MVP.

## Regras de negocio aprovadas
- Timezone oficial: `America/Sao_Paulo`.
- KPIs exibidos:
  - `activeStudentsInMonth`: alunos distintos com aula no mes.
  - `classesInMonth`: total de eventos no mes.
  - `revenueInMonth`: soma de `event.price` no mes.
  - `costInMonth`: soma de `event.payment` no mes.
- Grafico de pizza:
  - distribuicao de eventos por `EventContent` no mes.
- Registros de sistema:
  - `Aluno Removido` e `Colaborador Removido` entram nas somas de eventos e financeiro.
  - `Aluno Removido` nao entra em `activeStudentsInMonth`.
- Atualizacao automatica:
  - frontend com `refetchInterval = 60000`.

## Navegacao temporal (UX)
- Estado principal: `year + month`.
- Seta para direita: avanca para o proximo mes.
- Seta para esquerda: volta para o mes anterior.
- Ao trocar de mes, todos os KPIs e graficos passam a refletir o novo mes.

## Contrato alvo da API
Endpoint:
- `GET /v1/dashboard/summary?year=YYYY&month=MM`

Resposta (estrutura plana):
```json
{
  "year": 2026,
  "month": 3,
  "prevYear": 2026,
  "prevMonth": 2,
  "nextYear": 2026,
  "nextMonth": 4,
  "activeStudentsInMonth": 42,
  "classesInMonth": 118,
  "revenueInMonth": 15430.5,
  "costInMonth": 8630.0,
  "charts": [
    { "content": "AULA", "count": 56, "percentage": 47.46 },
    { "content": "MENTORIA", "count": 22, "percentage": 18.64 }
  ],
  "generatedAt": "2026-03-20T10:00:00Z",
  "refreshSeconds": 60
}
```

## Validacoes de entrada (backend)
- `year`: obrigatorio, faixa `2000..2100`.
- `month`: obrigatorio, faixa `1..12`.
- Requisicao invalida retorna `400 BAD_REQUEST`.

## Comportamento para estado vazio
- Sem eventos no mes:
  - `activeStudentsInMonth = 0`
  - `classesInMonth = 0`
  - `revenueInMonth = 0`
  - `costInMonth = 0`
  - `charts = []`
  - resposta `200 OK`

## Pipeline OpenAPI + Kubb

### Backend (documentacao)
- `DashboardController`: `@Tag`, `@Operation`, `@ApiResponses`, `@Parameter`
- `DashboardSummaryResponseDTO`: `@Schema` em todos os campos
- `springdoc-openapi`: integrado (Swagger UI em `/swagger-ui.html`)

### Geracao do JSON
- Profile Maven: `./mvnw -Pgenerate-openapi generate-resources`
- JSON gerado em: `server/api-aprimorar/target/generated/openapi/openapi.json`
- Ou via curl: `curl http://localhost:8080/v3/api-docs > client/src/gen/openapi.json`

### Frontend (Kubb)
- Config: `client/kubb.config.ts`
- Saida: `client/src/gen/`
- Plugins: `@kubb/plugin-oas`, `@kubb/plugin-ts`, `@kubb/plugin-zod`, `@kubb/plugin-react-query`

### Comandos
```bash
# Baixar openapi.json do backend
npm run generate

# Gerar tipos + schemas + hooks
npm run sync
```

### Arquivos gerados (182 arquivos)
```
client/src/gen/
├── openapi.json          <- fonte de verdade
├── types/                <- TypeScript types
│   └── DashboardSummaryResponseDTO.ts
├── zod/                  <- Zod schemas
│   └── dashboardSummaryResponseDTOSchema.ts
└── hooks/                 <- React Query hooks
    └── useGetSummary.ts
```

## Etapas e controle de progresso
Legenda:
- `[ ]` Pendente
- `[~]` Em progresso
- `[x]` Concluida

### Etapa 1 - Contrato e desenho tecnico
Status: `[x]`

### Etapa 2 - Backend de agregacao
Status: `[x]`
- DashboardController, DashboardService, DashboardSummaryResponseDTO
- Queries agregadas no EventRepository
- springdoc-openapi configurado

### Etapa 3 - OpenAPI + Kubb
Status: `[x]`
- Documentacao OpenAPI completa no controller e DTO
- springdoc-openapi-maven-plugin configurado no pom.xml
- Kubb configurado com 4 plugins
- 182 arquivos gerados (tipos + Zod + hooks)

### Etapa 4 - UI Dashboard
Status: `[ ]`

Entregas:
- Topo com navegacao por mes (setas).
- Exibicao de mes/ano atual.
- Quatro cards de KPI mensais.
- Grafico de pizza por `EventContent`.
- Estados de carregamento, erro e vazio.
- Integracao com hooks gerados pelo Kubb.

Criterios de conclusao:
- Navegacao mensal atualiza KPIs e pizza corretamente.
- Hook `useGetSummary` integrado com cache hibrido (`refetchInterval: 60000`).

### Etapa 5 - Validacao final
Status: `[ ]`

Entregas:
- Validacoes locais:
  - Backend: `./mvnw test`
  - Frontend: `npm run lint` e `npm run build`
- Checklist final de aceite funcional.
- Registro de proximos passos (fase 2).

Criterios de conclusao:
- Comandos de verificacao sem falhas.
- MVP pronto para uso operacional inicial.

## Checklist de aceite do MVP
- [ ] Navegacao por mes funcionando.
- [ ] KPIs mensais corretos para o mes selecionado.
- [ ] Grafico de pizza por conteudo funcionando.
- [ ] `Aluno Removido` excluido apenas do KPI de alunos ativos.
- [ ] Estados de loading/erro/vazio implementados.
- [ ] Atualizacao automatica de 60s funcionando.

## Proximos passos (fora do MVP)
- Comparacao com periodo anterior (delta e percentual).
- Inclusao do modulo de despesas para custo completo.
- Drill-down por colaborador, turma e tipo de evento.
- Reintroduzir grafico temporal quando houver demanda operacional.
