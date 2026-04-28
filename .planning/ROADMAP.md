# Roadmap: v1.1 Gestão Financeira e Detalhes do Colaborador

## Phases

- [ ] **Phase 01: Employee Events Tracking** - Users can view and search an employee's events on their detail page
- [ ] **Phase 02: Event Payment Workflow** - Users can process payments for events and track pending work
- [ ] **Phase 03: Financial KPIs Dashboard** - Users can instantly see the financial summary for an employee

## Phase Details

### Phase 01: Employee Events Tracking
**Goal**: Users can view and search an employee's events on their detail page
**Depends on**: None
**Requirements**: TAB-01, TAB-02
**Success Criteria** (what must be TRUE):
  1. User navigating to an employee's detail page sees a list of only that employee's events.
  2. Events are automatically ordered with the most recent first.
  3. User can type a student's name in a search bar to instantly filter the events list.
**Plans**: 2 plans
- [ ] 01-01-PLAN.md — Backend API & Hook synchronization
- [ ] 01-02-PLAN.md — Frontend Search UI & Pagination logic
**UI hint**: yes

### Phase 02: Event Payment Workflow
**Goal**: Users can process payments for events and track pending work
**Depends on**: Phase 01
**Requirements**: TAB-03, TAB-04
**Success Criteria** (what must be TRUE):
  1. User can mark an event as paid and register the exact date of payment.
  2. User can turn on a "Hide Paid" toggle to clear paid events from the list view.
  3. Filtering functionality (search and toggle) works together consistently.
**Plans**: TBD
**UI hint**: yes

### Phase 03: Financial KPIs Dashboard
**Goal**: Users can instantly see the financial summary for an employee
**Depends on**: Phase 02
**Requirements**: KPI-01, KPI-02
**Success Criteria** (what must be TRUE):
  1. User sees a summary panel showing "Total pago", "Total a pagar", and "Total de eventos".
  2. Marking an event as paid in the table automatically updates the KPI values without a page reload.
  3. User can toggle the KPI panel between "Mensal" (current month) and "Histórico" (all time) summaries.
**Plans**: TBD
**UI hint**: yes

## Progress

| Phase | Plans Complete | Status | Completed |
|-------|----------------|--------|-----------|
| 01. Employee Events Tracking | 0/2 | Not started | - |
| 02. Event Payment Workflow | 0/0 | Not started | - |
| 03. Financial KPIs Dashboard | 0/0 | Not started | - |
