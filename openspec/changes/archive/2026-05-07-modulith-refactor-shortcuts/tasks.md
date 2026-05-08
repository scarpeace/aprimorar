## 1. auth module — novos métodos no UserService

- [x] 1.1 Adicionar `deleteByEmployeeId(UUID employeeId)` à interface `UserService` e implementar em `UserServiceImpl` (delega para `UserRepository`)
- [x] 1.2 Adicionar `findByEmployeeId(UUID employeeId): UserResponseDTO` à interface e implementar
- [x] 1.3 Refatorar `EmployeeService` para usar `UserService.findByEmployeeId()` e `UserService.deleteByEmployeeId()` ao invés de `UserRepository` diretamente

## 2. student module — novos métodos no StudentService

- [x] 2.1 Adicionar `existsById(UUID id): boolean` ao `StudentService` e implementar
- [x] 2.2 Adicionar `hasActiveLinkedStudents(UUID parentId): boolean` ao `StudentService` e implementar
- [x] 2.3 Refatorar `ParentService` para usar `StudentService.hasActiveLinkedStudents()` ao invés de `StudentRepository` diretamente
- [x] 2.4 Refatorar `FinanceService` para usar `StudentService.existsById()` ao invés de `StudentRepository` diretamente

## 3. parent module — novos métodos no ParentService

- [x] 3.1 Adicionar `findById(UUID id): ParentResponseDTO` ao `ParentService` e implementar
- [x] 3.2 Refatorar `StudentService` para usar `ParentService.findById()` ao invés de `ParentRepository` diretamente

## 4. employee module — novos métodos no EmployeeService

- [x] 4.1 Adicionar `existsById(UUID id): boolean` ao `EmployeeService` e implementar
- [x] 4.2 Adicionar `getReferenceById(UUID id): EmployeeResponseDTO` ao `EmployeeService` e implementar
- [x] 4.3 Refatorar `UserService` para usar `EmployeeService.getReferenceById()` ao invés de `EmployeeRepository` diretamente
- [x] 4.4 Refatorar `EventService` para usar `EmployeeService.getReferenceById()` e `EmployeeService.existsById()` ao invés de `EmployeeRepository` diretamente
- [x] 4.5 Refatorar `FinanceService` para usar `EmployeeService.existsById()` ao invés de `EmployeeRepository` diretamente

## 5. event module — novos métodos no EventService

- [x] 5.1 Adicionar `countByStudentId(UUID studentId): long` ao `EventService` e implementar
- [x] 5.2 Adicionar `countByEmployeeId(UUID employeeId): long` ao `EventService` e implementar
- [x] 5.3 Adicionar `sumChargedByStudentId(UUID studentId): BigDecimal` ao `EventService` e implementar
- [x] 5.4 Adicionar `sumPaidByEmployeeId(UUID employeeId): BigDecimal` ao `EventService` e implementar
- [x] 5.5 Adicionar `reassignStudentEventsToGhost(UUID studentId)` ao `EventService` e implementar
- [x] 5.6 Adicionar `reassignEmployeeEventsToGhost(UUID employeeId)` ao `EventService` e implementar
- [x] 5.7 Refatorar `StudentService` para usar os novos métodos do `EventService` ao invés de `EventRepository`
- [x] 5.8 Refatorar `EmployeeService` para usar os novos métodos do `EventService` ao invés de `EventRepository`
- [x] 5.9 Refatorar `FinanceService` para usar os novos métodos do `EventService` ao invés de `EventRepository`
- [x] 5.10 Refatorar `EventService` para usar `StudentService.existsById()` e `EmployeeService.getReferenceById()` ao invés dos repositórios diretamente

## 6. Verificação

- [x] 6.1 Rodar `ModuleVerificationTest` para confirmar que violações diminuíram (esperado: zero violações service→repository)
- [x] 6.2 Rodar `./mvnw test` para garantir testes existentes
- [x] 6.3 Atualizar `allowedDependencies` nos package-info onde as dependências foram resolvidas
