# Architecture Patterns: Gestão Financeira e Tabela Exclusiva do Colaborador

**Domain:** Gestão Escolar (Financial & Operational)
**Researched:** 2026-04-19

## Recommended Architecture

A arquitetura estenderá os padrões existentes do SPA React + Spring Boot monorepo. As novas funcionalidades se integrarão de forma orgânica aos limites de contexto de `Employee` e `Event`.

### Component Boundaries (Novos e Modificados)

| Component | Responsibility | Communicates With |
|-----------|---------------|-------------------|
| `EventEntity` & Banco de Dados | Armazenar o status financeiro do evento (campos como `paymentDate`, `isPaid` e, se ausente, valor). | Banco de dados PostgreSQL |
| `EventRepository` & `Specifications` | Filtrar eventos por `employeeId`, status de pagamento, e busca textual por aluno. Calcular agregações de KPI usando SQL puro/JPQL. | Banco de dados PostgreSQL |
| `EmployeeController` | Expor endpoints agregados para os detalhes do colaborador. Novos endpoints sugeridos: `GET /v1/employees/{id}/events` e `GET /v1/employees/{id}/financial-kpis`. | `EventService`, Cliente Frontend (via Kubb) |
| `EventController` | Novo endpoint para registrar o pagamento de forma atômica: `PATCH /v1/events/{id}/pay`. | `EventService`, Cliente Frontend (via Kubb) |
| `EmployeeDetailPage.tsx` | Layout principal atualizado para orquestrar a exibição da tabela e painel financeiro, contendo os provedores de estado compartilhados, se necessário. | Hooks gerados pelo Kubb, Componentes UI da feature |
| `EmployeeEventsTable.tsx` | (Novo) Tabela exclusiva do colaborador com paginação, busca por aluno, filtro "ocultar pagos" e ordenação cronológica decrescente. | `EmployeeDetailPage`, Componentes UI genéricos |
| `EmployeeFinancialKpi.tsx` | (Novo) Painel lateral exibindo as estatísticas: "Total Pago", "Total a Pagar" e "Total de Eventos". | `EmployeeDetailPage`, Componentes UI genéricos |

### Data Flow

**Fluxo de Leitura (Carregamento da Página do Colaborador):**
1. O usuário acessa a página de detalhes de um funcionário (`EmployeeDetailPage.tsx`).
2. O React dispara duas consultas (queries) paralelas via hooks gerados pelo Kubb:
   - `useGetEmployeeEvents(id, { page, searchStudent, hidePaid })`: Busca a lista paginada de eventos atrelados ao funcionário.
   - `useGetEmployeeFinancialKpis(id, { timeframe: 'monthly' | 'historical' })`: Busca os totais agregados (KPIs).
3. As requisições chegam ao `EmployeeController` no backend, que delega para as classes de `Service` e `Repository` de Eventos para extrair e agregar os dados requeridos usando paginação (`PageDTO`) e views consolidadas.
4. Os dados retornam ao frontend e renderizam os componentes `EmployeeEventsTable` e `EmployeeFinancialKpi`.

**Fluxo de Escrita (Marcação de Pagamento):**
1. Na `EmployeeEventsTable`, o usuário localiza uma aula pendente e clica na ação "Marcar como Pago".
2. Um modal abre para confirmação e seleção da data exata do pagamento (padrão assumido: data de hoje).
3. O formulário é validado (Zod) e uma mutação é disparada via hook (`usePatchEventPayment`).
4. O `EventController` recebe a requisição com o body de atualização; o `EventService` valida as regras (ex: evento não pode ser pago em data futura absurda) e marca a coluna do evento.
5. Em caso de sucesso, o frontend invalida as consultas locais (invalidateQueries de `employeeEvents` e `employeeFinancialKpis`) para forçar o recarregamento da tabela e dos números financeiros instantaneamente.

## Suggested Build Order

1. **Database & Entities (Backend):** Adicionar/Verificar os campos necessários em `Event` (como `paymentDate` e controle financeiro) via migrations Flyway.
2. **Repositories & Specifications (Backend):** Criar os métodos de agrupamento de KPIs por colaborador e estender o Specification de eventos com os filtros desejados (colaborador, status de pagamento, aluno associado).
3. **Services & Controllers (Backend):** Implementar as regras de negócio para os novos endpoints (`PATCH` de pagamento, e os `GET`s paginados/consolidados vinculados ao `Employee`).
4. **OpenAPI & Codegen:** Iniciar localmente o Spring Boot, gerar a documentação Swagger atualizada e rodar a integração via Kubb (frontend npm script) para sincronizar tipagens e mutações.
5. **UI Components (Frontend):** Construir isoladamente os componentes `EmployeeEventsTable` (reusando as UIs de tabela existentes) e `EmployeeFinancialKpi`.
6. **Integration (Frontend):** Injetar os novos componentes na `EmployeeDetailPage`, conectar os hooks da API às UIs e plugar o estado React para controlar abas, buscas e os alternadores de tempo de KPIs.

## Scalability Considerations

| Concern | At 100 users | At 10K users | At 1M users |
|---------|--------------|--------------|-------------|
| Consultas de KPI Financeiro | Consultas diretas (`SUM`) no banco por requisição | Adicionar índices compostos em `employee_id`, `event_date` e `is_paid` na tabela referencial | Usar Materialized Views para agregações mensais históricas |
| Atualização de Dashboard do UI | Invalidação global simples após um pagamento | Invalidação estrita do cache apenas para a query do próprio funcionário | Atualizações pessimistas da UI (Optimistic Updates) antes do retorno da API |

## Sources
- Arquitetura Existente do Monorepo (.planning/codebase/ARCHITECTURE.md)
- Contexto de Projeto (.planning/PROJECT.md)