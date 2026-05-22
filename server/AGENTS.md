# AGENTS - Server

## Objetivo
- Este arquivo orienta agentes a manter os padroes de arquitetura e codigo do backend.
- Priorize consistencia com Spring Modulith e com os contratos publicos de cada modulo.

## Stack e baseline tecnico
- Java 21 com Spring Boot 3.5.x.
- Spring Modulith para fronteiras de modulo.
- PostgreSQL + Flyway para persistencia e migracoes.
- Spring Security com Resource Server JWT.

## Principios de arquitetura
- Pense o backend por modulos de negocio, nao por camadas globais.
- Dependencias entre modulos devem passar por contratos publicos em `...api`.
- Evite acesso direto a classes internas de outro modulo.
- Tudo que for compartilhado entre modulos deve ficar em `shared` ou em APIs explicitas.

## Regras obrigatorias de modulo (Spring Modulith)
- Antes de criar acoplamento entre modulos, leia o `package-info.java` do modulo de origem e destino.
- Respeite `allowedDependencies` definido nos `package-info.java` de topo.
- Se alterar fronteiras de modulo ou dependencias cruzadas, rode `./mvnw test -Dtest=ModuleVerificationTest`.
- Considere que o teste de verificacao de modulo tambem atualiza artefatos em `server/src/main/resources/docs/`.

## Padrões de código
- Prefira regras de negocio em servicos do modulo e controladores enxutos.
- Valide entrada com Bean Validation.
- Trate erros com Problem Details do Spring MVC; mantenha respostas de erro consistentes.
- Mantenha nomes de metodos e classes orientados ao dominio, evitando termos tecnicos genericos.
- Nao crie utilitarios globais sem dono de dominio claro.

## Banco e migracoes
- Toda mudanca de schema deve entrar via Flyway migration, sem atalhos.
- Nao dependa de criacao automatica de schema por JPA em desenvolvimento.
- Use UTC para datas/horarios e preserve esse padrao em novas consultas e entidades.

## API e contratos
- Mantenha compatibilidade dos endpoints existentes sempre que possivel.
- Mudou contrato de API: alinhe DTOs, validacoes e documentacao OpenAPI.
- Para integracao com frontend, lembre que o cliente gerado depende de `/v3/api-docs`.

## Fluxo de validacao antes de concluir
- Compile: `./mvnw clean compile`.
- Teste: `./mvnw test`.
- Se mexeu em fronteiras de modulo: `./mvnw test -Dtest=ModuleVerificationTest`.

## Comandos usuais (executar em `server/`)
- Subir banco local: `docker compose up -d db`.
- Rodar aplicacao: `./mvnw spring-boot:run`.
- Rodar um teste especifico: `./mvnw test -Dtest=NomeDaClasse`.

## Alertas importantes deste projeto
- O profile ativo padrao e `dev`.
- O root `package.json` tem scripts antigos para backend; prefira comandos direto em `server/`.
