## Why

Currently, the `EventsPage` lacks flexible date filtering, making it difficult for users to manage historical or future appointments beyond the current month. Additionally, the event table exhibits unstable behavior where rows jump after updates (e.g., toggling payment), and a backend bug in `EventSpecifications` prevents correct filtering of paid/unpaid events.

## What Changes

- **Full Date Range Filtering**: Implement `startDate` and `endDate` range pickers in `EventsPage` using `useSearchParams` to filter events by day, month, and year.
- **Table Stability**: Add a deterministic tie-breaker (`id,asc`) to all event query sorting criteria to ensure rows maintain their position after state updates.
- **Backend Specification Fix**: Correct the `withEmployeePaid` specification in `EventSpecifications.java` to use the `employeePaymentDate` field instead of the non-existent `employeePaid` field.
- **Default Range**: `EventsPage` will now default to showing the current month's events if no date parameters are provided.

## Capabilities

### New Capabilities
- `event-date-range-filtering`: Ability to filter events by arbitrary start and end date ranges across the system.

### Modified Capabilities
- `brazilian-states-metadata`: (No changes needed here, just listing for completeness of the section structure).

## Impact

- **Frontend**: `EventsPage.tsx`, `EventsTable.tsx`, `EmployeeEventsTable.tsx`.
- **Backend**: `EventSpecifications.java`, `EventController.java`.
- **API**: `getEvents` and `getEventsByEmployeeId` parameters.
