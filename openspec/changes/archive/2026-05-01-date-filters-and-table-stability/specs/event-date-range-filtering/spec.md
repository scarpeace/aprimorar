## ADDED Requirements

### Requirement: Date Range Filtering on Events Page
The `EventsPage` SHALL allow users to select a specific start and end date range to filter the list of events.

#### Scenario: Filtering by custom range
- **WHEN** the user selects a `startDate` of "2024-05-01" and an `endDate` of "2024-05-15"
- **THEN** the system SHALL update the URL search parameters and refresh the events list to only show events within that range.

### Requirement: Default Date Range
When no date parameters are present in the URL, the `EventsPage` SHALL default the range to the start and end of the current month.

#### Scenario: Initial page load without parameters
- **WHEN** the user navigates to `/events` without any search parameters
- **THEN** the system SHALL default the `startDate` to the first day of the current month and the `endDate` to the last day of the current month.

### Requirement: Deterministic Table Sorting
The system SHALL ensure that the order of events in any table is stable and deterministic, even when event metadata (like payment status) is updated.

#### Scenario: Toggling payment status
- **WHEN** a user toggles the payment status of an event in the table
- **THEN** the event SHALL remain in the same relative position in the list after the UI refreshes.

### Requirement: Backend Payment Status Filtering
The backend SHALL correctly filter events based on whether an employee has been paid, using the `employeePaymentDate` field.

#### Scenario: Filtering by unpaid events
- **WHEN** a request is made to `/v1/events` or `/v1/events/{id}/employee` with `hidePaid=true`
- **THEN** the backend SHALL return only events where `employeePaymentDate` is NULL.
