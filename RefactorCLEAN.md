# Refactor CLEAN

Este arquivo rastreia oportunidades de limpeza/refatoracao no backend. O foco e qualidade interna sem alterar comportamento funcional, exceto quando indicado. As tarefas devem ser marcadas com `[X]` conforme forem implementadas.

## TODOs

### Alto Impacto / Baixa Complexidade

- [X] Remover imports nao usados em `OpenAPIConfig`.
  - Problema: `SecuritySchemes` esta importado e nao usado.
  - Caminho: `server/src/main/java/aprimorar/config/OpenAPIConfig.java`.
  - Acao: remover import morto.

- [X] Corrigir metadado inconsistente no `GlobalExceptionHandler`.
  - Problema: handler de `DataIntegrityViolationException` retorna `409`, mas `@ApiResponse` diz `400`.
  - Caminho: `server/src/main/java/aprimorar/shared/exception/GlobalExceptionHandler.java`.
  - Acao: ajustar `@ApiResponse(responseCode = "409")`.

- [X] Corrigir tratamento de `HttpMessageNotReadableException`.
  - Problema: `HttpMessageNotReadableException` esta agrupada com `Exception.class` e retorna `500`.
  - Caminho: `server/src/main/java/aprimorar/shared/exception/GlobalExceptionHandler.java`.
  - Acao: criar handler especifico retornando `400 BAD_REQUEST`.

### Alto Impacto / Media Complexidade

- [X] Padronizar erros de dominio do modulo auth.
  - Problema: `UserService` usa `ResponseStatusException` direto, enquanto outros modulos usam excecoes de dominio.
  - Caminho: `server/src/main/java/aprimorar/auth/internal/UserService.java`, novo pacote `server/src/main/java/aprimorar/auth/api/exception/`.
  - Acao: criar excecoes como `UserAlreadyExistsException`, `UserNotFoundException`, `InvalidUserRoleException` e handler especifico.

- [ ] Criar mapper interno para usuario de autenticacao.
  - Problema: `UserService` monta `UserResponseDto` manualmente.
  - Caminho: `server/src/main/java/aprimorar/auth/internal/UserService.java`.
  - Acao: extrair `UserMapper` em `server/src/main/java/aprimorar/auth/internal/UserMapper.java`.
  - Beneficio: segue padrao usado por outros modulos e reduz responsabilidade do service.

- [X] Revisar CORS permissivo.
  - Problema: `SecurityConfig` usa `setAllowedOriginPatterns(List.of("*"))`, metodos e headers `*`.
  - Caminho: `server/src/main/java/aprimorar/config/SecurityConfig.java`.
  - Acao: manter permissivo apenas em dev ou mover origins para config por ambiente.

### Medio Impacto / Baixa Complexidade

- [X] Remover setters desnecessarios da entity `User`.
  - Problema: `User` possui `@Setter` em `username`, `password`, `role`, mas update de usuario foi removido.
  - Caminho: `server/src/main/java/aprimorar/auth/internal/User.java`.
  - Acao: remover setters se nenhum fluxo atual usa mutacao direta desses campos.

- [X] Adicionar `api/package-info.java` no modulo auth, se DTOs forem expostos como API publica do modulo.
  - Problema: `auth/api/` existe, mas nao possui `@NamedInterface("api")`; outros modulos seguem esse padrao.
  - Caminho: `server/src/main/java/aprimorar/auth/api/package-info.java`.
  - Acao: criar package-info se quisermos consistencia com Spring Modulith.
  - Observacao: nao e obrigatorio enquanto nenhum outro modulo depender de `auth::api`.

- [X] Padronizar sufixo dos DTOs do auth.
  - Problema: projeto mistura `DTO` e `Dto`; auth tem `UserRequestDto`, `UserResponseDto`, enquanto outros usam `DTO`.
  - Caminhos: `server/src/main/java/aprimorar/auth/api/dto/UserRequestDto.java`, `server/src/main/java/aprimorar/auth/api/dto/UserResponseDto.java`.
  - Acao: decidir entre manter novo padrao ou renomear para `UserRequestDTO` / `UserResponseDTO`.
  - Observacao: renomear exige regenerar Kubb.

### Medio Impacto / Media Complexidade

- [X] Separar configuracao de OAuth/JWT de configuracao HTTP em `SecurityConfig`.
  - Problema: `SecurityConfig` concentra CORS, filter chain, encoder/decoder, password encoder e converter JWT.
  - Caminho: `server/src/main/java/aprimorar/config/SecurityConfig.java`.
  - Acao: avaliar extrair beans JWT para `JwtConfig` ou similar.
  - Beneficio: reduz tamanho e responsabilidade de `SecurityConfig`.

- [X] Rever uso de `ResponseStatusException` em services novos.
  - Problema: novo auth usa exceptions HTTP no service, acoplando camada de negocio ao transporte.
  - Caminho: `server/src/main/java/aprimorar/auth/internal/UserService.java`.
  - Acao: substituir por excecoes de dominio mapeadas em handler.
  - Relacionado: tarefa de padronizacao de erros do modulo auth.

### Baixo Impacto / Baixa Complexidade

- [ ] Corrigir mensagens ASCII/acentuacao inconsistentes no auth.
  - Problema: mensagens misturam portugues sem acento (`Usuario`, `ja existe`) com o restante do projeto.
  - Caminhos: `server/src/main/java/aprimorar/auth/**/*.java`.
  - Acao: padronizar texto das mensagens depois de decidir convencao.

- [ ] Atualizar exemplos Swagger do auth.
  - Problema: exemplos e descriptions ainda usam termos como `Username` com email obrigatorio.
  - Caminhos: `server/src/main/java/aprimorar/auth/api/dto/*.java`.
  - Acao: padronizar descricoes para "email usado no login".

- [ ] Remover comentario suprimido desnecessario em `GlobalExceptionHandler`.
  - Problema: `@SuppressWarnings("unused")` em `applicationClock`, mas o campo so e injetado e nunca usado.
  - Caminho: `server/src/main/java/aprimorar/shared/exception/GlobalExceptionHandler.java`.
  - Acao: remover campo/construtor se clock nao for necessario, ou usar o clock no `ProblemResponseDTO` se aplicavel.
