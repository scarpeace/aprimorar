# Plano de Autenticacao

Este arquivo e a fonte de verdade para a implementacao de autenticacao no projeto. As decisoes fechadas devem ser mantidas aqui, e as etapas de implementacao serao marcadas com `[X]` conforme forem concluidas.

## Decisoes Fechadas

- Escopo inicial de acesso: apenas `EMPLOYEE` e `ADMIN`.
- Modelo de autenticacao: tabela propria de contas, separada das entidades de dominio.
- Nome da tabela de contas: `tb_users`.
- O modelo inicial sera desacoplado das entidades de dominio, sem `person_id`.
- O login usara `email + senha`.
- O email sera o identificador de login da conta, sem vinculacao obrigatoria com `Employee`.
- Nao teremos campo `name` em `tb_users` nesta primeira versao.
- O endpoint de login retornara apenas `accessToken` e `expiresIn`.
- O access token tera validade de `8h`.
- Nao havera refresh token na primeira entrega.
- O seed inicial criara usuario admin automaticamente apenas no ambiente de desenvolvimento.
- O admin inicial sera uma conta propria, sem necessidade de participar dos dados operacionais do Aprimorar.
- Cada conta tera um unico `role`.
- As chaves JWT no ambiente local ficarao em arquivo no backend ignorado pelo git.
- Ao receber `401`, o frontend deve limpar a sessao local e redirecionar para `/login`.

## Direcao Atual do Modelo

- Manteremos autenticacao com Spring Security + JWT.
- O backend emitira `Bearer Token` stateless.
- O frontend deixara de usar sessao mockada em `localStorage` e passara a usar login real.
- A API `POST /v1/auth/login` sera publica.
- Os endpoints administrativos serao restritos a `ADMIN`.
- Os demais endpoints protegidos ficarao acessiveis para usuarios autenticados dentro do escopo inicial.
- As contas de autenticacao nao dependerao de `Employee` nesta primeira versao.
- No ambiente local, as chaves JWT ficarao em `server/.jwt/app.key` e `server/.jwt/app.pub`.
- O `application-dev.yml` lera essas chaves via `file:.jwt/app.key` e `file:.jwt/app.pub`.

## Proposta Atual de Dados

Tabela `tb_users`:

- `id`
- `username`
- `password`
- `role`
- `active`
- `created_at`
- `updated_at`

Notas:

- `username` armazenara o email usado no login.
- O nome fisico da coluna pode continuar como `username` mesmo sendo um email, para preservar flexibilidade futura.
- O campo `password` deve armazenar hash BCrypt, nunca senha em texto puro.
- `active` define se a conta pode autenticar.
- Como o modelo esta desacoplado, nao havera FK para entidades de cadastro nesta primeira versao.

## Etapas de Implementacao

- [ ] Fechar todas as decisoes funcionais e tecnicas restantes neste documento.
- [ ] Definir o modelo final de `tb_users` e suas constraints.
- [X] Definir a estrategia de chave JWT para desenvolvimento e producao.
- [ ] Criar migration da tabela `tb_users`.
- [ ] Criar seed de admin para desenvolvimento.
- [ ] Criar entidade, repositorio e servicos de autenticacao no backend.
- [ ] Implementar `POST /v1/auth/login`.
- [ ] Configurar `SecurityConfig` para JWT stateless.
- [ ] Restringir `/admin/**` a `ADMIN`.
- [ ] Exigir autenticacao nos endpoints protegidos da API.
- [ ] Documentar autenticacao no OpenAPI/Swagger.
- [ ] Regenerar cliente Kubb no frontend.
- [ ] Trocar auth mockada por login real no frontend.
- [ ] Injetar `Authorization: Bearer <token>` no cliente HTTP do frontend.
- [ ] Implementar tratamento centralizado de `401/403` no frontend.
- [ ] Proteger rotas autenticadas no frontend.
- [ ] Restringir rotas e navegacao de admin no frontend.
- [ ] Validar backend com testes relevantes.
- [ ] Validar frontend com `npm run lint` e `npm run build`.

## Questoes em Aberto

- [X] Vamos usar `role` unico por usuario nesta fase ou preparar suporte a multiplos papeis por conta?
- [X] Onde as chaves JWT ficarao no ambiente local: arquivo ignorado no git, variavel de ambiente, ou outro mecanismo?
- [X] Ao receber `401`, o frontend deve apenas deslogar e redirecionar para `/login`, ou tentar alguma estrategia adicional?

## Decisoes Ja Sugeridas

- Recomendacao: um unico `role` por usuario nesta primeira versao.
- Recomendacao: usar coluna `username` unica, contendo o email de login.
- Recomendacao: ao receber `401`, limpar sessao local e redirecionar para `/login`.

## Status do Planejamento

- O planejamento funcional inicial do backend esta pronto para implementacao incremental.
- A implementacao deve priorizar backend primeiro, com validacao a cada etapa.
- O frontend sera adaptado depois da base de autenticacao do backend estar funcional.

## Estrategia JWT

- Desenvolvimento local:
  - chave privada em `server/.jwt/app.key`
  - chave publica em `server/.jwt/app.pub`
  - ambos os arquivos ficam fora do git
- Configuracao Spring no perfil `dev`:
  - `jwt.private.key=file:.jwt/app.key`
  - `jwt.public.key=file:.jwt/app.pub`
- Producao:
  - manter o mesmo contrato de propriedades (`jwt.private.key` e `jwt.public.key`)
  - trocar apenas a origem do recurso conforme o ambiente de deploy
- Observacao:
  - nesta etapa, apenas definimos o contrato e o local das chaves; a geracao e o consumo pelas beans de seguranca virao nas proximas etapas.
