# Capability: Server Architecture

## Purpose

Captures the structural architecture of the server backend — module decomposition, dependency rules, shared infrastructure, and entity relationship boundaries. This spec evolves during the modulith migration to reflect each phase's changes.
## Requirements
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

#### Scenario: ID-based column reference to employee module
- **GIVEN** the `User` entity
- **THEN** it SHALL have a `@Column(name = "employee_id", nullable = false, unique = true)` of type `UUID` referencing the employee module by ID
- **AND** it SHALL NOT have a `@OneToOne` JPA relationship to `Employee`

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
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Student` (table `tb_students`)

**Public API (`api/`):**
- `StudentService` (interface) — adds `existsById()`, `hasActiveLinkedStudents()`
- DTOs: `StudentRequestDTO`, `StudentResponseDTO`, `StudentSummaryDTO`, `StudentByParentDTO`, `StudentOptionsDTO`, `StudentResponsibleSummaryDTO`
- Exceptions: `StudentNotFoundException`, `StudentAlreadyExistException`, `InvalidStudentException`

**Internal (`internal/`):**
- `StudentController` (`/v1/students`)
- `StudentServiceImpl`
- `Student`, `StudentRepository`, `StudentSpecifications`
- `StudentMapper`

#### Scenario: Embedded address value object
- **GIVEN** the `Student` entity
- **THEN** it SHALL embed `Address` from the address module via `@Embedded`
- **AND** `StudentService` SHALL use `AddressMapper` from the address module
- **AND** `StudentResponseDTO` and `StudentRequestDTO` SHALL compose `AddressResponseDTO` and `AddressRequestDTO` from the address module

#### Scenario: ID-based column reference to parent module
- **GIVEN** the `Student` entity
- **THEN** it SHALL have a `@Column(name = "parent_id", nullable = false)` of type `UUID` referencing the parent module by ID
- **AND** it SHALL NOT have a `@ManyToOne` JPA relationship to `Parent`

#### Scenario: Cross-module service calls to parent and event modules
- **GIVEN** the `StudentService` needs parent or event data
- **THEN** it SHALL use `ParentService.findById()` and `EventService.*()` from the respective modules' APIs
- **AND** it SHALL NOT directly access `ParentRepository` or `EventRepository`

### Requirement: Module parent

The parent module SHALL manage parent/guardian CRUD.

- Package: `com.aprimorar.api.domain.parent`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Parent` (table `tb_parent`)

**Public API (`api/`):**
- `ParentService` (interface) — adds `findById()`
- DTOs: `ParentRequestDTO`, `ParentResponseDTO`, `ParentOptionsDTO`
- Exceptions: `ParentNotFoundException`, `ParentAlreadyExistsException`, `ParentHasLinkedStudentsException`, `InvalidParentException`

**Internal (`internal/`):**
- `ParentController` (`/v1/parents`)
- `ParentServiceImpl`
- `Parent`, `ParentRepository`, `ParentSpecifications`
- `ParentMapper`

#### Scenario: Cross-module service call to student module
- **GIVEN** the `ParentService` needs to check for linked students before archiving
- **THEN** it SHALL use `StudentService.hasActiveLinkedStudents()` from the student module's API
- **AND** it SHALL NOT directly access `StudentRepository`

### Requirement: Module employee

The employee module SHALL manage employee/staff CRUD.

- Package: `com.aprimorar.api.domain.employee`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Employee` (table `tb_employees`)

**Public API (`api/`):**
- `EmployeeService` (interface) — adds `existsById()`, `getReferenceById()`
- DTOs: `EmployeeRequestDTO`, `EmployeeResponseDTO`, `EmployeeSummaryDTO`, `EmployeeOptionsDTO`
- Exceptions: `EmployeeNotFoundException`, `EmployeeAlreadyExistsException`, `InvalidEmployeeException`

**Internal (`internal/`):**
- `EmployeeController` (`/v1/employees`)
- `EmployeeServiceImpl`
- `Employee`, `EmployeeRepository`, `EmployeeSpecifications`
- `EmployeeMapper`

#### Scenario: Cross-module service calls to auth and event modules
- **GIVEN** the `EmployeeService` needs user or event data
- **THEN** it SHALL use `UserService.findByEmployeeId()`, `UserService.deleteByEmployeeId()`, and `EventService.*()` from the respective modules' APIs
- **AND** it SHALL NOT directly access `UserRepository` or `EventRepository`

### Requirement: Module event

The event module SHALL manage event scheduling and sessions.

- Package: `com.aprimorar.api.domain.event`
- Structure: `api/` (public) + `internal/` (private)
- Key entity: `Event` (table `tb_events`)

**Public API (`api/`):**
- `EventService` (interface) — adds `countByStudentId()`, `countByEmployeeId()`, `sumChargedByStudentId()`, `sumPaidByEmployeeId()`, `reassignStudentEventsToGhost()`, `reassignEmployeeEventsToGhost()`
- DTOs: `EventRequestDTO`, `EventResponseDTO`
- Exceptions: `EventNotFoundException`, `InvalidEventException`, `EventScheduleConflictException`, `NotAllowedToUpdateEventException`

**Internal (`internal/`):**
- `EventController` (`/v1/events`)
- `EventServiceImpl`
- `Event`, `EventRepository`, `EventSpecifications`
- `EventMapper`

#### Scenario: ID-based column references
- **GIVEN** the `Event` entity
- **THEN** it SHALL have a `@Column(name = "student_id", nullable = false)` of type `UUID` referencing the student module by ID
- **AND** it SHALL have a `@Column(name = "employee_id", nullable = false)` of type `UUID` referencing the employee module by ID
- **AND** it SHALL NOT have `@ManyToOne` JPA relationships to `Student` or `Employee`

#### Scenario: Service-to-service dependency to finance module
- **GIVEN** the `EventService`
- **THEN** it SHALL inject and call `TransactionService` from the finance module (proper service-to-service contract — this is the only healthy cross-module dependency in the codebase)
- **AND** `EventService` SHALL call `transactionService.createEventTransactions()`, `syncEventTransactions()`, `deleteEventTransactions()`, `syncStudentCharge()`, and `syncEmployeePayment()`

#### Scenario: Cross-module service calls to student and employee modules
- **GIVEN** the `EventService` needs student or employee data
- **THEN** it SHALL use `StudentService` and `EmployeeService` from the respective modules' APIs
- **AND** it SHALL NOT directly access `StudentRepository` or `EmployeeRepository`

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

#### Scenario: Cross-module service calls to student, employee, and event modules
- **GIVEN** the `FinanceService` needs student, employee, or event data for summaries
- **THEN** it SHALL use `StudentService.existsById()`, `EmployeeService.existsById()`, and `EventService.countByStudentId()`/`EventService.countByEmployeeId()` from the respective modules' APIs
- **AND** it SHALL NOT directly access `StudentRepository`, `EmployeeRepository`, or `EventRepository`

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

### Requirement: Shared infrastructure

The shared infrastructure package SHALL provide common base types used across all modules.

- Package: `com.aprimorar.api.shared`
- Types: `BaseEntity`, `MapperUtils`, `PageDTO`

#### Scenario: Base entity for all domain entities
- **GIVEN** all domain entities
- **WHEN** they extend `BaseEntity`
- **THEN** they SHALL inherit `id`, `createdAt`, `updatedAt`, and `archivedAt` fields
- **AND** they SHALL use soft-delete via the `archivedAt` timestamp

#### Scenario: Utility methods available to all modules
- **GIVEN** any module needs CPF, phone, or ZIP code formatting
- **WHEN** it uses `MapperUtils`
- **THEN** it SHALL import from `com.aprimorar.api.shared` without module verification violations

### Requirement: Enumerations

The enums package SHALL provide shared enumeration types used across multiple modules.

- Package: `com.aprimorar.api.enums`
- Enums: `BrazilianStates`, `Duty`, `EventContent`, `Role`, `TransactionCategory`, `TransactionOrigin`, `TransactionStatus`, `TransactionType`

#### Scenario: Shared enum accessible from any module
- **GIVEN** any module needs to reference a shared enum (e.g., `Role`, `Duty`, `EventContent`)
- **WHEN** it imports from `com.aprimorar.api.enums`
- **THEN** it SHALL be able to do so without module verification violations

### Requirement: Global infrastructure

The global infrastructure package SHALL provide cross-cutting Spring configuration and exception handling.

- Package: `com.aprimorar.api.config` (configurations)
- Package: `com.aprimorar.api.exception` (exception handling)

#### Scenario: Global exception handling with RFC 9457
- **GIVEN** any module throws a domain exception
- **WHEN** the exception reaches the controller layer
- **THEN** the `GlobalExceptionHandler` SHALL convert it to RFC 9457 Problem Details format
- **AND** it SHALL return the appropriate HTTP status code

#### Scenario: Spring configuration accessible to all modules
- **GIVEN** `SecurityConfig`, `WebCorsConfig`, `ClockConfig`, `PasswordEncoderConfig`, and `JacksonConfig`
- **WHEN** any module's Spring beans need these configurations
- **THEN** they SHALL be accessible without module verification violations

### Requirement: Dependency graph

The dependency graph SHALL document all cross-module interactions. Directed edges represent direct access (entity references, repository injections, service calls).

#### Scenario: Post-refactor dependency matrix
- **GIVEN** the module structure after refactoring the 12 service→repository shortcuts
- **THEN** the dependency graph SHALL be reduced to:
  - `student ──> address` (JPA @Embedded)
  - `event ──> finance` (EventService→TransactionService)
- **AND** 12 convenience dependencies SHALL be resolved to service→service calls
- **AND** zero direct repository bypasses SHALL remain

### Requirement: Cross-module rules

#### Scenario: Direct repository access removed
- **GIVEN** a service in module A that needs data from module B
- **THEN** it SHALL use module B's service API (not its repository directly)
- **AND** there SHALL be zero direct repository bypass patterns remaining

#### Scenario: JPA entity references across modules
- **GIVEN** entities in different modules with relationships
- **THEN** they SHALL use ID-based columns (`@Column UUID`) instead of direct JPA annotations (`@ManyToOne`, `@OneToOne`)
- **AND** cross-entity lookups SHALL be done via service calls (not JPA relationship traversal)

#### Scenario: Global exception handling
- **GIVEN** exceptions thrown from any module
- **THEN** the `GlobalExceptionHandler` SHALL handle them centrally using RFC 9457 Problem Details format
- **AND** all module-specific exceptions SHALL extend `RuntimeException` (no custom base exception class)

#### Scenario: Soft delete across all entities
- **GIVEN** all entities extend `BaseEntity`
- **THEN** the `archivedAt` timestamp SHALL be used for soft deletion
- **AND** queries SHALL filter by `archivedAt IS NULL` unless explicitly including archived records

### Requirement: Module registration and verification

Each domain module SHALL be registered via `package-info.java` with `@ApplicationModule`, declaring its allowed dependencies. Module verification SHALL be active to enforce boundaries.

#### Scenario: Module package-info with allowed dependencies
- **GIVEN** a module under `com.aprimorar.api.domain`
- **WHEN** the module has cross-module dependencies that are not yet resolved
- **THEN** the `package-info.java` SHALL declare those as `allowedDependencies`
- **AND** each allowed dependency SHALL be removed as the corresponding cross-module access is resolved in later phases

#### Scenario: Module verification on build
- **GIVEN** the project is built with `./mvnw test` or `./mvnw verify`
- **THEN** the Spring Modulith verification SHALL run as part of the test phase
- **AND** any module accessing types outside its `allowedDependencies` SHALL cause a test failure

### Requirement: Shared packages

The following packages SHALL be excluded from module verification and accessible to all modules:

| Package | Contents |
|---------|----------|
| `com.aprimorar.api.shared` | `BaseEntity`, `MapperUtils`, `PageDTO` |
| `com.aprimorar.api.enums` | All shared enumerations |
| `com.aprimorar.api.exception` | `GlobalExceptionHandler`, `ProblemResponseDTO`, `ErrorCode` |
| `com.aprimorar.api.config` | Spring configuration classes (security, CORS, Jackson, clock, password encoder) |

#### Scenario: Shared package accessibility
- **GIVEN** any module class
- **WHEN** it imports from `shared`, `enums`, `exception`, or `config` packages
- **THEN** the module verification SHALL NOT flag this as a violation

### Requirement: Module dependency registry

The allowed dependencies for each module (post-Phase 4) SHALL be:

| Module | Allowed Dependencies | Rationale |
|--------|---------------------|-----------|
| auth | employee | UserService→EmployeeService service call via ObjectProvider (no JPA @OneToOne) |
| student | address, parent, event | @Embedded Address, StudentService→ParentService/EventService service calls (no JPA @ManyToOne to Parent) |
| parent | student | ParentService→StudentService call |
| employee | auth, event | EmployeeService→UserService/EventService calls |
| event | student, employee, finance | EventService→StudentService/EmployeeService/TransactionService service calls (no JPA @ManyToOne to Student/Employee) |
| finance | event, student, employee | JPQL JOIN to Event, FinanceService→StudentService/EmployeeService/EventService calls |
| dashboard | event | DashboardService→EventRepository access (aggregation queries) |
| address | (none) | Zero domain dependencies |

### Requirement: Infrastructure modules

The following SHALL NOT be Modulith modules but SHALL be shared infrastructure packages:

- `com.aprimorar.api.config`: Spring configuration (security, CORS, Jackson, clock, password encoder)
- `com.aprimorar.api.shared`: Base entity and utilities
- `com.aprimorar.api.enums`: Shared enumerations
- `com.aprimorar.api.exception`: Global exception handling

#### Scenario: Application module boot
- **GIVEN** the application startup
- **THEN** `ApiAprimorarApplication` SHALL be at `com.aprimorar.api`
- **AND** it SHALL NOT be part of any specific module
- **AND** all subpackages SHALL be scanned as modules by Spring Modulith

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

