---
gsd_state_version: 1.0
milestone: v1.1
milestone_name: Gestão Financeira e Detalhes do Colaborador
status: defining_requirements
stopped_at: 
last_updated: "2026-04-28T00:00:00.000Z"
last_activity: 28-04-2026
progress:
  total_phases: 0
  completed_phases: 0
  total_plans: 0
  completed_plans: 0
  percent: 0
---

# Estado do Projeto

## Referência do Projeto

Veja: .planning/PROJECT.md

**Valor central:** A secretária deve ser capaz de gerenciar o dia a dia da escola a partir do app, sem depender de planilhas espalhadas.
**Foco atual:** Levantamento de requisitos para a v1.1.

## Posição Atual

Fase: Não iniciada (definindo requisitos)
Plano: —
Status: Definindo requisitos
Última atividade: 28-04-2026 — Milestone v1.1 iniciado

Progresso: [          ] 0%

## Contexto Acumulado

### Decisões

As decisões são registradas na tabela de Decisões Chave do PROJECT.md.
Decisões recentes que afetam o trabalho atual:

- [Fase 04]: O financeiro é baseado em Atendimentos (Eventos) e Despesas Gerais manuais.
- [Fase 04]: Cada evento possui `incomeStatus` e `expenseStatus` independentes.
- [Fase 04]: Somente eventos com status `COMPLETED` podem ter baixa financeira (PAID).
- [Fase 04]: Despesas Gerais não possuem arquivamento (exclusão física permitida ou simplificada).
- [Fase 04]: Uso rigoroso de `BigDecimal` (Java) e `NUMERIC(19,2)` (SQL) para precisão monetária.

### Pendências (Todos)

Nenhuma por enquanto.

### Bloqueadores/Preocupações

- Nenhuma identificada na fase atual.

## Itens Adiados

Itens reconhecidos e levados adiante do encerramento do marco anterior:

| Categoria | Item | Status | Adiado em |
|----------|------|--------|-------------|
| Expansão de Acesso | Portais de autoatendimento para professores, pais e alunos | Adiado | planejamento v1 |
| Expansão Financeira | Integração com gateway de pagamento e políticas de faturamento avançadas | Adiado | planejamento v1 |
| Relatórios | Relatórios financeiros avançados e análises por período longo | Adiado | planejamento v1 |
