## Why

Atualmente 12 dos 14 acessos cross-module bypassam a camada de serviço — um módulo injecta diretamente o repositório de outro. Isso fere o encapsulamento: o módulo dono do dado não controla como ele é acessado, e mudanças internas (ex: renomear uma coluna) quebram consumidores distantes.

## What Changes

- Adicionar métodos públicos nos services de cada módulo para expor as operações necessárias
- Refatorar cada um dos 12 atalhos service→repository para service→service

Métodos a adicionar:

| Módulo | Novo método público | Consumido por |
|--------|-------------------|---------------|
| employee | `existsById(id)`, `getReferenceById(id)` | UserService, EventService, FinanceService |
| parent | `findById(id)`, `existsById(id)` | StudentService |
| student | `existsById(id)`, `hasActiveLinkedStudents(id)` | ParentService, FinanceService |
| event | `countByStudentId(id)`, `countByEmployeeId(id)`, `sumChargedByStudentId(id)`, `sumPaidByEmployeeId(id)`, `reassignStudentEventsToGhost(id)`, `reassignEmployeeEventsToGhost(id)` | StudentService, EmployeeService, FinanceService |
| user | `deleteByEmployeeId(id)`, `findByEmployeeId(id)` | EmployeeService |

- Atualizar todos os consumidores para usar os services ao invés dos repositórios diretamente

## Capabilities

### New Capabilities
- Nenhuma

### Modified Capabilities
- `server-architecture`: Modificar requirement sobre cross-module dependency rules (remover os atalhos, atualizar grafo de dependências)

## Impact

- 6 services ganham novos métodos públicos
- 12 arquivos de consumidores atualizados
- O grafo de dependências cross-module passa de 14 arestas para 2 (service-to-service legítimos + dependências JPA)
