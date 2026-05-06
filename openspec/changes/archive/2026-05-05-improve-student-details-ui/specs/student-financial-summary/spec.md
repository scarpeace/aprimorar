## MODIFIED Requirements

### Requirement: Student KPIs UI Component
The frontend SHALL display a `StudentKPIs` component on the `StudentDetailsPage` showing the financial summary. This component SHALL use a responsive grid layout to ensure readability on all devices.

#### Scenario: Displaying the KPI cards with date range
- **WHEN** the user views the `StudentDetailsPage` and selects a date range
- **THEN** the `StudentKPIs` component SHALL display three KPI cards in a 3-column grid on desktop and a 1-column stack on mobile, reflecting the data within the selected date range.
