## Context

The current `EmployeeDetailPage` has an outdated layout structure that does not align with the recent redesign applied to the `StudentDetailsPage`. The student details page introduced a clean `max-w-7xl mx-auto` container, a dedicated styled section for filters and KPIs, and staggered entry animations for different functional blocks. To reduce cognitive load for administrative users and simplify future UI maintenance, we need to adapt the employee details page to this new standard pattern.

## Goals / Non-Goals

**Goals:**
- Unify the visual hierarchy of the employee details page with the student details page.
- Implement a `max-w-7xl mx-auto` centered layout container.
- Create a dedicated "Indicadores e Filtros" card that houses both the date filters and the `EmployeeKPIs` component.
- Apply staggered `fade-up` animations to the distinct sections (Info, Filters/KPIs, Events Table).

**Non-Goals:**
- Modifying backend APIs or data structures.
- Altering the core business logic of data fetching, KPI calculation, or event table rendering.
- Renaming the file or component to `EmployeeDetailsPage` (plural), to avoid unnecessary routing and import churn; the focus is strictly on internal UI refactoring.

## Decisions

- **Decision 1: Layout Encapsulation:** Wrap the entire main content inside a `<div className="flex flex-col gap-6 max-w-7xl mx-auto">` to mirror the student page.
- **Decision 2: Filter and KPI Grouping:** Create a new wrapper `div` with styling `bg-base-100 p-4 rounded-xl border border-base-300 shadow-sm` to house the `DateRangeInput`, clear filters button, and the title "Indicadores e Filtros". The `EmployeeKPIs` component will sit directly below this toolbar within a shared animation wrapper.
- **Decision 3: Animation Strategy:** Use the existing Tailwind animation classes (`animate-[fade-up_...ms_ease-out_both]`) with different delays for the Info section, KPI/Filter section, and Events Table to create a smooth, staggered loading effect identical to the student page.

## Risks / Trade-offs

- **[Risk] CSS Class Typos:** Incorrect Tailwind classes could lead to broken layouts or missing animations. → **Mitigation:** Directly copy the structural container classes and animation timing values from `StudentDetailsPage.tsx`.
- **[Risk] Z-Index Issues with DatePicker:** The date picker dropdown might be hidden behind other elements due to new container stacking contexts. → **Mitigation:** Apply `z-30` or appropriate z-index to the filter container, as done in the student page.