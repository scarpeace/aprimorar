## Context

Com os módulos configurados e a verificação ativa, toda classe que não está em um pacote `api/` é inacessível para outros módulos (considerando verificação em modo estrito). Precisamos definir o que cada módulo expõe publicamente.

## Goals / Non-Goals

**Goals:**
- Definir API pública de cada módulo (o que vai em `api/`)
- Reorganizar classes existentes entre `api/` e `internal/`
- Atualizar todos os imports cross-module para apontar para `api/`

**Non-Goals:**
- Mudar interfaces ou contratos existentes (apenas mover de pacote)
- Resolver as dependências service→repository (fase 3)

## Decisions

### 1. Estrutura de cada módulo

```
com.aprimorar.api.<modulo>/
├── api/              # Público — visível para outros módulos
│   ├── <Modulo>Service.java       (interface pública)
│   ├── dto/                       (DTOs de request/response)
│   └── exception/                 (exceções que consumidores precisam capturar)
└── internal/         # Privado — invisível para outros módulos
    ├── <Modulo>Controller.java
    ├── <Modulo>ServiceImpl.java  (implementação da interface)
    ├── <Modulo>Mapper.java
    ├── <Modulo>Entity.java
    ├── <Modulo>Repository.java
    ├── <Modulo>Specifications.java
    └── dto/                       (DTOs internos, se houver)
```

### 2. API surface por módulo

| Módulo | API pública (`api/`) | Internal |
|--------|---------------------|----------|
| **auth** | `AuthService` (interface), `UserService` (interface), DTOs: `AuthLoginRequestDTO`, `AuthCurrentUserResponseDTO`, `UserCreateRequestDTO`, `UserResponseDTO`. Exceções: `UserNotFoundException`, `UserAlreadyExistsException` | `AuthController`, `UserController`, `User`, `UserRepository`, `AuthServiceImpl`, `UserServiceImpl` |
| **student** | `StudentService` (interface), DTOs: `StudentRequestDTO`, `StudentResponseDTO`, `StudentSummaryDTO`, `StudentByParentDTO`, `StudentOptionsDTO`, `StudentResponsibleSummaryDTO`. Exceções: `StudentNotFoundException`, `StudentAlreadyExistException`, `InvalidStudentException` | `StudentController`, `Student`, `StudentRepository`, `StudentSpecifications`, `StudentMapper`, `StudentServiceImpl` |
| **parent** | `ParentService` (interface), DTOs: `ParentRequestDTO`, `ParentResponseDTO`, `ParentOptionsDTO`. Exceções: `ParentNotFoundException`, `ParentAlreadyExistsException`, `ParentHasLinkedStudentsException`, `InvalidParentException` | `ParentController`, `Parent`, `ParentRepository`, `ParentSpecifications`, `ParentMapper`, `ParentServiceImpl` |
| **employee** | `EmployeeService` (interface), DTOs: `EmployeeRequestDTO`, `EmployeeResponseDTO`, `EmployeeSummaryDTO`, `EmployeeOptionsDTO`. Exceções: `EmployeeNotFoundException`, `EmployeeAlreadyExistsException`, `InvalidEmployeeException` | `EmployeeController`, `Employee`, `EmployeeRepository`, `EmployeeSpecifications`, `EmployeeMapper`, `EmployeeServiceImpl` |
| **event** | `EventService` (interface), DTOs: `EventRequestDTO`, `EventResponseDTO`. Exceções: `EventNotFoundException`, `InvalidEventException`, `EventScheduleConflictException`, `NotAllowedToUpdateEventException` | `EventController`, `Event`, `EventRepository`, `EventSpecifications`, `EventMapper`, `EventServiceImpl` |
| **finance** | `FinanceService`, `TransactionService`, DTOs: `TransactionRequestDTO`, `TransactionResponseDTO`, `FinanceSummaryDTO`. Exceções: `TransactionNotFoundException` | `FinanceController`, `Transaction`, `TransactionRepository`, `TransactionSpecifications` |
| **dashboard** | `DashboardService`, DTOs: `DashboardSummaryResponseDTO`. Exceções: `InvalidDashboardRequestException` | `DashboardController` |
| **address** | `Address`, `AddressMapper`, DTOs: `AddressRequestDTO`, `AddressResponseDTO`. Exceções: `InvalidAddressException` | (Embeddable — sem internal) |

### 3. Services viram interface + implementação

**Rationale:** A interface fica em `api/` (visível), a implementação em `internal/` (invisível). Isso permite que o módulo mude a implementação sem afetar consumidores.

**Casos especiais:**
- `FinanceService` e `TransactionService` já são classes concretas — viram interfaces também
- `DashboardService` é simples — interface + impl pode ser over-engineering, mas mantém consistência

### 4. DTOs do módulo address são expostos

**Rationale:** Student module depende de `AddressRequestDTO` e `AddressResponseDTO` nos seus DTOs. Como `student` depende de `address`, esses DTOs precisam estar na API pública de address.

## Risks / Trade-offs

- [Mover classes quebra referências no git blame] → Aceitar como custo da migração. Manter histórico no change.
- [Interface + Impl adiciona boilerplate] → Necessário para o Modulith funcionar com verificação. Aceitar como custo da arquitetura modular.
- [Controllers em internal, mas endpoints expostos via HTTP] → Controladores são detalhes de transporte, não API de domínio. O contrato REST está na interface (via DTOs), não no controller.
