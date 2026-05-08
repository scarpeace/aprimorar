## Context

Quatro referências JPA cruzam boundaries de módulo:
1. `Event.@ManyToOne → Student` (event → student)
2. `Event.@ManyToOne → Employee` (event → employee)
3. `Student.@ManyToOne → Parent` (student → parent)
4. `User.@OneToOne → Employee` (auth → employee)

Além disso, `TransactionRepository` faz JPQL JOIN referenciando a entidade `Event`.

## Goals / Non-Goals

**Goals:**
- Eliminar importação direta de entidades entre módulos
- Manter integridade referencial no banco (FKs continuam existindo)
- Refatorar JPQL queries que navegam por associations cross-module

**Non-Goals:**
- Mudar schema do banco (as FKs já são UUIDs)
- Introduzir DDD agregados ou bounded contexts completos (escopo futuro)

## Decisions

### 1. Abordagem: ID-based references

Substituir referências diretas a entidades por colunas UUID.

**Rationale:** É a abordagem mais pura para Modulith — cada módulo é dono do seu modelo de dados e não precisa conhecer as entidades de outros módulos. As FKs já são UUIDs no banco, então o schema não muda.

**Alternatives considered:**
- Expor entidades via `api/` package: mais rápido, mas entidades são detalhes de implementação (JPA annotations, lazy loading, etc.).
- Módulo compartilhado de entidades: derrota o propósito da modularização.

### 2. Mudanças nas entidades

**Event.java (módulo event):**
```
// ANTES:
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "student_id")
private Student student;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "employee_id")
private Employee employee;

// DEPOIS:
@Column(name = "student_id")
private UUID studentId;

@Column(name = "employee_id")
private UUID employeeId;
```

**Student.java (módulo student):**
```
// ANTES:
@ManyToOne(cascade = CascadeType.PERSIST)
@JoinColumn(name = "parent_id")
private Parent parent;

// DEPOIS:
@Column(name = "parent_id")
private UUID parentId;
```

**User.java (módulo auth):**
```
// ANTES:
@OneToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "employee_id")
private Employee employee;

// DEPOIS:
@Column(name = "employee_id", nullable = false)
private UUID employeeId;
```

**Address.java (módulo address):**
- Mantido como `@Embedded` em Student. Address é um value object, não uma entidade, e está no mesmo módulo conceitual. A dependência JPA de Student → Address é aceitável (Address não tem identidade própria).

### 3. Refatoração de JPQL queries

**TransactionRepository:** Substituir JOIN navegacional por JOIN explícito com IDs:
```
// ANTES:
"SELECT t FROM Transaction t JOIN Event e ON e.id = t.originId WHERE e.student.id = :studentId"

// DEPOIS:
"SELECT t FROM Transaction t WHERE t.originId IN (SELECT e.id FROM Event e WHERE e.studentId = :studentId)"
```

Ou usar query separada no EventService:
```
eventService.getEventIdsByStudentId(studentId)
→ transactionRepository.findByOriginIdIn(eventIds)
```

**EventSpecifications:** Qualquer spec que use navegação para Student ou Employee precisa ser refatorada. Substituir:
```
// ANTES:
(root, query, cb) -> cb.equal(root.get("student").get("id"), studentId)

// DEPOIS:
(root, query, cb) -> cb.equal(root.get("studentId"), studentId)
```

### 4. Services precisam fazer lookup explícito

Onde antes o código navegava `event.getStudent().getName()`, agora precisa fazer:
```java
StudentResponseDTO student = studentService.findById(event.getStudentId());
```

Isso já está parcialmente resolvido pela Fase 3. A diferença é que agora o `Event` não tem mais o campo `student` — apenas `studentId`. Qualquer código que usava `event.getStudent()` vai quebrar e precisa ser atualizado.

## Risks / Trade-offs

- [Perda de navegabilidade JPA] → Não é possível mais fazer `event.getStudent()` sem chamar o service. Isso é intencional — força o uso da API pública.
- [N+1 queries] → Antes o Hibernate fazia fetch lazy. Agora chamadas explícitas ao service podem gerar mais queries se não houver batch. Monitorar e ajustar com `findAllById` ou `@EntityGraph` onde necessário.
- [Ghost records] → Estudantes/employees deletados viram "ghosts" (registros com `archivedAt`). O service precisa tratar isso nos lookups.
- [Código existente que usa navegação] → Precisa ser encontrado via grep e refatorado. Escopo: services, mappers, e qualquer lógica que acesse `event.getStudent()` ou similar.
