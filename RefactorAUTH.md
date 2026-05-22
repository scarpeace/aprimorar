# Refactor AUTH

Este arquivo rastreia ajustes de autenticacao/autorizacao identificados no code review. As tarefas devem ser marcadas com `[X]` conforme forem implementadas.

## Decisoes

- Usuario arquivado nao consegue fazer novo login.
- Tokens ja emitidos continuam validos ate expirarem; nao vamos revogar sessoes ativas imediatamente nesta fase.
- Deve existir um unico usuario `ADMIN`.
- O usuario `ADMIN` unico nao pode ser deletado, arquivado ou modificado.

## TODOs

### Alto Impacto / Baixa Complexidade

- [X] Corrigir login com token antigo no frontend.
  - Problema: `client/src/lib/shared/api.ts` injeta `Authorization` tambem em `POST /v1/auth/login`.
  - Efeito: login pode retornar `401` se houver token expirado/invalido salvo em `localStorage`.
  - Caminho: `client/src/lib/shared/api.ts`.
  - Acao: nao injetar Bearer quando `config.url` for `/v1/auth/login`, ou criar um axios client sem auth para login.

- [X] Ignorar tratamento global de `401` na rota de login.
  - Problema: credenciais invalidas disparam mensagem de "Sessao expirada" e redirect global.
  - Efeito: UX confusa e possivel toast duplicado.
  - Caminho: `client/src/lib/shared/api.ts`.
  - Acao: se `error.config.url` for `/v1/auth/login`, deixar o erro subir para o `AuthProvider` tratar como credenciais invalidas.

- [ ] Limitar criacao de usuario a roles `EMPLOYEE` e `ADMIN`.
  - Problema: `UserRequestDto.role` usa `Role`, que tambem aceita `STUDENT` e `PARENT`.
  - Efeito: roles fora do escopo inicial podem autenticar e acessar endpoints autenticados.
  - Caminhos: `server/src/main/java/aprimorar/auth/api/dto/UserRequestDto.java`, `server/src/main/java/aprimorar/auth/internal/UserService.java`.
  - Acao: validar role no service ou criar uma enum/request especifica para auth.

- [ ] Adicionar validacao de request em criacao de usuario.
  - Problema: `UserRequestDto` nao tem `@NotBlank`, `@Email`, `@NotNull`; controller nao usa `@Valid`.
  - Efeito: input invalido pode virar `IllegalArgumentException` e resposta `500`.
  - Caminhos: `server/src/main/java/aprimorar/auth/api/dto/UserRequestDto.java`, `server/src/main/java/aprimorar/auth/internal/UserController.java`.
  - Acao: adicionar Bean Validation e `@Valid` em `createUser`.

### Alto Impacto / Media Complexidade

- [ ] Impedir modificacao do admin unico.
  - Decisao: o unico `ADMIN` nao pode ser deletado, arquivado ou modificado.
  - Problema atual: `deleteUser` e `toggleActive` nao protegem o admin.
  - Caminho: `server/src/main/java/aprimorar/auth/internal/UserService.java`.
  - Acao: bloquear operacoes em usuario com `role == ADMIN`; se update voltar no futuro, tambem bloquear update.
  - Criterio: `DELETE /v1/users/{adminId}` e `PATCH /v1/users/{adminId}/archive` retornam erro controlado.

- [ ] Garantir que exista somente um admin.
  - Decisao: deve existir um unico `ADMIN`.
  - Problema atual: `createUser` permite criar outro `ADMIN`.
  - Caminhos: `server/src/main/java/aprimorar/auth/internal/UserService.java`, possivel migration/constraint em `server/src/main/resources/db/migration/`.
  - Acao minima: bloquear criacao de `ADMIN` se ja existir algum usuario admin.
  - Acao robusta: adicionar constraint/indice parcial no banco para impedir mais de um admin.

- [ ] Definir erro de negocio para operacoes proibidas em admin.
  - Problema: `ResponseStatusException` direto dificulta padronizacao de respostas.
  - Caminho: `server/src/main/java/aprimorar/auth/api/exception/` e handler compartilhado ou de auth.
  - Acao: criar excecoes como `AdminUserCannotBeChangedException` e mapear status adequado (`409` ou `422`).

### Medio Impacto / Baixa Complexidade

- [ ] Remover `baseURL` hardcoded dos hooks gerados pelo Kubb.
  - Problema: `client/kubb.config.ts` define `client.baseURL: 'http://localhost:8080'`, e os hooks gerados sobrescrevem `VITE_API_URL`.
  - Caminhos: `client/kubb.config.ts`, `client/src/kubb/hooks/**` apos regeneracao.
  - Acao: configurar Kubb para nao embutir `baseURL` fixo ou usar valor compatibilidade com ambiente.

- [X] Corrigir typo de formatacao no import do `api.ts`.
  - Problema: ha espacos antes de `import { keepPreviousData, QueryClient }...` no working tree atual.
  - Caminho: `client/src/lib/shared/api.ts`.
  - Acao: remover indentacao indevida.

### Baixo Impacto / Baixa Complexidade

- [ ] Documentar explicitamente que arquivar usuario nao revoga token ja emitido.
  - Decisao: token permanece valido ate expirar.
  - Caminho: `AUTH.md`.
  - Acao: adicionar nota em `Estrategia JWT` ou `Decisoes Fechadas`.

- [ ] Ajustar mensagens de exemplo de email em DTOs.
  - Problema: `UserRequestDto.username` usa exemplo `john_doe`, mas username e email.
  - Caminho: `server/src/main/java/aprimorar/auth/api/dto/UserRequestDto.java`.
  - Acao: trocar exemplo para `john@empresa.com`.
