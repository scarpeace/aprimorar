# Requisitos: Aprimorar

**Definido:** 17-04-2026
**Valor Central:** A secretária deve ser capaz de gerenciar o dia a dia da escola a partir do app, sem depender de planilhas espalhadas.

## Requisitos v1

Requisitos para o lançamento inicial focado em secretária/administrador. Módulos brownfield existentes contam apenas se satisfizerem esses comportamentos de forma confiável o suficiente para substituir o uso de planilhas.

### Autenticação

- [x] **AUTH-01**: O usuário interno pode entrar com e-mail ou nome de usuário e senha
- [x] **AUTH-02**: A sessão do usuário autenticado persiste após a atualização do navegador até o logout ou expiração da sessão
- [x] **AUTH-03**: O usuário autenticado pode sair (logout) de forma segura do app
- [x] **AUTH-04**: Usuários não autenticados não podem acessar fluxos protegidos de alunos, responsáveis, funcionários, eventos, financeiro ou dashboard

### Alunos

- [x] **STUD-01**: A secretária pode criar um registro de aluno com todos os dados obrigatórios de matrícula
- [x] **STUD-02**: A secretária pode visualizar e atualizar um registro de aluno existente
- [x] **STUD-03**: A secretária pode arquivar um registro de aluno sem perder referências históricas
- [x] **STUD-04**: A secretária pode encontrar alunos por meio de busca, filtros, paginação e controles de visibilidade de arquivos

### Responsáveis

- [x] **PARN-01**: A secretária pode criar um registro de pai ou parte responsável com dados de faturamento e contato
- [x] **PARN-02**: A secretária pode visualizar e atualizar um registro existente de pai ou parte responsável
- [x] **PARN-03**: A secretária pode arquivar um registro de pai ou parte responsável sem quebrar registros vinculados
- [x] **PARN-04**: A secretária pode vincular a parte responsável correta a um aluno e preservar essa relação de forma consistente

### Funcionários

- [x] **EMPL-01**: A secretária pode criar um registro de funcionário com os dados operacionais necessários pela escola
- [x] **EMPL-02**: A secretária pode visualizar e atualizar um registro de funcionário existente
- [x] **EMPL-03**: A secretária pode arquivar um registro de funcionário sem quebrar registros operacionais vinculados
- [x] **EMPL-04**: A secretária pode encontrar funcionários por meio de busca, filtros, paginação e controles de visibilidade de arquivos

### Eventos

- [x] **EVNT-01**: A secretária pode criar um registro de evento para aulas, mentorias ou outras atividades escolares
- [x] **EVNT-02**: A secretária pode visualizar e atualizar um registro de evento existente
- [x] **EVNT-03**: A secretária pode arquivar um registro de evento sem perder a visibilidade histórica
- [x] **EVNT-04**: A secretária não pode salvar um evento em um estado que viole as regras de negócio de agendamento ou propriedade
- [x] **EVNT-05**: A secretária pode visualizar os próximos eventos em telas operacionais

### Financeiro

- [x] **FIN-01**: A secretária pode criar um registro de cobrança vinculado ao aluno ou parte responsável correta (via Atendimento)
- [x] **FIN-02**: A secretária pode registrar um pagamento com valor e data de pagamento contra uma cobrança existente (Baixa de Atendimento)
- [x] **FIN-03**: A secretária pode visualizar resumos financeiros básicos para o saldo total em aberto, valores recebidos e totais de cobranças (Dashboard Financeiro)

### Dashboard

- [ ] **DASH-01**: A secretária pode visualizar um instantâneo (snapshot) do dashboard com indicadores de alunos e eventos relevantes para as operações diárias
- [ ] **DASH-02**: A secretária pode visualizar um instantâneo do dashboard com indicadores financeiros relevantes para as decisões diárias

## Requisitos v2

...

## Rastreabilidade

Quais fases cobrem quais requisitos. Atualizado durante a criação do roadmap.

| Requisito | Fase | Status |
|-------------|-------|--------|
| AUTH-01 | Fase 1 | Completo |
| AUTH-02 | Phase 1 | Completo |
| AUTH-03 | Phase 1 | Completo |
| AUTH-04 | Phase 1 | Completo |
| STUD-01 | Fase 2 | Completo |
| STUD-02 | Fase 2 | Completo |
| STUD-03 | Fase 2 | Completo |
| STUD-04 | Fase 2 | Completo |
| PARN-01 | Fase 2 | Completo |
| PARN-02 | Fase 2 | Completo |
| PARN-03 | Fase 2 | Completo |
| PARN-04 | Fase 2 | Completo |
| EMPL-01 | Fase 3 | Completo |
| EMPL-02 | Fase 3 | Completo |
| EMPL-03 | Fase 3 | Completo |
| EMPL-04 | Fase 3 | Completo |
| EVNT-01 | Fase 3 | Completo |
| EVNT-02 | Fase 3 | Completo |
| EVNT-03 | Fase 3 | Completo |
| EVNT-04 | Fase 3 | Completo |
| EVNT-05 | Fase 3 | Completo |
| FIN-01 | Fase 4 | Completo |
| FIN-02 | Fase 4 | Completo |
| FIN-03 | Fase 4 | Completo |
| DASH-01 | Fase 5 | Pendente |
| DASH-02 | Fase 5 | Pendente |

**Cobertura:**
- requisitos v1: 26 total
- Mapeados para fases: 26
- Não mapeados: 0 ✓

---
*Requisitos definidos: 17-04-2026*
*Última atualização: 21-04-2026 após conclusão da Fase 4*
