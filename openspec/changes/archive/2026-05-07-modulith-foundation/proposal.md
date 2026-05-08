## Why

O servidor já possui uma estrutura de pacotes por domínio, mas sem enforcement de boundaries — qualquer classe pode importar qualquer outra. O Spring Modulith adiciona verificação formal de módulos, documentação automática de arquitetura, e prepara o terreno para uma separação mais rigorosa entre domínios.

## What Changes

- Adicionar dependência `spring-modulith-starter-core` e `spring-modulith-starter-test` no `pom.xml`
- Criar `package-info.java` para cada módulo declarando dependências permitidas (`@ApplicationModule`)
- Configurar `com.aprimorar.api.shared`, `com.aprimorar.api.enums` e `com.aprimorar.api.exception` como shared packages (isentos de verificação)
- Configurar `com.aprimorar.api.config` como application-level infrastructure
- Rodar a primeira verificação de módulos para revelar TODAS as violações atuais
- Registrar as violações conhecidas como `allowedDependencies` nos package-info (serão resolvidas em fases posteriores)

## Capabilities

### New Capabilities
- `server-architecture`: Estrutura modular do servidor — decomposição de módulos, dependências, regras estruturais. (Já existe como spec main — esta change adiciona regras de Modulith.)

### Modified Capabilities
- `server-architecture`: Adicionar requirement sobre configuração de módulos Modulith (package-info, shared packages, verification rules)

## Impact

- `pom.xml`: novas dependências
- `server/src/main/java/com/aprimorar/api/`: package-info.java em cada subpacote de módulo
- Nenhuma mudança de código de negócio — apenas infraestrutura de módulos
- CI: adicionar verificação de módulos como etapa de build
