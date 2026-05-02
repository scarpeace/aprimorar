# Capability: Student Events Filtering

## Purpose
TBD - This capability handles filtering and displaying events for a specific student with advanced criteria like date range, search terms, and charge status.

## Requirements

### Requirement: Enhanced Student Events Query
The `getEventsByStudentId` endpoint SHALL support filtering by date range, search term (employee name), and charge status.

#### Scenario: Filtering by uncharged events
- **WHEN** a client requests `/v1/events/{id}/student` with `hideCharged=true`
- **THEN** the system SHALL return only events where `studentChargeDate` is NULL.

#### Scenario: Filtering by date range
- **WHEN** a client requests `/v1/events/{id}/student` with `startDate` and `endDate`
- **THEN** the system SHALL return only events that fall within that date range.

### Requirement: StudentEventsTable UI Component
The frontend SHALL replace the generic `EventsTable` on the `StudentDetailsPage` with a new `StudentEventsTable` component.

#### Scenario: Default view with no date selected
- **WHEN** the `StudentEventsTable` is rendered and no date parameters exist in the URL
- **THEN** the table SHALL display all events for the student across all time.

#### Scenario: Filtering the table
- **WHEN** the user interacts with the `DateRangeInput`, search bar, or "Hide Charged" toggle
- **THEN** the URL search parameters SHALL update and the table data SHALL refresh to reflect the new filters.
