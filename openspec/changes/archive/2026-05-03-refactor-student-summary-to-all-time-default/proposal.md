## Why

Currently, when a user views a student's details without selecting a date range, the financial summary (KPIs) defaults to the current month, while the events table shows all-time data. This inconsistency can be confusing. Making the "All Time" view the default for the summary as well provides a better "at-a-glance" standing of the student's total financial and attendance history.

## What Changes

- **Backend**: **BREAKING** Rename `StudentMonthlySummaryDTO` to `StudentSummaryDTO`.
- **Backend**: Update `StudentService.getMonthlySummary` (rename to `getSummary`) to return all-time data if `startDate` and `endDate` are null.
- **Backend**: Add student-specific "All Time" aggregation methods to `EventRepository`.
- **Frontend**: Update `StudentKPIs` to handle the renamed DTO and update the card descriptions to reflect "Resumo Geral" when no filters are active.

## Capabilities

### New Capabilities
- None

### Modified Capabilities
- `student-financial-summary`: Update requirements to reflect the change in default behavior (from current month to all-time) and the DTO renaming.

## Impact

- **API**: Renamed DTO and changed default behavior for parameters.
- **Backend**: Logic update in `StudentService` and new methods in `EventRepository`.
- **Frontend**: Type updates and UI label adjustments.
