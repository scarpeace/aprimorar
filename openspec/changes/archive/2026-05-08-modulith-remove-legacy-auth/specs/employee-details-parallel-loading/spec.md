## REMOVED Requirements

### Requirement: Vincular Usuário ao Colaborador
O sistema SHALL permitir que um colaborador tenha um usuário associado para acesso ao sistema.

**Reason**: A autenticação está sendo movida para um modelo independente (stateless JWT) e o acoplamento atual gera ciclos de arquitetura.
**Migration**: O acesso ao sistema será desvinculado da entidade de colaborador. O `EmployeeDetailsDTO` não incluirá mais informações de conta de usuário.
