## 1. Decidir abordagem e preparar

- [x] 1.1 Decidir entre expor entidades vs ID-based (ver design.md para trade-offs)
- [x] 1.2 Mapear TODO código que usa navegação de entidade cross-module (`event.getStudent()`, `event.getEmployee()`, `student.getParent()`, `user.getEmployee()`)
- [x] 1.3 Mapear todas as JPQL queries que cruzam módulos

## 2. Event module — remover dependências JPA

- [x] 2.1 Substituir `@ManyToOne Student student` por `@Column UUID studentId` em `Event`
- [x] 2.2 Substituir `@ManyToOne Employee employee` por `@Column UUID employeeId` em `Event`
- [x] 2.3 Atualizar `EventService` para usar `StudentService` e `EmployeeService` onde antes usava navegação de entidade
- [x] 2.4 Atualizar `EventMapper` se usa navegação de entidade
- [x] 2.5 Refatorar `EventSpecifications` para usar studentId/employeeId diretamente (não `root.get("student").get("id")`)

## 3. Student module — remover dependência JPA

- [x] 3.1 Substituir `@ManyToOne Parent parent` por `@Column UUID parentId` em `Student`
- [x] 3.2 Atualizar `StudentService` para usar `ParentService` onde antes usava navegação
- [x] 3.3 Atualizar `StudentMapper` se usa navegação

## 4. Auth module — remover dependência JPA

- [x] 4.1 Substituir `@OneToOne Employee employee` por `@Column UUID employeeId` em `User`
- [x] 4.2 Atualizar `UserService` para usar `EmployeeService` onde antes usava navegação

## 5. Refatorar JPQL queries

- [x] 5.1 `TransactionRepository`: substituir JOIN navegacional (`e.student.id`) por JOIN com ID explícito (`e.studentId`)
- [x] 5.2 Qualquer outra query que use navegação cross-module: refatorar

## 6. Verificação

- [x] 6.1 Rodar `ModuleVerificationTest` e confirmar que as violações JPA foram resolvidas
- [ ] 6.2 Rodar `./mvnw test` para garantir testes existentes
- [x] 6.3 Atualizar `allowedDependencies` nos package-info (auth pode remover employee, event pode remover student/employee, student pode remover parent)
- [x] 6.4 Verificar performance (N+1 queries) e adicionar batch fetching se necessário
