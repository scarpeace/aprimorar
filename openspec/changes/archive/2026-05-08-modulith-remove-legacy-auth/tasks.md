## 1. Remoção do Módulo Auth

- [x] 1.1 Deletar o diretório `server/src/main/java/com/aprimorar/api/domain/auth/` e todos os seus arquivos.
- [x] 1.2 Remover a declaração do módulo `auth` no `package-info.java` raiz ou onde houver referências ao `@ApplicationModule` de Auth.

## 2. Desacoplamento do Módulo Employee

- [x] 2.1 Remover a injeção do `UserService` e o campo `UserDTO` em `EmployeeServiceImpl.java`.
- [x] 2.2 Ajustar o `EmployeeDetailsDTO` para remover campos relacionados à conta de usuário.
- [x] 2.3 Remover a lógica de deleção síncrona de usuário no `deleteEmployee` do `EmployeeService`.
- [x] 2.4 Limpar imports e dependências não utilizadas relacionadas ao `auth` em todo o módulo `employee`.

## 3. Ajustes de Segurança e Infraestrutura

- [x] 3.1 Modificar `SecurityConfig.java` para remover o `UserDetailsService`, `PasswordEncoder` (se não for usado em outro lugar) e `DaoAuthenticationProvider`.
- [x] 3.2 Configurar o `SecurityFilterChain` para `permitAll()` em todas as rotas e desativar CSRF temporariamente.
- [x] 3.3 Verificar e remover referências ao módulo `auth` no `GlobalExceptionHandler`.

## 4. Banco de Dados e Limpeza Final

- [x] 4.1 Remover/Comentar o código da entidade `User` para garantir que a tabela `tb_users` não seja mais gerenciada pelo Hibernate (se houver ddl-auto ativo).
- [x] 4.2 Executar testes de fumaça (smoke tests) para garantir que o sistema de colaboradores e eventos continua funcionando sem autenticação.
