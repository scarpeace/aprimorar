## ADDED Requirements

### Requirement: Employee Payment Status Indicator
The system SHALL display a payment status indicator in the EmployeesTable showing the current payment state of each employee.

#### Scenario: Employee has pending payments
- **WHEN** an employee has `totalUnpaid` greater than zero in the monthly summary
- **THEN** the status column SHALL display a yellow indicator
- **AND** the indicator SHALL be visible without requiring user interaction

#### Scenario: Employee is fully paid and current
- **WHEN** an employee has `totalUnpaid` equal to zero AND the last payment date is within the last 30 days
- **THEN** the status column SHALL display a green indicator
- **AND** the indicator SHALL be visible without requiring user interaction

#### Scenario: Employee payment is overdue
- **WHEN** an employee's last payment date is more than 30 days ago (regardless of current unpaid amount)
- **THEN** the status column SHALL display a red indicator
- **AND** the indicator SHALL be visible without requiring user interaction

#### Scenario: Employee is archived
- **WHEN** an employee has an `archivedAt` timestamp set
- **THEN** the status column SHALL display an archived indicator (existing behavior)
- **AND** the payment status indicator SHALL NOT be shown