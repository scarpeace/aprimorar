## Context

The employee summary flow currently uses a legacy monthly-only approach, which is inconsistent with the flexible date range filtering available in the events table and the patterns recently established for the student flow.

## Goals / Non-Goals

**Goals:**
- Harmonize the employee details view with the student details view.
- Support all-time and period-based financial and attendance summaries for employees.
- Unify the page-level date filtering.

**Non-Goals:**
- Changing the calculation logic for employee payments (e.g., changing how price or payment is derived).

## Decisions

### 1. DTO and Method Consistency
- **Decision:** Rename `EmployeeMonthlySummaryDTO` to `EmployeeSummaryDTO` and `getMonthlySummary` to `getSummary`.
- **Rationale:** Aligns with the "All Time" capability and matches the Student flow naming.

### 2. Null Date Range as All Time
- **Decision:** When `startDate` and `endDate` are null in the API request, the system will return a summary of all events in the employee's history.
- **Rationale:** Matches the landing state of the UI and provides immediate context on the employee's total standing.

### 3. Unified URL State
- **Decision:** Use URL search parameters (`startDate`, `endDate`) as the single source of truth for the date range on the employee details page.
- **Rationale:** Simplifies component communication and enables deep linking/bookmarking of filtered views.

### 4. Repository Methods
- **Decision:** Implement dedicated all-time aggregation methods in `EventRepository` for employees.
- **Rationale:** Avoids the overhead of passing arbitrary large date ranges or complex null-aware SQL logic in the period-based queries.

## Risks / Trade-offs

- **[Risk]** → Performance on employees with very high event counts.
  - **Mitigation** → Simple aggregation queries on indexed `employee_id` columns are efficient even for large datasets.
- **[Trade-off]** → Breaking API change.
  - **Mitigation** → Internal API with automated client regeneration ensures immediate synchronization.
