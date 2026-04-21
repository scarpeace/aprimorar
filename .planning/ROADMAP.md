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
- [ ] **Fase 4: Núcleo de Acompanhamento Financeiro** - Introduzir cobranças, pagamentos e visibilidade simples de saldo para o acompanhamento financeiro interno da escola.
- [ ] **Fase 5: Dashboard Diário Unificado** - Combinar indicadores operacionais e financeiros em um snapshot diário confiável.

## Detalhes das Fases

### Fase 1: Autenticação & Acesso Protegido
**Objetivo**: A equipe interna pode acessar o aplicativo de forma segura, enquanto usuários anônimos são mantidos fora dos fluxos de trabalho protegidos.
**Depende de**: Nada (primeira fase)
**Requisitos**: AUTH-01, AUTH-02, AUTH-03, AUTH-04
**Critérios de Sucesso** (o que deve ser VERDADE):
  1. O usuário interno pode fazer login com e-mail ou nome de usuário mais senha e chegar ao app protegido.
  2. O usuário autenticado permanece logado após a atualização do navegador até o logout ou expiração da sessão.
  3. O usuário autenticado pode sair (logout) do app e as páginas protegidas deixam de ser acessíveis imediatamente.
  4. Visitantes não autenticados não podem abrir fluxos de dashboard, aluno, responsável, funcionário, evento ou financeiro diretamente.
**Planos**: 4 planos
Planos:
- [x] 01-01-PLAN.md — Criar autenticação de sessão Spring Security, persistência de usuário interno e rotas de backend protegidas.
- [x] 01-02-PLAN.md — Regenerar contratos de autenticação e construir a infraestrutura de login/bootstrap do frontend.
- [x] 01-03-PLAN.md — Conectar o roteamento protegido e a UX de logout em todo o SPA.
- [x] 01-04-PLAN.md — Restaurar o bootstrap do cliente de autenticação e o comportamento real de atualização/logout baseado em sessão após lacunas de UAT.
**Dica de UI**: sim

### Fase 2: Fortalecimento do Registro de Alunos & Responsáveis
**Objetivo**: A secretária pode gerenciar registros de alunos e partes responsáveis de forma confiável o suficiente para parar de depender de planilhas para o trabalho de registro.
**Depende de**: Fase 1
**Requisitos**: STUD-01, STUD-02, STUD-03, STUD-04, PARN-01, PARN-02, PARN-03, PARN-04
**Critérios de Sucesso** (o que deve ser VERDADE):
  1. A secretária pode criar, visualizar, atualizar e arquivar registros de alunos sem perder referências históricas.
  2. A secretária pode criar, visualizar, atualizar e arquivar registros de pais ou partes responsáveis com os dados de faturamento e contato intactos.
  3. A secretária pode vincular a parte responsável correta a um aluno e ver essa relação preservada consistentemente quando os registros forem visualizados ou editados posteriormente.
  4. A secretária pode encontrar rapidamente o registro do aluno necessário por meio de busca, filtros, paginação e controles de visibilidade de registros arquivados.
**Planos**: 7 planos
Planos:
- [x] 02-01-PLAN.md — Fortalecer as invariantes existentes de backend para um único responsável e os caminhos de busca sem alterar o modelo de relacionamento.
- [x] 02-02-PLAN.md — Atualizar contratos de backend de aluno/responsável, regras de ciclo de vida e clientes de API gerados em torno do fluxo atual de responsável único.
- [x] 02-03-PLAN.md — Refinar fluxos de criação/edição/detalhe/lista de alunos em torno da experiência de registro de responsável único existente.
- [x] 02-04-PLAN.md — Fortalecer telas e terminologia de CRUD/detalhe/arquivamento de responsáveis, preservando a relação de um responsável para muitos alunos.
- [x] 02-05-PLAN.md — Fechar a lacuna semântica de busca por CPF de aluno e alternância de arquivado no fluxo de lista de backend.
- [x] 02-06-PLAN.md — Alinhar a UX de detalhe/exclusão de responsável com as regras de integridade de aluno vinculado apenas ativos.
- [x] 02-07-PLAN.md — Remover os bloqueadores restantes de build do frontend para que a Fase 2 seja operacionalmente entregável.
**Dica de UI**: sim

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
  1. A secretária pode visualizar resumos financeiros básicos para saldo total em aberto, valores recebidos e totais de cobrança.
  2. A secretária pode registrar um pagamento (baixa) contra um evento concluído ou despesa geral.
  3. A secretária pode gerenciar despesas gerais não relacionadas a eventos.
**Planos**: 4 planos
Planos:
- [ ] 04-01-PLAN.md — Estabelecer os modelos de domínio e esquema de banco de dados para rastreamento financeiro (FinancialStatus, GeneralExpense).
- [ ] 04-02-PLAN.md — Implementar a lógica de negócios e endpoints de API para rastreamento financeiro e liquidação de eventos.
- [ ] 04-03-PLAN.md — Implementar a infraestrutura frontend para finanças e o módulo de gerenciamento de Despesas Gerais.
- [ ] 04-04-PLAN.md — Implementar o Dashboard Financeiro e o fluxo de trabalho de Liquidação de Eventos (Baixa).
**Dica de UI**: sim

### Fase 5: Dashboard Diário Unificado
**Objetivo**: A secretária pode revisar o status operacional e financeiro diário da escola a partir de um único snapshot de dashboard.
**Depende de**: Fase 4
**Requisitos**: DASH-01, DASH-02
**Critérios de Sucesso** (o que deve ser VERDADE):
  1. A secretária pode visualizar indicadores do dashboard para alunos e eventos que sejam relevantes para as operações diárias.
  2. A secretária pode visualizar indicadores do dashboard para o status financeiro que sejam relevantes para as decisões diárias.
  3. Após a alteração de registros operacionais ou financeiros, o snapshot do dashboard reflete os indicadores atualizados sem a necessidade de reconciliação de planilhas.
**Planos**: 2 planos
**Dica de UI**: sim

## Progresso

**Ordem de Execução:**
As fases são executadas em ordem numérica: 2 → 2.1 → 2.2 → 3 → 3.1 → 4

| Fase | Planos Concluídos | Status | Concluído |
|-------|----------------|--------|-----------|
| 1. Autenticação & Acesso Protegido | 4/4 | Concluído   | 18-04-2026 |
| 2. Fortalecimento do Registro de Alunos & Responsáveis | 7/7 | Concluído | 20-04-2026 |
| 3. Fortalecimento das Operações de Funcionários & Eventos | 4/4 | Concluído | 21-04-2026 |
| 4. Núcleo de Acompanhamento Financeiro | 0/4 | Não iniciado | - |
| 5. Dashboard Diário Unificado | 0/2 | Não iniciado | - |
