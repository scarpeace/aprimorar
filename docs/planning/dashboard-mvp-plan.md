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

## Contrato alvo da API (resumo)
Endpoint unico para a tela:
- `GET /v1/dashboard/summary?year=YYYY&month=MM`

Resposta deve conter:
- `period`: periodo atual e ponteiros de navegacao mensal (`prev`/`next`).
- `kpis`: metricas mensais.
- `charts`: apenas `classesByContent`.
- `meta`: `generatedAt` e `refreshSeconds`.

## Contrato de resposta (payload final)
```json
{
  "period": {
    "year": 2026,
    "month": 3,
    "monthLabel": "Marco/2026",
    "timezone": "America/Sao_Paulo",
    "prev": { "year": 2026, "month": 2 },
    "next": { "year": 2026, "month": 4 }
  },
  "kpis": {
    "activeStudentsInMonth": 42,
    "classesInMonth": 118,
    "revenueInMonth": 15430.5,
    "costInMonth": 8630.0
  },
  "charts": {
    "classesByContent": [
      { "content": "AULA", "count": 56, "percentage": 47.46 },
      { "content": "MENTORIA", "count": 22, "percentage": 18.64 },
      { "content": "TERAPIA", "count": 14, "percentage": 11.86 },
      { "content": "ORIENTACAO_VOCACIONAL", "count": 9, "percentage": 7.63 },
      { "content": "ENEM", "count": 8, "percentage": 6.78 },
      { "content": "PAS", "count": 5, "percentage": 4.24 },
      { "content": "OUTRO", "count": 4, "percentage": 3.39 }
    ]
  },
  "meta": {
    "generatedAt": "2026-03-18T19:42:00Z",
    "refreshSeconds": 60
  }
}
```

## Validacoes de entrada (backend)
- `year`:
  - obrigatorio.
  - faixa `2000..2100`.
- `month`:
  - obrigatorio.
  - faixa `1..12`.
- Requisicao invalida retorna `400 BAD_REQUEST`.

## Comportamento para estado vazio
- Sem eventos no mes:
  - `activeStudentsInMonth = 0`
  - `classesInMonth = 0`
  - `revenueInMonth = 0`
  - `costInMonth = 0`
  - `classesByContent = []`
  - resposta `200 OK`

## Etapas e controle de progresso
Use o status abaixo para travar o fluxo: so avancar para a proxima etapa apos marcar a atual como concluida.

Legenda:
- `[ ]` Pendente
- `[~]` Em progresso
- `[x]` Concluida

### Etapa 1 - Contrato e desenho tecnico
Status: `[x]`

Entregas:
- Definicao do endpoint simplificado por mes.
- Definicao dos campos finais do payload (sem semana).
- Definicao das regras de negocio para KPIs e pizza.

Criterios de conclusao:
- Contrato aprovado sem ambiguidades.
- Regras de calculo e navegacao mensal formalizadas.

### Etapa 2 - Backend de agregacao (repositorio + service)
Status: `[~]`

Entregas:
- Criar/ajustar modulo `dashboard` no backend (`controller`, `service`, `dto`).
- Implementar queries agregadas para KPIs mensais.
- Implementar distribuicao por `EventContent` no mes.
- Ajustar payload para remover qualquer referencia semanal.

Criterios de conclusao:
- Endpoint responde corretamente para meses com e sem dados.
- Valores financeiros retornam com precisao (`BigDecimal`).
- Payload aderente ao contrato simplificado.

### Etapa 3 - Testes backend
Status: `[ ]`

Entregas:
- Testes unitarios do service de dashboard (KPIs mensais e pizza).
- Testes de controller para parametros invalidos e resposta valida.
- Cobrir navegacao mensal (`prev`/`next`).

Criterios de conclusao:
- `./mvnw test` verde para o modulo.
- Cenarios vazios retornam zero sem erro.

### Etapa 4 - Contrato frontend (schema + api client + query keys)
Status: `[ ]`

Entregas:
- Criar schema Zod para o payload simplificado.
- Ajustar `dashboardApi.getSummary(year, month)` em `client/src/services/api.ts`.
- Ajustar query key de dashboard por mes.

Criterios de conclusao:
- Parse de resposta validado por Zod.
- Tipos inferidos usados sem `any`.

### Etapa 5 - UI Dashboard (MVP visual)
Status: `[ ]`

Entregas:
- Topo com navegacao por mes (setas).
- Exibicao de mes/ano atual.
- Quatro cards de KPI mensais.
- Grafico de pizza por `EventContent`.
- Estados de carregamento, erro e vazio.

Criterios de conclusao:
- Navegacao mensal atualiza KPIs e pizza corretamente.
- Tela sem referencias semanais.

### Etapa 6 - Refresh, polimento e responsividade
Status: `[ ]`

Entregas:
- Atualizacao automatica a cada 60s.
- Ajustes de responsividade desktop/mobile.
- Refino visual para leitura operacional rapida.

Criterios de conclusao:
- Tela funcional em diferentes larguras sem quebra.
- Revalidacao automatica sem flicker excessivo.

### Etapa 7 - Validacao final e handoff
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
