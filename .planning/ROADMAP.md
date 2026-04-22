# Roadmap: Aprimorar

## Visão Geral

Este roadmap transforma o atual monorepo brownfield de gestão escolar em um sistema operacional focado na secretária que pode substituir o trabalho diário baseado em planilhas. Ele começa garantindo a segurança do acesso, depois fortalece os módulos existentes de alunos, responsáveis, funcionários e eventos, adiciona o financeiro como o principal domínio novo e finaliza com um dashboard que combina visibilidade operacional e financeira.

## Fases

**Numeração das Fases:**
- Fases inteiras (1, 2, 3): Trabalho de marco (milestone) planejado
- Fases decimais (2.1, 2.2): Inserções urgentes (marcadas com INSERIDO)

Fases decimais aparecem entre os inteiros circundantes em ordem numérica.

- [x] **Fase 1: Autenticação & Acesso Protegido** - Adicionar login seguro e proteger todos os fluxos de trabalho da secretária/admin. (concluído 18-04-2026)
- [x] **Fase 2: Fortalecimento do Registro de Alunos & Responsáveis** - Tornar os fluxos de registro existentes confiáveis o suficiente para substituir as planilhas de alunos/partes responsáveis. (concluído 20-04-2026)
- [x] **Fase 3: Fortalecimento das Operações de Funcionários & Eventos** - Refinar os fluxos de funcionários e eventos para operações de agendamento diárias confiáveis. (concluído 21-04-2026)
- [x] **Fase 4: Núcleo de Acompanhamento Financeiro** - Introduzir cobranças, pagamentos e visibilidade simples de saldo para o acompanhamento financeiro interno da escola. (concluído 21-04-2026)
- [ ] **Fase 5: Dashboard Diário Unificado** - Combinar indicadores operacionais e financeiros em um snapshot diário confiável.

## Detalhes das Fases

...

### Fase 3: Fortalecimento das Operações de Funcionários & Eventos
**Objetivo**: A secretária pode gerenciar funcionários e atividades escolares no app com aplicação confiável de regras de negócio.
**Depende de**: Fase 2
**Requisitos**: EMPL-01, EMPL-02, EMPL-03, EMPL-04, EVNT-01, EVNT-02, EVNT-03, EVNT-04, EVNT-05
**Critérios de Sucesso** (o que deve ser VERDADE):
  1. A secretária pode criar, visualizar, atualizar, arquivar e encontrar registros de funcionários por meio de busca, filtros, paginação e controles de arquivamento.
  2. A secretária pode criar, visualizar, atualizar e arquivar registros de eventos para aulas, mentorias e outras atividades escolares mantendo a visibilidade histórica.
  3. A secretária não pode salvar um evento que quebre as regras de negócio de agendamento ou propriedade e recebe feedback claro em vez disso.
  4. A secretária pode abrir telas operacionais que mostram os próximos eventos necessários para o planejamento diário.
**Planos**: 4 planos
Planos:
- [x] 03-01-PLAN.md — Estabelecer a infraestrutura de status de eventos, descrições de funções e integridade de conflitos no backend.
- [x] 03-02-PLAN.md — Expor filtros operacionais e status via API e sincronizar contratos com o frontend (Kubb).
- [x] 03-03-PLAN.md — Fortalecer a interface de funcionários com descrições amigáveis dos papéis em português.
- [x] 03-04-PLAN.md — Adicionar badges de status, filtros operacionais rápidos e gerenciamento de ciclo de vida na interface de eventos.
**Dica de UI**: sim

### Fase 4: Núcleo de Acompanhamento Financeiro
**Objetivo**: A secretária pode acompanhar cobranças, pagamentos e saldos dentro do app em vez de planilhas financeiras informais.
**Depende de**: Fase 3
**Requisitos**: FIN-01, FIN-02, FIN-03
**Critérios de Sucesso** (o que deve ser VERDADE):
  1. A secretária pode criar uma cobrança vinculada ao aluno ou parte responsável correta (via Atendimento).
  2. A secretária pode registrar um pagamento com valor e data de pagamento contra uma cobrança existente e ver o saldo restante ser atualizado corretamente (Baixa de Atendimento).
  3. A secretária pode visualizar resumos financeiros básicos para saldo total em aberto, valores recebidos e totais de cobrança (Painel Financeiro).
**Planos**: 4 planos
Planos:
- [x] 04-01-PLAN.md — Estabelecer enums financeiros, tb_general_expenses e atualizar entidade Event.
- [x] 04-02-PLAN.md — Implementar CRUD de despesas gerais, API de resumo financeiro e lógica de baixa de eventos.
- [x] 04-03-PLAN.md — Adicionar menu financeiro e implementar página de gestão de despesas gerais.
- [x] 04-04-PLAN.md — Implementar Dashboard Financeiro e visão de Baixa de Atendimentos com filtros.
**Dica de UI**: sim

...

## Progresso

**Ordem de Execução:**
As fases são executadas em ordem numérica: 2 → 2.1 → 2.2 → 3 → 3.1 → 4

| Fase | Planos Concluídos | Status | Concluído |
|-------|----------------|--------|-----------|
| 1. Autenticação & Acesso Protegido | 4/4 | Concluído   | 18-04-2026 |
| 2. Fortalecimento do Registro de Alunos & Responsáveis | 7/7 | Concluído | 20-04-2026 |
| 3. Fortalecimento das Operações de Funcionários & Eventos | 4/4 | Concluído | 21-04-2026 |
| 4. Núcleo de Acompanhamento Financeiro | 4/4 | Concluído | 21-04-2026 |
| 5. Dashboard Diário Unificado | 0/2 | Não iniciado | - |
