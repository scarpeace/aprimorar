# Employee Financial Summary

## Purpose
This capability provides a summary of financial data and attendance for employees, allowing for both an all-time overview and period-specific analysis.
## Requirements
### Requirement: Employee Financial Summary Endpoint
The finance module SHALL expose an endpoint to retrieve a financial and attendance summary for a specific employee.

#### Scenario: Requesting summary without explicit dates
- **WHEN** a client requests `/v1/finance/employees/{id}/summary` without `startDate` or `endDate` parameters
- **THEN** the system SHALL calculate and return the total events, total paid amount, and total unpaid amount for that specific employee across all recorded history using transactions linked to event employee payments

#### Scenario: Requesting summary with explicit date range
- **WHEN** a client requests `/v1/finance/employees/{id}/summary` with `startDate` and `endDate` parameters
- **THEN** the system SHALL calculate and return the totals for that specific employee within the given period using transactions linked to event employee payments

### Requirement: Employee KPIs UI Component
The frontend SHALL display an `EmployeeKPIs` component showing either a general summary or a filtered summary based on the page-level date filter.

#### Scenario: Displaying the general summary
- **WHEN** the user views the Employee Details page and **no date range** is selected
- **THEN** the `EmployeeKPIs` component SHALL display the total accumulation of data, labeled as "Resumo Geral".

#### Scenario: Displaying the filtered summary
- **WHEN** the user selects a date range
- **THEN** the `EmployeeKPIs` component SHALL update to show data only for that period.

