## MODIFIED Requirements

### Requirement: Module auth

#### Scenario: ID-based column reference to employee module
- **GIVEN** the `User` entity
- **THEN** it SHALL have a `@Column(name = "employee_id", nullable = false, unique = true)` of type `UUID` referencing the employee module by ID
- **AND** it SHALL NOT have a `@OneToOne` JPA relationship to `Employee`

### Requirement: Module student

#### Scenario: ID-based column reference to parent module
- **GIVEN** the `Student` entity
- **THEN** it SHALL have a `@Column(name = "parent_id", nullable = false)` of type `UUID` referencing the parent module by ID
- **AND** it SHALL NOT have a `@ManyToOne` JPA relationship to `Parent`

### Requirement: Module event

#### Scenario: ID-based column references
- **GIVEN** the `Event` entity
- **THEN** it SHALL have a `@Column(name = "student_id", nullable = false)` of type `UUID` referencing the student module by ID
- **AND** it SHALL have a `@Column(name = "employee_id", nullable = false)` of type `UUID` referencing the employee module by ID
- **AND** it SHALL NOT have `@ManyToOne` JPA relationships to `Student` or `Employee`

## MODIFIED Requirements (Cross-Module Rules)

### Requirement: Cross-module rules

#### Scenario: JPA entity references across modules
- **GIVEN** entities in different modules with relationships
- **THEN** they SHALL use ID-based columns (`@Column UUID`) instead of direct JPA annotations (`@ManyToOne`, `@OneToOne`)
- **AND** cross-entity lookups SHALL be done via service calls (not JPA relationship traversal)

## MODIFIED Requirements (Module Dependency Registry)

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
