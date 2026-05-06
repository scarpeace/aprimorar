## Why

The `EmployeeDetailPage` currently uses an older, less structured layout compared to the recently updated `StudentDetailsPage`. To ensure consistency across the application and improve the user experience for administrative staff, we need to refactor the employee details view to match the visual hierarchy, animations, and sectioning of the student view. This will make the interface more predictable and easier to navigate.

## What Changes

- **Layout Structure**: Wrap the main content in a `max-w-7xl mx-auto` container to match the student details page.
- **Sectioning & Animations**: Split the page into distinct sections (Info, KPIs & Filters, Events Table) and apply staggered `fade-up` animations to each section independently.
- **Filters UI**: Encapsulate the date filters and KPIs within a styled "Indicadores e Filtros" card (`bg-base-100 p-4 rounded-xl border border-base-300 shadow-sm`), matching the new pattern.
- **Component Renaming**: The page itself will be renamed from `EmployeeDetailPage` to `EmployeeDetailsPage` (plural 'Details') to match `StudentDetailsPage` if desired, though we will keep the component name as is if it causes too much routing churn, but we will focus on internal UI refactoring.

## Capabilities

### New Capabilities

- `employee-details-open-layout`: Refactored layout structure with distinct "Indicadores e Filtros" section and staggered animations for employee details.

### Modified Capabilities

- 

## Impact

- **Frontend**: `client/src/features/employees/pages/EmployeeDetailPage.tsx`
- No backend changes required.