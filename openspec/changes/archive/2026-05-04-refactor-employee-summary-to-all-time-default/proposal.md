## Why

Currently, the employee financial summary (KPIs) defaults to the current month, while the events table on the same page can display all-time data or a custom date range. This inconsistency matches the previous state of the student flow. Aligning the employee summary to use an "All Time" default view and a unified page-level date filter will provide a consistent and intuitive user experience across the application.

## What Changes

- **Backend**: **BREAKING** Rename `EmployeeMonthlySummaryDTO` to `EmployeeSummaryDTO`.
- **Backend**: **BREAKING** Rename the endpoint `/v1/employees/{id}/monthly-summary` to `/v1/employees/{id}/summary` (or update its parameters to `startDate` and `endDate` of type `Instant`).
- **Backend**: Implement "All Time" aggregation logic in `EmployeeService.getSummary` when no dates are provided.
- **Backend**: Add employee-specific "All Time" methods to `EventRepository`.
- **Frontend**: Move `DateRangeInput` from `EmployeeEventsTable` to the page level in `EmployeeDetailsPage`.
- **Frontend**: Synchronize both `EmployeeKPIs` and `EmployeeEventsTable` to use the shared date range from the URL search parameters.
- **Frontend**: Update `EmployeeKPIs` descriptions to "Resumo Geral" when no filters are active.

## Capabilities

### New Capabilities
- `employee-financial-summary`: Requirements for retrieving and displaying financial and attendance summaries for employees, supporting both period-based and all-time views.

### Modified Capabilities
- None

## Impact

- **API**: Breaking change in DTO naming and endpoint parameters/path.
- **Backend**: Logic update in `EmployeeService` and new repository methods.
- **Frontend**: Refactoring of the employee details view components.
- **Developer Experience**: Improved consistency with the student flow patterns.
