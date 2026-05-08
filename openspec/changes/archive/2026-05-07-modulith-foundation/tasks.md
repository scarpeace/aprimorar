## 1. Dependências e configuração inicial

- [x] 1.1 Adicionar `spring-modulith-starter-core` e `spring-modulith-starter-test` ao `pom.xml`
- [x] 1.2 Verificar se o `pom.xml` compila com as novas dependências (`./mvnw clean compile`)

## 2. package-info.java por módulo

- [x] 2.1 auth: criar `package-info.java` com `@ApplicationModule(displayName = "Auth", allowedDependencies = {"employee"})`
- [x] 2.2 student: criar `package-info.java` com `@ApplicationModule(displayName = "Students", allowedDependencies = {"address", "parent", "event"})`
- [x] 2.3 parent: criar `package-info.java` com `@ApplicationModule(displayName = "Parents", allowedDependencies = {"student"})`
- [x] 2.4 employee: criar `package-info.java` com `@ApplicationModule(displayName = "Employees", allowedDependencies = {"auth", "event"})`
- [x] 2.5 event: criar `package-info.java` com `@ApplicationModule(displayName = "Events", allowedDependencies = {"student", "employee", "finance"})`
- [x] 2.6 finance: criar `package-info.java` com `@ApplicationModule(displayName = "Finance", allowedDependencies = {"event", "student", "employee"})`
- [x] 2.7 dashboard: criar `package-info.java` com `@ApplicationModule(displayName = "Dashboard", allowedDependencies = {"event"})`
- [x] 2.8 address: criar `package-info.java` com `@ApplicationModule(displayName = "Address")` (sem dependências)

## 3. Shared packages e verificação

- [x] 3.1 Configurar `com.aprimorar.api.shared`, `com.aprimorar.api.enums`, `com.aprimorar.api.exception`, `com.aprimorar.api.config` como shared packages na verificação Modulith
- [x] 3.2 Criar teste `ModuleVerificationTest` com `@ApplicationModuleTest` e método `verifyModuleStructure()`
- [x] 3.3 Rodar a verificação e registrar TODAS as violações encontradas
- [x] 3.4 Ajustar `allowedDependencies` se alguma dependência não mapeada for descoberta

## 4. CI e validação

- [x] 4.1 Garantir que `ModuleVerificationTest` roda como parte do `./mvnw test`
- [ ] 4.2 Verificar que `./mvnw test` passa com as configurações atuais

## Violações registradas na primeira verificação

- Ciclo `auth -> employee -> auth`: `User` referencia `Employee`; `UserService` usa `EmployeeRepository`; `EmployeeService` usa `UserRepository`.
- Ciclo `employee -> event -> employee`: `EmployeeService` usa `EventRepository`; `Event` e `EventService` referenciam `Employee` e `EmployeeRepository`.
- Ciclo `employee -> event -> finance -> employee`: `EventService` usa `TransactionService`; `FinanceService` usa `EmployeeRepository` e DTOs de employee.
- Ciclo `event -> finance -> event`: `EventService` usa `TransactionService`; `FinanceService` e `TransactionRepository` dependem de `EventRepository`/`Event`.
- Ciclo `event -> student -> event`: `Event` e `EventService` referenciam `Student`/`StudentRepository`; `StudentService` usa `EventRepository`.
- Ciclo `parent -> student -> parent`: `ParentService` usa `StudentRepository`; `Student` e `StudentService` referenciam `Parent`/`ParentRepository`.
- Dependencia `config -> auth`: `SecurityConfig` referencia `AuthService`.
- Dependencias `exception -> address/auth/employee/event/finance/parent/student`: `GlobalExceptionHandler` referencia exceptions especificas dos modulos.
