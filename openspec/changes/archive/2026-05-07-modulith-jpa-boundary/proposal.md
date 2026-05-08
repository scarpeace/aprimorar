## Why

Após refatorar os atalhos service→repository, restam as dependências JPA diretas entre entidades de módulos diferentes: `Event→Student`, `Event→Employee`, `Student→Parent`, `User→Employee`. Essas referências criam acoplamento forte no modelo de dados — uma entidade importa a classe de outra entidade de outro módulo.

## What Changes

- Decidir abordagem para JPA cross-module: expor entidades como parte da API pública OU substituir referências por IDs (UUIDs)
- Se ID-based:
  - Substituir `@ManyToOne Student student` por `@Column UUID studentId` em Event
  - Substituir `@ManyToOne Employee employee` por `@Column UUID employeeId` em Event
  - Substituir `@ManyToOne Parent parent` por `@Column UUID parentId` em Student
  - Substituir `@OneToOne Employee employee` por `@Column UUID employeeId` em User
  - Refatorar JPQL queries que usam navegação de entidade (`event.student.name`) para usar joins explícitos com IDs
- Se exposição de entidades:
  - Mover classes de entidade para o pacote `api/` dos respectivos módulos
  - Declarar dependência `@ApplicationModule(allowedDependencies = {"student", "employee"})` nos módulos que referenciam entidades

## Capabilities

### New Capabilities
- Nenhuma

### Modified Capabilities
- `server-architecture`: Modificar requirement sobre JPA entity references (atualizar regras de boundary entre módulos)

## Impact

- Entidades em 4 módulos (event, student, user) podem ser alteradas
- JPQL queries em `TransactionRepository` e `EventSpecifications` precisam ser refatoradas se optar por ID-based
- Sem mudança de schema de banco (FKs continuam sendo UUIDs)
