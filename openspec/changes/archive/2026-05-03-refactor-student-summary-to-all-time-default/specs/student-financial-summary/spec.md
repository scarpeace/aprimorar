## MODIFIED Requirements

### Requirement: Student Financial Summary Endpoint
The backend SHALL expose an endpoint to retrieve a financial and attendance summary for a specific student.

#### Scenario: Requesting summary without explicit dates
- **WHEN** a client requests `/v1/students/{id}/summary` without `startDate` or `endDate` parameters
- **THEN** the system SHALL calculate and return the total events, total charged amount, and total pending amount for that specific student across **all recorded history**.

### Requirement: Student KPIs UI Component
The frontend SHALL display a `StudentKPIs` component showing either a general summary or a filtered summary based on the page-level date filter.

#### Scenario: Displaying the general summary
- **WHEN** the user views the `StudentDetailsPage` and **no date range** is selected
- **THEN** the `StudentKPIs` component SHALL display the total accumulation of data since the student's creation, labeled as "Resumo Geral".
