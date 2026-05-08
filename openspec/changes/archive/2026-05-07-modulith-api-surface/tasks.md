## 1. auth module — API surface

- [x] 1.1 Criar pacote `com.aprimorar.api.domain.auth.api` com `AuthService` (interface movida), `UserService` (interface movida), DTOs e exceções públicas
- [x] 1.2 Criar `AuthServiceImpl` e `UserServiceImpl` em `internal/` implementando as interfaces
- [x] 1.3 Mover `AuthController`, `UserController`, `User`, `UserRepository` para `internal/`
- [x] 1.4 Atualizar todos os imports no módulo auth e em consumidores de auth

## 2. student module — API surface

- [x] 2.1 Criar pacote `com.aprimorar.api.domain.student.api` com `StudentService` (interface), DTOs e exceções públicas
- [x] 2.2 Criar `StudentServiceImpl` em `internal/`
- [x] 2.3 Mover `StudentController`, `Student`, `StudentRepository`, `StudentSpecifications`, `StudentMapper` para `internal/`
- [x] 2.4 Atualizar imports

## 3. parent module — API surface

- [x] 3.1 Criar `com.aprimorar.api.domain.parent.api` com `ParentService` (interface), DTOs, exceções
- [x] 3.2 Criar `ParentServiceImpl` em `internal/`
- [x] 3.3 Mover controller, entity, repository, specifications, mapper para `internal/`
- [x] 3.4 Atualizar imports

## 4. employee module — API surface

- [x] 4.1 Criar `com.aprimorar.api.domain.employee.api` com `EmployeeService` (interface), DTOs, exceções
- [x] 4.2 Criar `EmployeeServiceImpl` em `internal/`
- [x] 4.3 Mover controller, entity, repository, specifications, mapper para `internal/`
- [x] 4.4 Atualizar imports

## 5. event module — API surface

- [x] 5.1 Criar `com.aprimorar.api.domain.event.api` com `EventService` (interface), DTOs, exceções
- [x] 5.2 Criar `EventServiceImpl` em `internal/`
- [x] 5.3 Mover controller, entity, repository, specifications, mapper para `internal/`
- [x] 5.4 Atualizar imports

## 6. finance module — API surface

- [x] 6.1 Criar `com.aprimorar.api.domain.finance.api` com `FinanceService` (interface), `TransactionService` (interface), DTOs, exceções
- [x] 6.2 Criar `FinanceServiceImpl` e `TransactionServiceImpl` em `internal/`
- [x] 6.3 Mover controller, entity, repository, specifications para `internal/`
- [x] 6.4 Atualizar imports

## 7. dashboard e address — API surface

- [x] 7.1 dashboard: criar `api/` com `DashboardService` (interface), DTOs, exceção; mover controller para `internal/`
- [x] 7.2 address: criar `api/` com `Address`, `AddressMapper`, DTOs, exceção (não tem internal — é embeddable)
- [x] 7.3 Atualizar imports

## 8. Verificação

- [x] 8.1 Rodar `ModuleVerificationTest` para confirmar que violações diminuíram
- [ ] 8.2 Rodar `./mvnw test` para garantir que testes existentes continuam passando
- [x] 8.3 Atualizar `allowedDependencies` nos package-info se necessário (dependências que sumiram com a reorganização)
