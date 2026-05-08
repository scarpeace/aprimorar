## ADDED Requirements

### Requirement: Event-backed transactions
The system SHALL create and maintain transaction records for the financial impact of each event.

#### Scenario: Creating transactions when an event is created
- **WHEN** a new event is created successfully
- **THEN** the system SHALL create one `IN` transaction for the student charge and one `OUT` transaction for the employee payment
- **AND** both transactions SHALL be created with `PENDING` status
- **AND** both transactions SHALL reference the event as their origin

#### Scenario: Settling the student charge transaction
- **WHEN** the student charge is marked as completed on an event
- **THEN** the system SHALL mark the corresponding student charge transaction as `SETTLED`
- **AND** the system SHALL set the transaction `settledAt` timestamp
- **AND** the system SHALL keep the event `studentChargeDate` synchronized with the settlement

#### Scenario: Settling the employee payment transaction
- **WHEN** the employee payment is marked as completed on an event
- **THEN** the system SHALL mark the corresponding employee payment transaction as `SETTLED`
- **AND** the system SHALL set the transaction `settledAt` timestamp
- **AND** the system SHALL keep the event `employeePaymentDate` synchronized with the settlement

### Requirement: General expenses as transactions
The system SHALL register general expenses as finance transactions instead of using a separate general expense entity.

#### Scenario: Creating a general expense transaction
- **WHEN** a user registers a new general expense
- **THEN** the system SHALL create an `OUT` transaction with `origin = GENERAL_EXPENSE`
- **AND** the transaction SHALL store the selected finance category
- **AND** the transaction `originId` SHALL be the transaction's own identifier

### Requirement: Finance transaction summaries
The finance module SHALL calculate monetary summaries from transactions.

#### Scenario: Requesting the global finance summary
- **WHEN** a client requests the finance summary
- **THEN** the system SHALL calculate total received, total pending income, total paid to collaborators, total pending collaborator payments, total general expenses, and total profit using transactions

#### Scenario: Filtering finance transactions
- **WHEN** a client requests finance transactions with category, type, status, or date filters
- **THEN** the system SHALL return only transactions matching the provided filters
