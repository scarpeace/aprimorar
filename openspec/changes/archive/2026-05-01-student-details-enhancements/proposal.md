## Why

The `StudentDetailsPage` currently shows a basic `EventsTable` but lacks the rich, filtered context provided by the `EmployeeDetailPage`. Users need to see a monthly summary of charges for a student (total events, total charged, total pending) to effectively manage student accounts, just as they manage employee payouts. Additionally, the event table needs full filtering capabilities (date range, hide paid/charged, search) specific to that student.

## What Changes

- **Backend: Student Summary**: Create a new endpoint `/v1/students/{id}/summary` returning a `StudentMonthlySummaryDTO` (similar to the employee summary).
- **Backend: Student Events Filtering**: Enhance `getEventsByStudentId` (`/v1/events/{id}/student`) to accept the same filtering parameters as the employee endpoint (`startDate`, `endDate`, `employeeName`, `hideCharged`, etc.).
- **Frontend: Student KPIs**: Create a `StudentKPIs` component to display the monthly summary (Total Events, Total Charged, Total Pending).
- **Frontend: Student Events Table**: Create a `StudentEventsTable` component replicating the `EmployeeEventsTable` structure, using the new `DateRangeInput` for date filtering, and integrating search and "hide charged" toggles. If no date is selected, it will default to showing all events for the student.
- **Frontend: Page Integration**: Update `StudentDetailsPage.tsx` to include the new `StudentKPIs` and replace the generic `EventsTable` with the new `StudentEventsTable`.

## Capabilities

### New Capabilities
- `student-financial-summary`: Ability to view a monthly summary of a student's financial status (charges and events).
- `student-events-filtering`: Ability to filter a student's specific event history by date range and charge status.

### Modified Capabilities
- (None)

## Impact

- **Frontend**: `StudentDetailsPage.tsx` will be refactored to use new components `StudentKPIs.tsx` and `StudentEventsTable.tsx`.
- **Backend**: `StudentController`, `StudentService` (or `EventService`), and `EventRepository` will need new queries and endpoints for the summary and enhanced filtering.
