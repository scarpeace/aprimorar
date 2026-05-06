## ADDED Requirements

### Requirement: Open Layout and Animations
The Employee Details page SHALL utilize a structured, open layout with a centered maximum width and apply staggered `fade-up` animations to distinct logical sections.

#### Scenario: Page Loading
- **WHEN** the user navigates to the Employee Details page
- **THEN** the layout container SHALL constrain the maximum width.
- **AND THEN** the Info section, the KPI/Filter section, and the Events Table SHALL animate into view sequentially using `fade-up` effects with different delays.

### Requirement: Encapsulated Filters and KPIs
The date filters and KPI summary SHALL be grouped together within a dedicated, styled container.

#### Scenario: Visual Hierarchy
- **WHEN** the user views the Employee Details page
- **THEN** the date range picker and clear filter button SHALL be housed in a styled header card (e.g., `Indicadores e Filtros`).
- **AND THEN** the `EmployeeKPIs` component SHALL be positioned directly beneath this header within the same animated grouping.