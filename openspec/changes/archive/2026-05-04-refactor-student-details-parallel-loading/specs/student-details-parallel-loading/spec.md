## ADDED Requirements

### Requirement: Non-blocking Layout Rendering
The `StudentDetailsPage` SHALL render its layout shell (header and navigation) immediately upon mounting, without waiting for any data fetching to complete.

#### Scenario: Initial page mount
- **WHEN** the user navigates to the student details page
- **THEN** the `PageLayout` with title and description is visible immediately, while the internal sections display loading indicators.

### Requirement: Parallel Data Fetching
The system SHALL initiate data fetching for the student profile, financial summary (KPIs), and events table concurrently to minimize the total loading time.

#### Scenario: Concurrent API requests
- **WHEN** the student details page is mounted
- **THEN** simultaneous requests for the student record, student summary, and student events are dispatched.

### Requirement: Independent Section States
Each major section of the student details page (Student Info, KPIs, and Events Table) SHALL manage its own loading and error states independently, preventing a failure in one section from blocking others.

#### Scenario: Partial data loading
- **WHEN** the student info query is pending but the KPIs query completes
- **THEN** the KPI cards display their data while the student info section continues to show a loading placeholder.

### Requirement: Date Range Reference Stability
The system SHALL ensure that date objects derived from URL search parameters are memoized, maintaining reference stability across page re-renders where the search parameters have not changed.

#### Scenario: Re-render with stable dates
- **WHEN** a state change occurs in the parent `StudentDetailsPage` (e.g., opening a modal) that does not affect URL parameters
- **THEN** the `DateRangeInput` component receives the same date object references as props, preventing unnecessary re-renders.
