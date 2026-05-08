## MODIFIED Requirements

### Requirement: Student Financial Summary Endpoint
The finance module SHALL expose an endpoint to retrieve the financial summary for a specific student based on a date range.

#### Scenario: Requesting summary with explicit date range
- **WHEN** a client requests `/v1/finance/students/{id}/summary` with `startDate` and `endDate` parameters
- **THEN** the system SHALL calculate and return the total events, total charged amount, and total pending amount for that specific student within the given period using transactions linked to event student charges

#### Scenario: Requesting summary without explicit dates
- **WHEN** a client requests `/v1/finance/students/{id}/summary` without `startDate` or `endDate` parameters
- **THEN** the system SHALL calculate and return the all-time summary for that student using transactions linked to event student charges
