## MODIFIED Requirements

### Requirement: Module auth

The auth module SHALL handle authentication, authorization, and user management.

- Package: `com.aprimorar.api.domain.auth`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `User` (table `tb_users`)

**Public API (`api/`):**
- `AuthService` (interface), `UserService` (interface)
- DTOs: `AuthLoginRequestDTO`, `AuthCurrentUserResponseDTO`, `UserCreateRequestDTO`, `UserResponseDTO`
- Exceptions: `UserNotFoundException`, `UserAlreadyExistsException`

**Internal (`internal/`):**
- `AuthController` (`/v1/auth`), `UserController` (`/v1/users`)
- `AuthServiceImpl`, `UserServiceImpl` (implementations)
- `User`, `UserRepository`

#### Scenario: JPA entity cross-reference to employee module
- **GIVEN** the `User` entity
- **THEN** it SHALL have a `@OneToOne(fetch = FetchType.LAZY, optional = false)` reference to `Employee` from the employee module
- **AND** the `UserService` SHALL directly access `EmployeeRepository` to look up employees when creating users

#### Scenario: Cross-module repository access
- **GIVEN** the `UserService`
- **THEN** it SHALL directly inject and call `EmployeeRepository` (convenience dependency — bypasses employee service layer)

#### Scenario: Service interface in api package
- **GIVEN** the auth module
- **WHEN** other modules need auth services
- **THEN** they SHALL import from `com.aprimorar.api.domain.auth.api`
- **AND** they SHALL NOT import from `com.aprimorar.api.domain.auth.internal`

### Requirement: Module student

The student module SHALL manage student CRUD and enrollment.

- Package: `com.aprimorar.api.domain.student`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Student` (table `tb_students`)

**Public API (`api/`):**
- `StudentService` (interface)
- DTOs: `StudentRequestDTO`, `StudentResponseDTO`, `StudentSummaryDTO`, `StudentByParentDTO`, `StudentOptionsDTO`, `StudentResponsibleSummaryDTO`
- Exceptions: `StudentNotFoundException`, `StudentAlreadyExistException`, `InvalidStudentException`

**Internal (`internal/`):**
- `StudentController` (`/v1/students`)
- `StudentServiceImpl` (implementation)
- `Student`, `StudentRepository`, `StudentSpecifications`
- `StudentMapper`

#### Scenario: Embedded address value object
- **GIVEN** the `Student` entity
- **THEN** it SHALL embed `Address` from the address module via `@Embedded`
- **AND** `StudentService` SHALL use `AddressMapper` from the address module
- **AND** `StudentResponseDTO` and `StudentRequestDTO` SHALL compose `AddressResponseDTO` and `AddressRequestDTO` from the address module

#### Scenario: JPA entity cross-reference to parent module
- **GIVEN** the `Student` entity
- **THEN** it SHALL have a `@ManyToOne(cascade = CascadeType.PERSIST)` reference to `Parent` from the parent module

#### Scenario: Cross-module repository access
- **GIVEN** the `StudentService`
- **THEN** it SHALL directly inject and call `ParentRepository` (convenience dependency — bypasses parent service layer)
- **AND** it SHALL directly inject and call `EventRepository` (convenience dependency — bypasses event service layer)

### Requirement: Module parent

The parent module SHALL manage parent/guardian CRUD.

- Package: `com.aprimorar.api.domain.parent`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Parent` (table `tb_parent`)

**Public API (`api/`):**
- `ParentService` (interface)
- DTOs: `ParentRequestDTO`, `ParentResponseDTO`, `ParentOptionsDTO`
- Exceptions: `ParentNotFoundException`, `ParentAlreadyExistsException`, `ParentHasLinkedStudentsException`, `InvalidParentException`

**Internal (`internal/`):**
- `ParentController` (`/v1/parents`)
- `ParentServiceImpl` (implementation)
- `Parent`, `ParentRepository`, `ParentSpecifications`
- `ParentMapper`

#### Scenario: Cross-module repository access
- **GIVEN** the `ParentService`
- **THEN** it SHALL directly inject and call `StudentRepository` (convenience dependency — bypasses student service layer)
- **AND** this dependency SHALL be used to check for active linked students before archiving/deleting a parent

### Requirement: Module employee

The employee module SHALL manage employee/staff CRUD.

- Package: `com.aprimorar.api.domain.employee`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Employee` (table `tb_employees`)

**Public API (`api/`):**
- `EmployeeService` (interface)
- DTOs: `EmployeeRequestDTO`, `EmployeeResponseDTO`, `EmployeeSummaryDTO`, `EmployeeOptionsDTO`
- Exceptions: `EmployeeNotFoundException`, `EmployeeAlreadyExistsException`, `InvalidEmployeeException`

**Internal (`internal/`):**
- `EmployeeController` (`/v1/employees`)
- `EmployeeServiceImpl` (implementation)
- `Employee`, `EmployeeRepository`, `EmployeeSpecifications`
- `EmployeeMapper`

#### Scenario: Cross-module repository access
- **GIVEN** the `EmployeeService`
- **THEN** it SHALL directly inject and call `UserRepository` from the auth module (convenience dependency — bypasses auth service layer)
- **AND** it SHALL directly inject and call `EventRepository` from the event module (convenience dependency — bypasses event service layer)

### Requirement: Module event

The event module SHALL manage event scheduling and sessions.

- Package: `com.aprimorar.api.domain.event`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Event` (table `tb_events`)

**Public API (`api/`):**
- `EventService` (interface)
- DTOs: `EventRequestDTO`, `EventResponseDTO`
- Exceptions: `EventNotFoundException`, `InvalidEventException`, `EventScheduleConflictException`, `NotAllowedToUpdateEventException`

**Internal (`internal/`):**
- `EventController` (`/v1/events`)
- `EventServiceImpl` (implementation)
- `Event`, `EventRepository`, `EventSpecifications`
- `EventMapper`

#### Scenario: JPA entity cross-references
- **GIVEN** the `Event` entity
- **THEN** it SHALL have a `@ManyToOne(fetch = FetchType.LAZY)` reference to `Student` from the student module
- **AND** it SHALL have a `@ManyToOne(fetch = FetchType.LAZY)` reference to `Employee` from the employee module

#### Scenario: Service-to-service dependency to finance module
- **GIVEN** the `EventService`
- **THEN** it SHALL inject and call `TransactionService` from the finance module (proper service-to-service contract — this is the only healthy cross-module dependency in the codebase)
- **AND** `EventService` SHALL call `transactionService.createEventTransactions()`, `syncEventTransactions()`, `deleteEventTransactions()`, `syncStudentCharge()`, and `syncEmployeePayment()`

#### Scenario: Cross-module repository access
- **GIVEN** the `EventService`
- **THEN** it SHALL directly inject and call `StudentRepository` (convenience dependency — bypasses student service layer)
- **AND** it SHALL directly inject and call `EmployeeRepository` (convenience dependency — bypasses employee service layer)

### Requirement: Module finance

The finance module SHALL manage financial transactions and summaries.

- Package: `com.aprimorar.api.domain.finance`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Transaction` (table `tb_transactions`)

**Public API (`api/`):**
- `FinanceService` (interface), `TransactionService` (interface)
- DTOs: `TransactionRequestDTO`, `TransactionResponseDTO`, `FinanceSummaryDTO`
- Exceptions: `TransactionNotFoundException`

**Internal (`internal/`):**
- `FinanceController` (`/v1/finance`)
- `FinanceServiceImpl`, `TransactionServiceImpl` (implementations)
- `Transaction`, `TransactionRepository`, `TransactionSpecifications`

#### Scenario: JPQL join across module boundaries
- **GIVEN** the `TransactionRepository`
- **THEN** it SHALL reference `Event` from the event module in JPQL queries (joins `Transaction t` with `Event e on e.id = t.originId`)
- **AND** this SHALL be used to query transactions by student/employee IDs through the event relationship

#### Scenario: Cross-module repository access
- **GIVEN** the `FinanceService`
- **THEN** it SHALL directly inject and call `EventRepository` (convenience dependency — bypasses event service layer)
- **AND** it SHALL directly inject and call `StudentRepository` (convenience dependency — bypasses student service layer)
- **AND** it SHALL directly inject and call `EmployeeRepository` (convenience dependency — bypasses employee service layer)

### Requirement: Module dashboard

The dashboard module SHALL provide aggregated analytics and operational metrics.

- Package: `com.aprimorar.api.domain.dashboard`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: none (read-only aggregator)

**Public API (`api/`):**
- `DashboardService` (interface? or class?)
- DTOs: `DashboardSummaryResponseDTO`
- Exceptions: `InvalidDashboardRequestException`

**Internal (`internal/`):**
- `DashboardController` (`/v1/dashboard`)
- `DashboardServiceImpl` (if applicable)

#### Scenario: Cross-module repository access
- **GIVEN** the `DashboardService`
- **THEN** it SHALL directly inject and call `EventRepository` from the event module (necessary dependency — dashboards purpose is to aggregate event data)
- **AND** the `DashboardSummaryResponseDTO` SHALL import and use `EventRepository.EventContentCount` inner interface

### Requirement: Module address

The address module SHALL provide an embeddable address value object.

- Package: `com.aprimorar.api.domain.address`
- Structure: `api/` (public) — no `internal/` since address is an embeddable value object
- Key type: `Address` (Embeddable — no separate table)

**Public API (`api/`):**
- `Address`, `AddressMapper`
- DTOs: `AddressRequestDTO`, `AddressResponseDTO`
- Exceptions: `InvalidAddressException`

#### Scenario: Zero domain dependencies
- **GIVEN** the address module
- **THEN** it SHALL have ZERO imports from other domain modules
- **AND** it SHALL only depend on shared infrastructure (`shared.MapperUtils`, `enums.BrazilianStates`)

## ADDED Requirements

### Requirement: Module API surface structure

Each domain module SHALL follow a consistent package structure with a public `api/` package and a private `internal/` package.

#### Scenario: Public API in api package
- **GIVEN** any domain module
- **WHEN** another module needs to access its types
- **THEN** the public API SHALL be in `com.aprimorar.api.domain.<module>.api`
- **AND** only interfaces, DTOs, and exception classes SHALL be in the `api/` package

#### Scenario: Internal implementation in internal package
- **GIVEN** any domain module
- **WHEN** it contains controllers, entities, repositories, mappers, or service implementations
- **THEN** those SHALL be in `com.aprimorar.api.domain.<module>.internal`
- **AND** they SHALL NOT be imported by other modules

#### Scenario: Service interface pattern
- **GIVEN** a service that is consumed by other modules
- **THEN** its interface SHALL be in the `api/` package
- **AND** its implementation SHALL be in the `internal/` package
- **AND** consumers SHALL depend only on the interface
