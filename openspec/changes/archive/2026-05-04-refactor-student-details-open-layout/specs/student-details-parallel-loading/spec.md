## MODIFIED Requirements

### Requirement: Independent Section States
Each major section of the student details page (Student Info, KPIs, and Events Table) SHALL manage its own loading and error states independently, preventing a failure in one section from blocking others. Sections SHALL be rendered as semantic blocks within the page layout.

#### Scenario: Partial data loading
- **WHEN** the student info query is pending but the KPIs query completes
- **THEN** the KPI cards display their data while the student info section continues to show a loading placeholder.
