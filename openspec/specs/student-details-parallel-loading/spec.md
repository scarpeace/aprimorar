# Capability: Student Details Parallel Loading

## Purpose
Optimize the Student Details page to provide a faster perceived performance by rendering the layout immediately and fetching data sections in parallel.

## Requirements

### Requirement: Non-blocking Layout Rendering
The `StudentDetailsPage` SHALL render its layout shell (header and navigation) immediately upon mounting, without waiting for any data fetching to complete. The header SHALL be responsive, adjusting icon size and navigation button placement based on screen width.

#### Scenario: Initial page mount
- **WHEN** the user navigates to the student details page
- **THEN** the `PageLayout` with title and description is visible immediately, while the internal sections display loading indicators.

#### Scenario: Initial page mount on mobile
- **WHEN** the user navigates to the student details page on a mobile device
- **THEN** the `PageLayout` is visible with a smaller icon and stacked or simplified navigation, while the internal sections display loading indicators.

### Requirement: Parallel Data Fetching
The system SHALL initiate data fetching for the student profile, financial summary (KPIs), and events table concurrently to minimize the total loading time.

#### Scenario: Concurrent API requests
- **WHEN** the student details page is mounted
- **THEN** simultaneous requests for the student record, student summary, and student events are dispatched.

### Requirement: Independent Section States
Each major section of the student details page (Student Info, KPIs, and Events Table) SHALL manage its own loading and error states independently, preventing a failure in one section from blocking others. Sections SHALL be rendered as semantic blocks within the page layout and use staggered animations for a smoother visual entry.

#### Scenario: Partial data loading
- **WHEN** the student info query is pending but the KPIs query completes
- **THEN** the KPI cards display their data with a subtle fade-in animation while the student info section continues to show a loading placeholder.

### Requirement: Date Range Reference Stability
The system SHALL ensure that date objects derived from URL search parameters are memoized, maintaining reference stability across page re-renders where the search parameters have not changed.

#### Scenario: Re-render with stable dates
- **WHEN** a state change occurs in the parent `StudentDetailsPage` (e.g., opening a modal) that does not affect URL parameters
- **THEN** the `DateRangeInput` component receives the same date object references as props, preventing unnecessary re-renders.
