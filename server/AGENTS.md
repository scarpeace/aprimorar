# AGENTS - Server (tecnico-first)

## Objetivo
- Guiar implementacao de codigo backend com foco em consistencia, previsibilidade e baixo acoplamento.
- Priorizar regras de negocio claras, contratos estaveis e fronteiras de modulo respeitadas.

## Arquitetura em uma frase
- Backend modular (Spring Modulith): entrada HTTP no pacote `internal/web` do modulo, orquestracao em service, persistencia via repository, integracao entre modulos apenas por contratos `...api`.

## Fluxo de implementacao obrigatorio
- Controller: recebe request, valida DTO (`@Valid`), delega para service e retorna DTO de resposta; sem regra de negocio.
- Service: concentra regra de negocio e casos de uso; coordena repositories e chamadas a outros modulos via `...api`.
- Repository: somente acesso a dados; nao coloca regra de negocio aqui.
- Mapper/construcao de DTO: manter conversao explicita na borda (controller/service), sem vazar entidade JPA para API.

## Padroes de escrita de codigo
- Nomeie classes/metodos por intencao de dominio (`archiveAluno`, `togglePagamentoDespesa`) e nao por detalhe tecnico.
- Prefira metodos curtos por caso de uso; extraia metodos privados quando houver mais de uma regra no mesmo fluxo.
- Nao criar "utils" generico sem dono; helper deve viver no modulo que usa.
- Evite logica duplicada entre modulos; quando for contrato de negocio, promova para `...api` do modulo dono.

## DTO, validacao e mapeamento
- Entrada/saida HTTP sempre por DTO (request/response), nunca entidade JPA direta.
- Validar entrada no DTO com Bean Validation; mensagens de erro devem ser consistentes com Problem Details.
- Mapeamento DTO <-> dominio deve ser explicito e previsivel; nao esconder regra em conversao automatica opaca.

## Erros e excecoes
- Use excecoes de dominio especificas para regra de negocio invalida e deixe handler global transformar em resposta padrao.
- Preserve semantica HTTP (400 validacao, 404 nao encontrado, 409 conflito, 401/403 seguranca).
- Nao retornar `null` para fluxo de erro de negocio; falhe com excecao clara.

## Transacao e consistencia
- Anote fronteira transacional no service (caso de uso), nao no controller.
- Uma transacao deve cobrir a consistencia do agregado local; integracao com outro modulo deve ser via contrato e sem acoplamento interno.
- Nao misture operacao de leitura pesada com escrita no mesmo metodo transacional sem necessidade.
- Sempre manter datas/horarios em UTC (app e JDBC ja usam UTC).

## Consultas e performance
- Em listagens, prefira projecoes e filtros no banco em vez de carregar tudo em memoria.
- Evite N+1: ajustar estrategia de consulta quando houver relacionamento frequente.
- Para buscas sensiveis (dashboard/resumo), documente no service qual regra de filtro foi aplicada.
- Alteracao de schema: sempre via Flyway migration (`db/migration` ou `db/dev/migration`), sem atalho.

## Contratos e integracao entre modulos
- Dependencia cruzada so por `...api`; nunca importar classes internas de outro modulo.
- Antes de nova dependencia, leia `package-info.java` do modulo e confirme `allowedDependencies`.
- Modulos atuais e limites principais:
  - `atendimentos` pode depender de `financeiro::api`, `pessoas::api`, `shared::*`, `shared`.
  - `financeiro` pode depender de `shared::*`.
  - `pessoas` pode depender de `shared::*`, `shared`.
  - `auth` pode depender de `shared::*`, `shared`.
  - `shared` nao deve depender de outros modulos de dominio.
- Mudou contrato de endpoint: alinhar OpenAPI e comunicar impacto no frontend (Kubb depende de `/v3/api-docs`).

## Matriz de testes por tipo de mudanca
- Regra de negocio em service: teste unitario do service + teste de integracao do caso principal.
- Endpoint/controller: teste de contrato HTTP (status, payload, validacao).
- Consulta custom/repository: teste de integracao com banco para filtro/ordenacao/paginacao.
- Fronteira de modulo/dependencia cruzada: `./mvnw test -Dtest=ModuleVerificationTest`.
- Mudanca ampla backend: `./mvnw clean compile` e `./mvnw test`.

## Anti-padroes
- Controller com regra de negocio ou acesso direto ao repository.
- Chamar modulo vizinho por classe interna ignorando `...api`.
- Entidade JPA exposta na resposta HTTP.
- Transacao gigante com varias responsabilidades sem limite claro.
- Query sem filtro minimo em endpoint de listagem.

## Regras de negocio do projeto (resumo)
- Contagens de resumo de aluno/colaborador excluem ghost.
- Contagens "ativas" excluem arquivados; contagens totais ainda incluem arquivados nao-ghost.
- Seguranca por JWT (resource server); fluxo de autenticacao deve manter esse contrato.
- `dev` e o profile padrao; execute comandos dentro de `server/`.

## Comandos de verificacao (executar em `server/`)
- `docker compose up -d db`
- `./mvnw spring-boot:run`
- `./mvnw clean compile`
- `./mvnw test`
- `./mvnw test -Dtest=ModuleVerificationTest`
