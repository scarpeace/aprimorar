## 1. Criação da Estrutura de Pacotes

- [x] 1.1 Criar diretório `server/src/main/java/com/aprimorar/api/domain/registration/`.
- [x] 1.2 Criar `package-info.java` no pacote `registration` com a anotação `@ApplicationModule`.

## 2. Migração do Domínio Registration

- [x] 2.1 Mover pacote `address` para `domain/registration/address` e atualizar pacotes/imports.
- [x] 2.2 Mover pacote `parent` para `domain/registration/parent` e atualizar pacotes/imports.
- [x] 2.3 Mover pacote `employee` para `domain/registration/employee` e atualizar pacotes/imports.
- [x] 2.4 Mover pacote `student` para `domain/registration/student` e atualizar pacotes/imports.

## 3. Refatoração de Relacionamentos e Código

- [x] 3.1 Alterar entidade `Student` para utilizar `@ManyToOne Parent parent` em vez de `UUID parentId`.
- [x] 3.2 Atualizar `StudentMapper`, `StudentServiceImpl` e `StudentTest` para lidar com a nova referência de objeto.
- [x] 3.3 Remover injeção de `ParentService` em `StudentServiceImpl` onde a navegação direta de objeto for suficiente.
- [x] 3.4 Corrigir todos os erros de compilação em `events` e `finance` decorrentes da mudança de pacotes.

## 4. Limpeza e Verificação

- [x] 4.1 Deletar diretórios de domínios antigos que ficaram vazios.
- [ ] 4.2 Executar `ModuleVerificationTest` para validar a nova arquitetura sem ciclos.
- [ ] 4.3 Rodar testes de fumaça (smoke tests) nas APIs de cadastro.
