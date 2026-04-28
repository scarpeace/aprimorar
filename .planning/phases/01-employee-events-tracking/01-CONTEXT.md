# Phase 01: Employee Events Tracking - Context

**Gathered:** 2026-04-28
**Status:** Ready for planning

<domain>
## Phase Boundary

This phase delivers the ability to view, sort (by date descending), and search an employee's events (by student name) directly on the employee's detail page. It strictly focuses on visualization and filtering, without handling payment flows or overall KPIs.
</domain>

<decisions>
## Implementation Decisions

### Search Scope
- **D-01:** The search bar should ONLY match the student name, strictly adhering to requirement TAB-02. It does not need to search event content or types.

### Search Behavior
- **D-02:** The search should be auto-debounced (filtering as you type) rather than requiring a manual "submit" or "enter" press. This is consistent with other pages (e.g., `EmployeesPage`).

### Search Bar Placement
- **D-03:** The search bar should be placed above the events table as a standalone component, making it prominent.

### Empty States
- **D-04:** When a search yields no results, display a simple text message inside the table body (e.g., "Nenhum atendimento encontrado") to keep the table headers visible, rather than using a large standalone `EmptyCard` illustration.

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Project Plans & State
- `.planning/PROJECT.md` — Project context and architecture guidelines
- `.planning/REQUIREMENTS.md` — Requirement specifications (TAB-01, TAB-02)
- `.planning/ROADMAP.md` — Overall project phases and goals
- `.planning/STATE.md` — Current execution state

### Related UI Source Code
- `client/src/features/employees/pages/EmployeeDetailPage.tsx` — Target page where the events table currently lives
- `client/src/features/events/components/EventsTable.tsx` — The events table component being modified
- `client/src/components/ui/list-search-input.tsx` — Reusable search input component to use

</canonical_refs>

<code_context>
## Existing Code Insights

### Reusable Assets
- `ListSearchInput`: The existing `ListSearchInput` from `client/src/components/ui/list-search-input.tsx` should be reused for the search bar.
- `useDebounce`: Use `client/src/lib/shared/use-debounce.ts` to implement the auto-search behavior.
- `EventsTable`: Already integrated into `EmployeeDetailPage.tsx`. Requires extension to accept search props and to display empty states.

### Established Patterns
- **Pagination and Searching:** Following patterns from `EmployeesPage.tsx`, the search term is tracked in state, debounced, and passed as a parameter to the query hook (e.g., `useGetEventsByEmployeeId`).

### Integration Points
- **API Call Updates:** The backend API and the generated Kubb hook (`useGetEventsByEmployeeId`) might need to support a `search` or `studentName` query parameter if it does not already.

</code_context>

<specifics>
## Specific Ideas

No specific aesthetic requirements — open to standard approaches using existing UI components like `ListSearchInput`.

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope.
</deferred>

---

*Phase: 01-employee-events-tracking*
*Context gathered: 2026-04-28*