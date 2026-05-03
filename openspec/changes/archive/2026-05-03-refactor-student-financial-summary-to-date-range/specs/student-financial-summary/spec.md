## MODIFIED Requirements

### Requirement: Student Monthly Financial Summary Endpoint
The backend SHALL expose an endpoint to retrieve the monthly financial summary for a specific student based on a date range.

#### Scenario: Requesting summary with explicit date range
- **WHEN** a client requests `/v1/students/{id}/summary` with `startDate` and `endDate` parameters
- **THEN** the system SHALL calculate and return the total events, total charged amount (sum of `price` where `studentChargeDate` is NOT NULL), and total pending amount (sum of `price` where `studentChargeDate` is NULL) for that specific student within the given period.

#### Scenario: Requesting summary without explicit dates
- **WHEN** a client requests `/v1/students/{id}/summary` without `startDate` or `endDate` parameters
- **THEN** the system SHALL calculate and return the summary for the current month (from the first day to the last day).

### Requirement: Student KPIs UI Component
The frontend SHALL display a `StudentKPIs` component on the `StudentDetailsPage` showing the financial summary, synchronized with the page-level date filter.

#### Scenario: Displaying the KPI cards with date range
- **WHEN** the user views the `StudentDetailsPage` and selects a date range
- **THEN** the `StudentKPIs` component SHALL display three KPI cards: "Total de atendimentos", "Total Cobrado", and "Total Pendente" reflecting the data within the selected date range.
