## Context

12 atalhos service→repository atravessam boundaries de módulo. Cada um precisa ser convertido para uma chamada service→service através da API pública dos módulos.

## Goals / Non-Goals

**Goals:**
- Adicionar métodos públicos nos services necessários
- Refatorar todos os 12 consumidores para usar services
- Verificar que violações de módulo diminuíram correspondentemente

**Non-Goals:**
- Mudar lógica de negócio (apenas redirecionar chamadas)
- Remover entidades JPA cross-module (fase 4)

## Decisions

### 1. Novos métodos públicos

Para cada novo método, seguir o padrão: delegar ao repositório internamente, expor via interface do service.

**auth module (`UserService`):**
- `deleteByEmployeeId(UUID employeeId)`: Deleta user vinculado a um employee (usado por `EmployeeService`)
- `findByEmployeeId(UUID employeeId): UserResponseDTO`: Busca user por employee (usado por `EmployeeService`)

**student module (`StudentService`):**
- `existsById(UUID id): boolean`: Verifica se student existe (usado por `ParentService`, `FinanceService`)
- `hasActiveLinkedStudents(UUID parentId): boolean`: Verifica se parent tem students ativos (usado por `ParentService`)

**parent module (`ParentService`):**
- `findById(UUID id): ParentResponseDTO`: Busca parent por ID (usado por `StudentService`)

**employee module (`EmployeeService`):**
- `existsById(UUID id): boolean`: Verifica se employee existe (usado por `UserService`, `EventService`, `FinanceService`)
- `getReferenceById(UUID id): EmployeeResponseDTO`: Busca employee por ID (usado por `EventService`)

**event module (`EventService`):**
- `countByStudentId(UUID studentId): long`: Total de eventos de um student (usado por `StudentService`, `FinanceService`)
- `countByEmployeeId(UUID employeeId): long`: Total de eventos de um employee (usado por `EmployeeService`, `FinanceService`)
- `sumChargedByStudentId(UUID studentId): BigDecimal`: Soma de cobranças de um student (usado por `StudentService`)
- `sumPaidByEmployeeId(UUID employeeId): BigDecimal`: Soma de pagamentos de um employee (usado por `EmployeeService`)
- `reassignStudentEventsToGhost(UUID studentId)`: Reatribui eventos de um student para o ghost (usado por `StudentService`)
- `reassignEmployeeEventsToGhost(UUID employeeId)`: Reatribui eventos de um employee para o ghost (usado por `EmployeeService`)

### 2. Refatoração dos consumidores

| # | Consumidor (antes) | Repositório acessado | Novo chamador |
|---|-------------------|---------------------|---------------|
| 1 | `UserService` | `EmployeeRepository` | `EmployeeService.getReferenceById()` |
| 2 | `StudentService` | `ParentRepository` | `ParentService.findById()` |
| 3 | `StudentService` | `EventRepository` | `EventService.{countByStudentId,sumChargedByStudentId,reassignStudentEventsToGhost}()` |
| 4 | `ParentService` | `StudentRepository` | `StudentService.hasActiveLinkedStudents()` |
| 5 | `EmployeeService` | `UserRepository` | `UserService.{findByEmployeeId,deleteByEmployeeId}()` |
| 6 | `EmployeeService` | `EventRepository` | `EventService.{countByEmployeeId,sumPaidByEmployeeId,reassignEmployeeEventsToGhost}()` |
| 7 | `EventService` | `StudentRepository` | `StudentService.existsById()` |
| 8 | `EventService` | `EmployeeRepository` | `EmployeeService.getReferenceById()` |
| 9 | `FinanceService` | `StudentRepository` | `StudentService.existsById()` |
| 10 | `FinanceService` | `EmployeeRepository` | `EmployeeService.existsById()` |
| 11 | `FinanceService` | `EventRepository` | `EventService.{countByStudentId,countByEmployeeId}()` |
| 12 | `TransactionRepository` | `Event` (JPQL JOIN) | Mantido — resolvido na fase 4 |

### 3. TransactionRepository (JPQL) — caso especial

O `TransactionRepository` faz JPQL JOIN com a entidade `Event` do módulo event. Isso não é um atalho service→repository, mas sim uma dependência JPA em nível de query. Será tratado na fase 4 (JPA Boundary).

## Risks / Trade-offs

- [Acoplamento circular potencial] → Verificar se algum novo método cria ciclo. Atualmente não há ciclos no grafo.
- [Performance] → Adicionar uma camada de service call (vs repository direto) pode adicionar latência. A diferença é irrelevante (mesma JVM, chamada de método).
- [Testes existentes] → Testes que mockam repositórios diretamente precisam ser atualizados para mockar services.
