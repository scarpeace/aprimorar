## Context

The current `EmployeeDetailPage` implementation uses a blocking loading guard at the top level: `if (employeeQuery.isPending || !employeeQuery.data) return <LoadingCard />`. This prevents the `PageLayout`, `EmployeeKPIs`, and `EmployeeEventsTable` from mounting until the employee's basic profile is fetched. Since these sections are independent in the backend, they can and should be loaded in parallel. This design mirrors the "Parallel Shell" pattern recently applied to the students feature.

## Goals / Non-Goals

**Goals:**
- Improve perceived performance by rendering the page shell immediately.
- Enable parallel API requests for employee profile, summary, and events.
- Modularize the page by extracting logical sub-sections into components.
- Ensure stable date references for child components.

**Non-Goals:**
- Refactoring the internal logic of `EmployeeForm` or `EmployeeKPIs`.
- Changing any backend API endpoints.
- Modifying the styles or layout of the page significantly.

## Decisions

### 1. Decentralized Loading State
Instead of a single blocking guard in `EmployeeDetailPage`, the loading and error states will be handled within the specific sections (`EmployeeInfoSection`, `EmployeeKPIs`, `EmployeeEventsTable`).
- **Rationale**: This allows the "shell" (Header, Date Filters) to render instantly and each section to pop in as its specific data arrives.

### 2. Request Deduplication Strategy
The `useGetEmployeeById` query will be called in both `EmployeeDetailPage` (for the Edit Modal) and `EmployeeInfoSection` (for the profile display).
- **Rationale**: TanStack Query automatically deduplicates these requests, so no extra network call is made, while keeping components decoupled.

### 3. Custom Filter Hook
Extract URL state logic into a `useEmployeeDateFilters` hook.
- **Rationale**: Improves readability of the main page and makes the logic reusable. It will return memoized `startDate` and `endDate` objects.

## Risks / Trade-offs

- **[Risk] Layout Shift** → **Mitigation**: Use fixed-height skeleton loaders in `EmployeeInfoSection` to match the typical size of the data cards, minimizing layout jumps as data arrives.
- **[Risk] State Synchronization** → **Mitigation**: Rely on the URL as the single source of truth for date filters, ensuring all components reacting to `useSearchParams` stay in sync.
