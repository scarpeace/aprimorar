# Plano — JWT stateless sem Resource Server

## Objetivo

Substituir o uso de `spring-boot-starter-oauth2-resource-server`, Nimbus e
`JwtConfig` por uma implementação JWT stateless explícita dentro do módulo
`auth`.

O Spring Security continuará responsável pela cadeia HTTP, autorização por
roles, BCrypt e `SecurityContext`. O módulo `auth` passará a emitir e validar
os tokens por meio de `java-jwt` e de um filtro próprio.

## Contrato escolhido

- A variável `JWT_SECRET` continua obrigatória, mas será usada como texto
  bruto, com pelo menos 32 bytes em UTF-8.
- O algoritmo continua sendo HS256, usando `Algorithm.HMAC256(secret)`.
- O token terá `issuer` `aprimorar-api`, `subject` igual ao UUID do usuário e
  expiração de oito horas.
- O endpoint de login e o payload da resposta não mudam.
- O token terá somente os claims necessários para validação (`iss`, `sub`,
  `iat` e `exp`). As authorities serão carregadas do usuário no banco.

O valor atual do `.env` pode continuar igual: ele tem aparência de Base64, mas
passará a ser interpretado literalmente. Isso muda a chave efetiva e invalida
tokens emitidos anteriormente, o que é aceitável no ambiente de desenvolvimento.

## Estrutura final

```text
auth/
├── config/
│   ├── AuthExceptionHandler
│   └── JwtAuthenticationFilter
├── service/
│   ├── AuthService
│   └── JwtService
└── ...

config/
└── SecurityConfig
```

`SecurityConfig` permanece geral porque configura CORS e as regras HTTP de
todos os módulos. `JwtAuthenticationFilter` e `JwtService` pertencem a
`auth`.

## Ordem de implementação

### Fase 1 — Simplificar o emissor

- Manter a dependência `com.auth0:java-jwt` em uma versão compatível com Java
  21.
- Ajustar `JwtService` para usar o `JWT_SECRET` diretamente como texto, sem
  `Base64.decode`.
- Criar um `Algorithm.HMAC256` e um `JWTVerifier` reutilizáveis.
- Emitir apenas `issuer`, `subject`, `issuedAt` e `expiresAt`.
- Atualizar o teste unitário do serviço.
- Não testar rota protegida enquanto o filtro antigo ainda estiver ativo.

### Fase 2 — Ativar o filtro próprio

- Usar o `JwtAuthenticationFilter` já criado em `auth.config`.
- Aceitar somente o cabeçalho no formato exato `Authorization: Bearer <token>`.
- Validar assinatura, issuer e expiração e retornar `Optional<UUID>` para token
  inválido.
- Carregar o usuário pelo UUID e autenticar somente usuários ativos.
- Registrar o filtro antes de `UsernamePasswordAuthenticationFilter`.
- Remover `.oauth2ResourceServer(...)` e o `JwtAuthenticationConverter` da
  cadeia HTTP.
- Mover o bean `PasswordEncoder` para `SecurityConfig` e removê-lo de
  `JwtConfig`, evitando dois beans iguais.

### Fase 3 — Remover o legado OAuth2/Nimbus

- Remover `JwtConfig` depois que a aplicação estiver funcionando com o filtro.
- Remover `spring-boot-starter-oauth2-resource-server` do `pom.xml`.
- Remover imports `org.springframework.security.oauth2.*` residuais.
- Conferir que nenhum módulo fora de `auth` depende de Nimbus ou Resource
  Server.

### Fase 4 — Validação final

- Executar `./mvnw clean test`.
- Subir a aplicação com o banco local.
- Conferir manualmente no Bruno:
  - login válido retorna token;
  - token válido acessa rota protegida;
  - token expirado, inválido ou alterado recebe `401`;
  - ausência de token em rota protegida recebe `401`;
  - desativar o usuário impede o uso de um token ainda não expirado.

## Fora deste plano

- Refresh token, blacklist ou revogação persistida.
- OAuth login com provedores externos.
- Chaves assimétricas, JWKS ou Authorization Server.
- Alterações no frontend ou no código gerado pelo Kubb.
