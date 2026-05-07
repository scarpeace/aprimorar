## MODIFIED Requirements

### Requirement: Employee Financial Summary Endpoint
The finance module SHALL expose an endpoint to retrieve a financial and attendance summary for a specific employee.

#### Scenario: Requesting summary without explicit dates
- **WHEN** a client requests `/v1/finance/employees/{id}/summary` without `startDate` or `endDate` parameters
- **THEN** the system SHALL calculate and return the total events, total paid amount, and total unpaid amount for that specific employee across all recorded history using transactions linked to event employee payments

#### Scenario: Requesting summary with explicit date range
- **WHEN** a client requests `/v1/finance/employees/{id}/summary` with `startDate` and `endDate` parameters
- **THEN** the system SHALL calculate and return the totals for that specific employee within the given period using transactions linked to event employee payments
