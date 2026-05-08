## Why
O sistema atualmente utiliza uma autenticação stateful acoplada ao domínio de colaboradores, o que gera dependências cíclicas e impede a verificação estrita do Spring Modulith. Esta mudança visa remover completamente o módulo legado para permitir uma futura implementação de JWT stateless "tabula rasa".

## What Changes
- **BREAKING**: Remoção completa do pacote `com.aprimorar.api.domain.auth`.
- **BREAKING**: Remoção de todos os endpoints relacionados a login, logout e gerenciamento de usuários.
- Remoção da injeção de `UserService` no módulo `employee`.
- Limpeza do `SecurityConfig` para remover referências ao `AuthService` e `DaoAuthenticationProvider`.
- Desativação temporária da segurança (ou configuração para permitir tudo) até a nova implementação.

## Capabilities

### New Capabilities
<!-- Nenhuma nesta fase de remoção -->

### Modified Capabilities
- `employee-details-parallel-loading`: Remoção da necessidade de gerenciar o usuário vinculado ao colaborador.

## Impact
- **Banco de Dados**: A tabela `tb_users` deixará de ser utilizada/existir.
- **Frontend**: Todos os fluxos de login e autenticação deixarão de funcionar até o novo módulo JWT.
- **Arquitetura**: Resolução imediata dos ciclos `auth <-> employee` e `config -> auth`.
