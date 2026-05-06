## ADDED Requirements

### Requirement: Global Event Filtering for Pending States
The backend SHALL allow filtering the global events list by student charge status and employee payment status.

#### Scenario: Filter by pending student charges
- **WHEN** a GET request is made to `/v1/events` with `hideCharged=true`
- **THEN** the system SHALL return only events where the student has not yet been charged.

#### Scenario: Filter by pending employee payments
- **WHEN** a GET request is made to `/v1/events` with `hidePaid=true`
- **THEN** the system SHALL return only events where the employee has not yet been paid.

### Requirement: All-Time Default Listing
The `EventsPage` SHALL display all events across the entire recorded history by default when no date range is selected.

#### Scenario: Initial page load
- **WHEN** the user navigates to the Events page without URL date parameters
- **THEN** the system SHALL display events from all dates, ordered from newest to oldest.

### Requirement: Tactical Toolbar UI
The `EventsPage` SHALL feature a consolidated toolbar containing search, toggles for pending states, and a date range picker in a single row.

#### Scenario: Using pending state toggles
- **WHEN** the user toggles the "Cobrança Pendente" switch
- **THEN** the UI SHALL update the URL parameters and refresh the events list to show only uncharged events.

### Requirement: Parallel Shell Rendering
The `EventsPage` SHALL render the page header and toolbar immediately upon mounting, with the events list loading asynchronously.

#### Scenario: Fast shell rendering
- **WHEN** the page is mounted
- **THEN** the header and filtering controls are interactive immediately, while the table area displays a loading state.
