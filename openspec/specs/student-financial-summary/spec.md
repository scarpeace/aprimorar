# Capability: Student Financial Summary

## Purpose
TBD - This capability provides and displays a monthly financial summary (KPIs) for a specific student, including total events, charged amounts, and pending amounts.

## Requirements

### Requirement: Student Monthly Financial Summary Endpoint
The backend SHALL expose an endpoint to retrieve the monthly financial summary for a specific student.

#### Scenario: Requesting summary with explicit dates
- **WHEN** a client requests `/v1/students/{id}/summary` with `month` and `year` parameters
- **THEN** the system SHALL calculate and return the total events, total charged amount (sum of `price` where `studentChargeDate` is NOT NULL), and total pending amount (sum of `price` where `studentChargeDate` is NULL) for that specific student within the given month.

#### Scenario: Requesting summary without explicit dates
- **WHEN** a client requests `/v1/students/{id}/summary` without `month` or `year` parameters
- **THEN** the system SHALL calculate and return the summary for the current month.

### Requirement: Student KPIs UI Component
The frontend SHALL display a `StudentKPIs` component on the `StudentDetailsPage` showing the financial summary.

#### Scenario: Displaying the KPI cards
- **WHEN** the user views the `StudentDetailsPage`
- **THEN** they SHALL see three KPI cards: "Total de atendimentos", "Total Cobrado", and "Total Pendente" derived from the student summary endpoint.
