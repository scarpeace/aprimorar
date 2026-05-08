## ADDED Requirements

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

The allowed dependencies for each module (initial state, pre-modulith) SHALL be:

| Module | Allowed Dependencies | Rationale |
|--------|---------------------|-----------|
| auth | employee | JPA @OneToOne to Employee entity |
| student | address, parent, event | @Embedded Address, @ManyToOne Parent, direct EventRepository access |
| parent | student | Direct StudentRepository access |
| employee | auth, event | Direct UserRepository and EventRepository access |
| event | student, employee, finance | @ManyToOne to Student/Employee, service-to-service to TransactionService |
| finance | event, student, employee | JPQL JOIN to Event, direct StudentRepository/EmployeeRepository/EventRepository access |
| dashboard | event | Direct EventRepository access for aggregation queries |
| address | (none) | Zero domain dependencies |

#### Scenario: Temporary allowed dependencies
- **GIVEN** a module with more `allowedDependencies` than its final target
- **THEN** the excess dependencies SHALL be marked with a comment indicating they are temporary and which phase resolves them
- **AND** they SHALL be removed when the corresponding phase is implemented

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
