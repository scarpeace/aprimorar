## MODIFIED Requirements

### Requirement: Student KPIs UI Component
The frontend SHALL display a `StudentKPIs` component on the `StudentDetailsPage` showing the financial summary, synchronized with the page-level date filter. This component SHALL render its KPIs without a surrounding card container.

#### Scenario: Displaying the KPI cards with date range
- **WHEN** the user views the `StudentDetailsPage` and selects a date range
- **THEN** the `StudentKPIs` component SHALL display three KPI cards (stats) reflecting the data within the selected date range.
