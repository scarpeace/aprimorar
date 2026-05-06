## MODIFIED Requirements

### Requirement: Independent Section States
The each major section of the student details page (Student Info, KPIs, and Events Table) SHALL manage its own loading and error states independently, preventing a failure in one section from blocking others. Additionally, sections SHALL use staggered animations for a smoother visual entry.

#### Scenario: Partial data loading
- **WHEN** the student info query is pending but the KPIs query completes
- **THEN** the KPI cards display their data with a subtle fade-in animation while the student info section continues to show a loading placeholder.

### Requirement: Non-blocking Layout Rendering
The `StudentDetailsPage` SHALL render its layout shell (header and navigation) immediately upon mounting. The header SHALL be responsive, adjusting icon size and navigation button placement based on screen width.

#### Scenario: Initial page mount on mobile
- **WHEN** the user navigates to the student details page on a mobile device
- **THEN** the `PageLayout` is visible with a smaller icon and stacked or simplified navigation, while the internal sections display loading indicators.
