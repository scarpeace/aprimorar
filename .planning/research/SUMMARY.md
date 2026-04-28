# Research Summary

## Executive Summary

The Aprimorar project requires a dedicated financial management feature for employees (teachers), primarily aimed at the school secretary. This feature centers on a specialized data table of events tied to a specific employee and an accompanying sidebar for financial KPIs (Total Paid vs. Total to Pay). The core objective is to provide an audit-friendly, clear operational view without introducing the complexities of automated gateways or self-service portals. 

The recommended approach embraces the existing brownfield constraints by leveraging the current React 19 + TypeScript + Spring Boot monorepo. Instead of adopting heavy data-grid libraries, the solution relies on native HTML/DaisyUI tables with robust frontend state management (debounced filtering, URL synchronization) and backend-driven aggregations. This ensures minimal bundle impact while meeting all user requirements.

The most significant risk lies in data inconsistency between the KPI sidebar and the filtered table data, which can severely damage user trust. Mitigating this requires a single source of truth for calculations—ideally pushing KPI aggregation to the backend—and ensuring explicit payment date tracking rather than simple boolean toggles.

## Key Findings

### Stack Recommendations
* **Core:** React 19.2 + TypeScript + DaisyUI/Tailwind. Aligns seamlessly with the current SPA architecture.
* **Libraries:** Explicit installation of `date-fns` for precise "Mensal" vs "Histórico" KPI boundaries. Use of `@tanstack/react-query` with existing Kubb auto-generated hooks.
* **Avoid:** Heavy grid libraries like `@tanstack/react-table` or new currency formatters; use native React hooks, `<table>`, and the existing `brl` formatter instead.

### Feature Landscape
* **Table Stakes:** Filtered event table per employee, student search, "hide paid" toggle, explicit payment date tracking, and monthly/historical KPI summaries.
* **Differentiators:** Monthly vs. All-Time KPI toggling, bulk payment actions, and PDF receipts (V2).
* **Anti-features:** Teacher self-service portals, automated split payments/gateways, and complex tiered commission logic are explicitly out of scope to protect the MVP timeline.

### Architectural Patterns
* **Backend Additions:** Add `paymentDate` and financial control to `EventEntity`. Introduce aggregated endpoints `GET /v1/employees/{id}/financial-kpis` and a transactional `PATCH /v1/events/{id}/pay`.
* **Frontend Flow:** `EmployeeDetailPage.tsx` orchestrates parallel Kubb queries for events and KPIs, passing data to the isolated `EmployeeEventsTable.tsx` and `EmployeeFinancialKpi.tsx` components.
* **State Management:** Invalidate both local queries upon a successful payment mutation to ensure the dashboard instantly reflects the updated financial state.

### Critical Pitfalls
* **Disconnected KPI Logic:** Showing KPI totals that don't match the table data destroys trust. Must enforce a single source of truth via robust backend calculations.
* **Performance Jank:** Complex local filtering without debouncing will freeze the UI. Debounce text inputs and handle complex pagination/aggregation server-side if datasets scale.
* **State Loss:** Losing filter states (like "hide paid" or search query) on page refresh. Must sync these view states to URL query parameters.

## Implications for Roadmap

### Suggested Phases

1. **Phase 1: Database & Core Backend** — *Foundation for financial tracking.*
   * **Rationale:** Data structures and base repositories must exist before any frontend logic.
   * **Delivers:** `EventEntity` updates (`paymentDate`, `isPaid`), migrations, and KPI aggregation queries using SQL/JPQL.
   * **Pitfall to Avoid:** Over-fetching massive datasets later; ensure the DB schema supports efficient querying.

2. **Phase 2: API Endpoints & Codegen** — *Exposing data safely.*
   * **Rationale:** Provides the exact data payload required by the frontend.
   * **Delivers:** `GET /events`, `GET /financial-kpis`, and `PATCH /pay` endpoints in Spring Boot. Execution of Kubb to sync TypeScript interfaces.
   * **Pitfall to Avoid:** Avoid mismatched KPI totals by centralizing calculation logic in the Service/Repository layer.

3. **Phase 3: Frontend UI Components** — *Building isolated visual blocks.*
   * **Rationale:** Speeds up development by avoiding premature wiring and focusing on pure UI mapping.
   * **Delivers:** `EmployeeEventsTable` (native HTML/DaisyUI) and `EmployeeFinancialKpi` sidebar.
   * **Pitfall to Avoid:** DO NOT introduce heavy data table libraries. Use native `<table>`.

4. **Phase 4: State Wiring & Validation** — *Bringing the feature to life.*
   * **Rationale:** Connects the UI to real API hooks and manages user interactions.
   * **Delivers:** Integration inside `EmployeeDetailPage`, Zod validation for exact payment dates, and URL state syncing for filters.
   * **Pitfall to Avoid:** Filter state loss on refresh (use URL sync) and UI jank (debounce search).

### Research Flags
* **Needs Research:** Bulk Payments (if prioritized for Phase 4, requires backend concurrency validation).
* **Standard Patterns:** Phase 1 and 3 are standard CRUD and UI mapping.

## Confidence Assessment

| Area | Confidence | Notes |
|------|------------|-------|
| Stack | HIGH | Strictly follows existing monorepo brownfield constraints. |
| Features | HIGH | Clear alignment with administrative operational needs. |
| Architecture | HIGH | Follows established Spring Boot + React Query patterns. |
| Pitfalls | HIGH | Common React and EdTech issues correctly identified. |

**Gaps:** The exact behavior of "Mensal" vs "Histórico" date boundaries (e.g., closing day of the month vs. 1st to 30th/31st) needs definition during requirements gathering to ensure the KPI logic is accurate.

## Sources
* `.planning/PROJECT.md`
* `.planning/codebase/ARCHITECTURE.md`
* `client/package.json`
* Domain standards (Hubifi, EdTech platforms)
* React Performance Best Practices