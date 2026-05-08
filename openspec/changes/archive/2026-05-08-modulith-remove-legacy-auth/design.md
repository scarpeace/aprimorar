## Context
O sistema possui um módulo `auth` legado que utiliza sessões stateful e está fortemente acoplado ao domínio de colaboradores, gerando ciclos de dependência que impedem a verificação estrita do Spring Modulith.

## Goals / Non-Goals

**Goals:**
- Eliminar completamente o pacote `com.aprimorar.api.domain.auth`.
- Resolver os ciclos de arquitetura `auth <-> employee` e `config -> auth`.
- Preparar o terreno para uma implementação futura de JWT.
- Manter o sistema funcional (mesmo que temporariamente inseguro) para desenvolvimento.

**Non-Goals:**
- Implementar JWT ou qualquer novo sistema de autenticação nesta fase.
- Refatorar outros módulos além da remoção de referências ao `auth`.

## Decisions

- **Remoção de Código**: Deletar fisicamente o diretório `server/src/main/java/com/aprimorar/api/domain/auth/`.
- **Desacoplamento do Employee**: 
    - Remover `UserService` e `UserDTO` de `EmployeeServiceImpl` e `EmployeeDetailsDTO`.
    - Remover a lógica de deleção de usuário em cascata no `EmployeeService`.
- **Configuração de Segurança**: 
    - Simplificar `SecurityConfig` para desativar CSRF (temporariamente para dev) e permitir acesso total (`permitAll()`) a todos os endpoints.
    - Remover referências ao `UserDetailsService` e `AuthenticationProvider` legados.

## Risks / Trade-offs

- **[Risk] Sistema Inseguro** → Mitigation: Esta é uma fase intermediária e puramente de desenvolvimento local/migração. A segurança será reimplementada com JWT no próximo ciclo.
- **[Risk] Quebra do Frontend** → Mitigation: O frontend precisará ser ajustado para não tentar realizar login ou passar headers de sessão até que o novo sistema esteja pronto.
