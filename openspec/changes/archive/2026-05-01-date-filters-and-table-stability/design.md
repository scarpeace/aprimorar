## Context

The `EventsPage` currently manages events but lacks granular date filtering. Users can search by text, but cannot restrict results to specific date ranges (e.g., "all events last week"). The sorting is also non-deterministic, causing rows to move when their `updatedAt` field changes during a status toggle.

## Goals / Non-Goals

**Goals:**
- Implement `startDate` and `endDate` filtering in the frontend using `useSearchParams`.
- Fix the backend specification to correctly handle "paid" status filtering.
- Stabilize row ordering by adding a tie-breaker to all event queries.

**Non-Goals:**
- Modifying the event data model.
- Implementing recurring events.
- Adding complex multi-select filters.

## Decisions

### 1. Date Range Handling in Frontend
- **Decision**: Use `react-datepicker` with ISO-8601 strings in `useSearchParams`.
- **Rationale**: ISO strings are URL-safe and easily parsed by the backend `Instant` type. `useSearchParams` ensures filters are bookmarkable and survive page refreshes.
- **Implementation**: `EventsPage` will read `startDate` and `endDate` from the URL. If missing, it will calculate the current month's bounds.

### 2. Deterministic Sorting
- **Decision**: Append `id,asc` as a secondary sort parameter to all event API calls.
- **Rationale**: `startDate` is the primary sort, but multiple events often have the same start time. Adding `id,asc` ensures a stable order even when metadata (like `updatedAt`) changes.
- **Affected Hooks**: `useGetEvents`, `useGetEventsByEmployeeId`.

### 3. Backend Specification Fix
- **Decision**: Update `EventSpecifications.withEmployeePaid` to use `root.get("employeePaymentDate")`.
- **Rationale**: The field `employeePaid` does not exist on the `Event` entity; the status is derived from whether `employeePaymentDate` is null.

## Risks / Trade-offs

- **[Risk] Date Formatting Mismatch** → **Mitigation**: Use `fromDateToDatetimeLocalInput` utility and ensure backend receives standard ISO strings.
- **[Risk] URL Length with many params** → **Mitigation**: Use short parameter names and standard ISO format.
- **[Risk] Performance with Specification joins** → **Mitigation**: Backend already uses `@EntityGraph` in the repository to prevent N+1 issues.
