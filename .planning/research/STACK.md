# Technology Stack

**Project:** Aprimorar (Gestão Financeira e Tabela Exclusiva do Colaborador)
**Researched:** 2024-05-15

## Recommended Stack

### Core Framework
| Technology | Version | Purpose | Why |
|------------|---------|---------|-----|
| React + TypeScript | 19.2.0 | Base UI | Maintains existing SPA architecture constraints. |
| DaisyUI + Tailwind | 5.5.x | Styling | Reuses existing styling patterns and UI elements (`KpiCard`, `ToggleSwitch`). |

### Supporting Libraries
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| date-fns | ^4.1.0 | Date Manipulation | Required for precise calculation of start/end dates for the "Mensal" KPI boundaries, and tracking exact payment dates. |
| @tanstack/react-query | ^5.90.5 | Data Fetching & Caching | Reuses existing auto-generated Kubb API hooks to fetch and filter the employee's event list without adding separate state management. |

## Alternatives Considered

| Category | Recommended | Alternative | Why Not |
|----------|-------------|-------------|---------|
| Data Table | Native HTML + DaisyUI `<table>` | @tanstack/react-table | Requirements (filter by "pago", search by student name, sort by date) are easily implemented using `useState` and `useMemo`. Introducing a complex table library violates the "brownfield" constraint of using existing patterns and adds unnecessary bundle size. |
| Date Logic | date-fns (explicitly installed) | Native JS `Date` | Calculating safe month boundaries for financial KPIs ("Mensal" vs "Histórico") with native JS dates is error-prone. |
| Currency Formatting | Native `Intl.NumberFormat` | react-currency-format / numeral | The project already uses a configured `brl` formatter in `client/src/lib/utils/formatter.ts`. Do not introduce redundant formatting libraries. |

## Installation

Although `date-fns` is currently used in the codebase, it is a **transient dependency** (inherited from `react-datepicker`). To ensure stability for the new core KPI logic, it must be explicitly installed:

```bash
# Add explicitly as a dependency to prevent transient dependency breakages
cd client && npm install date-fns
```

## Integration Points & What NOT to Add

1.  **Do NOT add Data Grid Libraries (`ag-grid`, `@tanstack/react-table`):** 
    Keep the feature aligned with current standard tables in the app. Filtering and sorting should be handled via native React hooks (`useMemo`) over the data fetched by React Query.
2.  **Do NOT create new generic UI Components:** 
    Integrate with the existing `<KpiCard />` and `<ToggleSwitch />` located in `client/src/components/ui/`.
3.  **Do NOT rely on hidden dependencies:** 
    `date-fns` is currently used in `client/src/features/finance` but is completely missing from `client/package.json`. Add it explicitly to safeguard the KPI logic.
4.  **Local vs Server State:** 
    The KPI aggregations (Total paid, Total to pay) should be derived locally in React from the events list using `reduce()` when toggling between "Mensal" and "Histórico", reducing the need for new complex backend aggregation endpoints for this specific view.

## Sources

- `.planning/PROJECT.md` (Analyzed brownfield constraints and milestone goals)
- `client/package.json` (Audited existing dependencies)
- `client/src/components/ui/` (Found existing components for KPIs)
- `client/src/lib/utils/formatter.ts` (Found native BRL currency formatting)