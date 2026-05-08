## MODIFIED Requirements

### Requirement: Module auth

The auth module SHALL handle authentication, authorization, and user management.

- Package: `com.aprimorar.api.domain.auth`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `User` (table `tb_users`)

**Public API (`api/`):**
- `AuthService` (interface), `UserService` (interface) — adds `deleteByEmployeeId()`, `findByEmployeeId()`
- DTOs: `AuthLoginRequestDTO`, `AuthCurrentUserResponseDTO`, `UserCreateRequestDTO`, `UserResponseDTO`
- Exceptions: `UserNotFoundException`, `UserAlreadyExistsException`

**Internal (`internal/`):**
- `AuthController` (`/v1/auth`), `UserController` (`/v1/users`)
- `AuthServiceImpl`, `UserServiceImpl`
- `User`, `UserRepository`

#### Scenario: JPA entity cross-reference to employee module
- **GIVEN** the `User` entity
- **THEN** it SHALL have a `@OneToOne(fetch = FetchType.LAZY, optional = false)` reference to `Employee` from the employee module

#### Scenario: Cross-module service call to employee module
- **GIVEN** the `UserService` needs to look up an employee when creating a user
- **THEN** it SHALL use `EmployeeService.getReferenceById()` from the employee module's API
- **AND** it SHALL NOT directly access `EmployeeRepository`

#### Scenario: Service interface in api package
- **GIVEN** the auth module
- **WHEN** other modules need auth services
- **THEN** they SHALL import from `com.aprimorar.api.domain.auth.api`
- **AND** they SHALL NOT import from `com.aprimorar.api.domain.auth.internal`

### Requirement: Module student

The student module SHALL manage student CRUD and enrollment.

- Package: `com.aprimorar.api.domain.student`
- Structure: `api/` + `internal/`
- Key entity: `Student` (table `tb_students`)

**Public API (`api/`):**
- `StudentService` (interface) — adds `existsById()`, `hasActiveLinkedStudents()`
- DTOs, exceptions

**Internal (`internal/`):**
- `StudentController`, `StudentServiceImpl`
- `Student`, `StudentRepository`, `StudentSpecifications`
- `StudentMapper`

#### Scenario: Cross-module service calls to parent and event modules
- **GIVEN** the `StudentService` needs parent or event data
- **THEN** it SHALL use `ParentService.findById()` and `EventService.*()` from the respective modules' APIs
- **AND** it SHALL NOT directly access `ParentRepository` or `EventRepository`

### Requirement: Module parent

The parent module SHALL manage parent/guardian CRUD.

- Package: `com.aprimorar.api.domain.parent`
- Structure: `api/` + `internal/`
- Key entity: `Parent` (table `tb_parent`)

**Public API (`api/`):**
- `ParentService` (interface) — adds `findById()`

#### Scenario: Cross-module service call to student module
- **GIVEN** the `ParentService` needs to check for linked students before archiving
- **THEN** it SHALL use `StudentService.hasActiveLinkedStudents()` from the student module's API
- **AND** it SHALL NOT directly access `StudentRepository`

### Requirement: Module employee

The employee module SHALL manage employee/staff CRUD.

- Package: `com.aprimorar.api.domain.employee`
- Structure: `api/` + `internal/`
- Key entity: `Employee` (table `tb_employees`)

**Public API (`api/`):**
- `EmployeeService` (interface) — adds `existsById()`, `getReferenceById()`

#### Scenario: Cross-module service calls to auth and event modules
- **GIVEN** the `EmployeeService` needs user or event data
- **THEN** it SHALL use `UserService.findByEmployeeId()`, `UserService.deleteByEmployeeId()`, and `EventService.*()` from the respective modules' APIs
- **AND** it SHALL NOT directly access `UserRepository` or `EventRepository`

### Requirement: Module event

The event module SHALL manage event scheduling and sessions.

- Package: `com.aprimorar.api.domain.event`
- Structure: `api/` + `internal/`
- Key entity: `Event` (table `tb_events`)

**Public API (`api/`):**
- `EventService` (interface) — adds `countByStudentId()`, `countByEmployeeId()`, `sumChargedByStudentId()`, `sumPaidByEmployeeId()`, `reassignStudentEventsToGhost()`, `reassignEmployeeEventsToGhost()`

#### Scenario: Cross-module service calls to student and employee modules
- **GIVEN** the `EventService` needs student or employee data
- **THEN** it SHALL use `StudentService` and `EmployeeService` from the respective modules' APIs
- **AND** it SHALL NOT directly access `StudentRepository` or `EmployeeRepository`

### Requirement: Module finance

The finance module SHALL manage financial transactions and summaries.

- Package: `com.aprimorar.api.domain.finance`
- Structure: `api/` + `internal/`
- Key entity: `Transaction` (table `tb_transactions`)

#### Scenario: Cross-module service calls to student, employee, and event modules
- **GIVEN** the `FinanceService` needs student, employee, or event data for summaries
- **THEN** it SHALL use `StudentService.existsById()`, `EmployeeService.existsById()`, and `EventService.countByStudentId()`/`EventService.countByEmployeeId()` from the respective modules' APIs
- **AND** it SHALL NOT directly access `StudentRepository`, `EmployeeRepository`, or `EventRepository`

## MODIFIED Requirements (Dependency Graph)

### Requirement: Dependency graph

The dependency graph SHALL document all cross-module interactions.

#### Scenario: Post-refactor dependency matrix
- **GIVEN** the module structure after refactoring the 12 service→repository shortcuts
- **THEN** the dependency graph SHALL be reduced to:
  - `student ──> address` (JPA @Embedded)
  - `event ──> finance` (EventService→TransactionService)
  - (Zero direct repository bypasses remaining)
- **AND** all 12 convenience dependencies SHALL be resolved to service→service calls

## MODIFIED Requirements (Cross-Module Rules)

### Requirement: Cross-module rules

#### Scenario: Direct repository access removed
- **GIVEN** a service in module A that needs data from module B
- **THEN** it SHALL use module B's service API (not its repository directly)
- **AND** there SHALL be zero direct repository bypass patterns remaining
