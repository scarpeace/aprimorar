# Domain Pitfalls

**Domain:** EdTech/Finance (Gestão Financeira e Tabela de Colaborador)
**Researched:** 2024-05-25 (Current Date)

## Critical Pitfalls

Mistakes that cause rewrites or major issues when adding complex tables and granular financial KPIs to existing systems.

### Pitfall 1: Client-Side Performance Jank (Complex Tables)
**What goes wrong:** The browser freezes or lags heavily when filtering or rendering large lists of events (aulas) for a given employee.
**Why it happens:** Attempting to render hundreds of rows without virtualization, performing complex filtering directly on the main thread without debouncing inputs, or failing to memoize table columns and data.
**Consequences:** Unusable UI, frustrating user experience, browser crashes on lower-end devices.
**Prevention:** Implement virtualization (e.g., TanStack Virtual) if row counts grow. Always debounce filter text inputs. Ensure table data and columns definitions are wrapped in `useMemo` to prevent infinite re-renders.
**Detection:** UI lags significantly on keystrokes in search/filter boxes or when toggling the "Pagos" switch.

### Pitfall 2: Disconnected/Inconsistent KPI Logic (Financial Trust)
**What goes wrong:** KPI values on the sidebar (e.g., Total Pago, Total a Pagar) do not match the sum of the filtered rows visible in the table.
**Why it happens:** Re-implementing summation logic in multiple places or calculating KPIs from a different data slice than what the table uses, rather than having a "Single Source of Truth".
**Consequences:** Complete loss of user trust in the system's financial tracking. The school will revert to spreadsheets if the numbers don't add up correctly.
**Prevention:** Maintain a single source of truth. Ideally, calculate KPIs on the backend and return them alongside the table data, or calculate them strictly from the exact memoized dataset that is passed into the table rendering engine. Document the calculation logic clearly.
**Detection:** A manual calculation of visible "A Pagar" rows yields a different number than the "Total a Pagar" KPI card.

## Moderate Pitfalls

### Pitfall 3: State Loss on Navigation or Refresh (Filter State)
**What goes wrong:** Users configure a specific view (e.g., filtering for a specific student, hiding paid events) and then refresh the page or navigate away and back, completely losing their context.
**Prevention:** Sync complex filter states (like the search text and "ocultar pagos" toggle) to the URL query parameters (using `useSearchParams`). This allows users to maintain state across refreshes.

### Pitfall 4: Lack of Context in Financial KPIs
**What goes wrong:** Displaying isolated numbers (e.g., "R$ 1.500") without context, making it hard for the secretary to know if this is good, bad, or complete.
**Prevention:** Always pair KPIs with context. The planned feature correctly addresses this by showing "Total Pago" alongside "Total a Pagar" and allowing toggles between "Mensal" and "Histórico". Avoid vanity metrics; stick to actionable ones.

## Minor Pitfalls

### Pitfall 5: Over-fetching Data for KPI Calculations
**What goes wrong:** Fetching thousands of historical event records strictly to calculate the "Histórico (desde a criação)" KPI on the frontend.
**Prevention:** Ensure the backend provides a dedicated endpoint or includes metadata in the response for aggregate KPIs, rather than forcing the frontend to download and sum massive arrays of data.

## Phase-Specific Warnings

| Phase Topic | Likely Pitfall | Mitigation |
|-------------|---------------|------------|
| Tabela de Eventos Exclusiva | Performance drops on typing in "busca por aluno" | Use debounced inputs for the search bar (e.g., 300ms delay). |
| Integração de Filtros | Desync between the "ocultar pagos" filter and the API response | Clearly separate frontend-only filtering from backend queries, preferably doing pagination/filtering on the backend if datasets are large. |
| Resumo KPI Lateral | Mismatched totals when toggling Mensal vs Histórico | Write unit tests for the summation logic, ensuring the dates boundary matches exactly what the user expects for "Mensal". |
| Registro de Data de Pagamento | Timezone issues causing payments to appear on the wrong day | Standardize timezone handling (e.g., UTC on backend, formatted to local on frontend) when registering exact payment dates. |

## Sources

- [Hubifi: Common mistakes implementing financial KPIs dashboard (HIGH Confidence)](https://hubifi.com) - Validates need for context, single source of truth, and drill-down capabilities.
- [React Performance Patterns for Data Tables (HIGH Confidence)] - Standard React ecosystem knowledge (TanStack Table, Virtualization, Debouncing).
- [Project Context: .planning/PROJECT.md] - Verified specific requirements for "Gestão Financeira e Tabela Exclusiva do Colaborador".