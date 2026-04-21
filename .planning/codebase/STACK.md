# Stack Tecnológica

**Data da Análise:** 17-04-2026

## Linguagens

**Primárias:**
- Java 21 - API Backend em `server/api-aprimorar/pom.xml` e `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`
- TypeScript 5.9 - SPA Frontend em `client/package.json`, `client/src/main.tsx` e `client/src/App.tsx`

**Secundárias:**
- SQL - Migração de esquema Flyway em `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql`
- JavaScript (módulos ES) - configuração de frontend/build em `client/eslint.config.js` e scripts de orquestração raiz no `package.json`
- YAML - Configuração do Spring em `server/api-aprimorar/src/main/resources/application.yml` e `server/api-aprimorar/src/main/resources/application-dev.yml`

## Tempo de Execução (Runtime)

**Ambiente:**
- JVM 21 para a API Spring Boot, fixada por `server/api-aprimorar/pom.xml`
- Tempo de execução Node.js para ferramentas de frontend/build; o npm é utilizado, mas nenhum arquivo de versão do Node fixado no repositório foi detectado (`package-lock.json` presente na raiz do repositório e em `client/`)
- Tempo de execução do navegador para o SPA React em `client/src/main.tsx`

**Gerenciador de Pacotes:**
- npm - scripts de workspace raiz no `package.json` e dependências de frontend em `client/package.json`
- Maven Wrapper (`./mvnw`) - ponto de entrada de build do backend em `server/api-aprimorar/mvnw`
- Lockfile: presente em `package-lock.json` e `client/package-lock.json`

## Frameworks

**Core:**
- Spring Boot 3.5.7 - Plataforma de API REST em `server/api-aprimorar/pom.xml`
- Spring Web MVC - Camada HTTP via `spring-boot-starter-web` em `server/api-aprimorar/pom.xml`
- Spring Data JPA - Camada de persistência via `spring-boot-starter-data-jpa` em `server/api-aprimorar/pom.xml`
- React 19.2 - UI do SPA em `client/package.json` e `client/src/main.tsx`
- React Router 7 - Roteamento do cliente em `client/package.json` e `client/src/App.tsx`

**Testes:**
- Spring Boot Test - pacote de testes de backend em `server/api-aprimorar/pom.xml`
- JUnit 5 / Mockito / AssertJ - incluídos através do `spring-boot-starter-test`; árvore de testes sob `server/api-aprimorar/src/test/java/`
- Nenhum framework de teste de frontend dedicado detectado; `client/AGENTS.md` afirma que nenhum está configurado

**Build/Dev:**
- Vite 7 - Servidor de desenvolvimento e empacotador (bundler) de frontend em `client/package.json` e `client/vite.config.ts`
- Compilador TypeScript - verificação de tipo/build do frontend em `client/package.json`
- Tailwind CSS 4 com `@tailwindcss/vite` - pipeline de estilização em `client/package.json` e `client/vite.config.ts`
- ESLint 9 + typescript-eslint - linting do frontend em `client/eslint.config.js`
- Kubb 4 - codegen/cliente OpenAPI em `client/package.json` e `client/kubb.config.ts`
- Springdoc OpenAPI 2.8.9 - geração de documentação de API em `server/api-aprimorar/pom.xml` e `server/api-aprimorar/src/main/resources/application.yml`
- Flyway - migrações de banco de dados em `server/api-aprimorar/pom.xml` e `server/api-aprimorar/src/main/resources/application.yml`
- JaCoCo - relatórios de cobertura do backend em `server/api-aprimorar/pom.xml`
- Docker Compose - dependência do Postgres local conforme `AGENTS.md` e `server/api-aprimorar/docker-compose.yml`

## Dependências Chave

**Críticas:**
- `org.springframework.boot:spring-boot-starter-web` - serve a API REST do backend em `server/api-aprimorar/pom.xml`
- `org.springframework.boot:spring-boot-starter-data-jpa` - sustenta a persistência de entidade/repositório em `server/api-aprimorar/pom.xml`
- `org.postgresql:postgresql` - driver de banco de dados em tempo de execução em `server/api-aprimorar/pom.xml`
- `org.flywaydb:flyway-database-postgresql` - motor de migração de esquema em `server/api-aprimorar/pom.xml`
- `@tanstack/react-query` - cache de estado de servidor do frontend em `client/package.json` e `client/src/lib/shared/queryClient.ts`
- `axios` - cliente HTTP do frontend em `client/package.json` e `client/src/lib/shared/api.ts`
- `react-hook-form` + `zod` + `@hookform/resolvers` - pilha de limite de formulário e validação em `client/package.json` e arquivos de formulário de funcionalidades como `client/src/features/students/pages/StudentCreatePage.tsx`

**Infraestrutura:**
- `org.springdoc:springdoc-openapi-starter-webmvc-ui` - expõe `/v3/api-docs` e a UI do Swagger configurada em `server/api-aprimorar/src/main/resources/application.yml`
- `@kubb/cli` e plugins Kubb - geram tipos TS, esquemas Zod e hooks do React Query em `client/kubb.config.ts`
- `@vitejs/plugin-react` - integração do React para o Vite em `client/package.json` e `client/vite.config.ts`
- `lucide-react` - biblioteca de ícones em todos os arquivos de UI, como `client/src/App.tsx`
- `react-datepicker` - UI de entrada de data em `client/src/components/ui/date-time-input.tsx`
- `recharts` - gráficos de dashboard em `client/src/features/dashboard/components/PizzaChart.tsx`
- `sonner` - notificações toast em `client/src/App.tsx`
- `spring-boot-docker-compose` - dependência de integração do Docker Compose no lado do Spring em `server/api-aprimorar/pom.xml`

## Configuração

**Ambiente:**
- O Spring usa o perfil `dev` por padrão em `server/api-aprimorar/src/main/resources/application.yml`
- O datasource do backend local, Hibernate, logging e as configurações de desenvolvimento do Jackson residem em `server/api-aprimorar/src/main/resources/application-dev.yml`
- A URL base da API do frontend é configurável com a opção `VITE_API_URL` em `client/src/lib/shared/api.ts`
- Nenhum arquivo `.env` foi detectado na raiz do repositório, em `client/` ou em `server/api-aprimorar/`

**Build:**
- Os scripts de orquestração raiz residem no `package.json`
- A configuração de build do frontend reside em `client/vite.config.ts`, `client/tsconfig.json` e `client/eslint.config.js`
- A configuração de build do backend reside em `server/api-aprimorar/pom.xml`
- A configuração de geração do cliente OpenAPI reside em `client/kubb.config.ts`

## Requisitos da Plataforma

**Desenvolvimento:**
- Java 21 para o backend conforme `server/api-aprimorar/pom.xml`
- Node.js + npm para a raiz e os scripts de frontend no `package.json` e `client/package.json`
- PostgreSQL disponível localmente em `localhost:5432` para o perfil de desenvolvimento em `server/api-aprimorar/src/main/resources/application-dev.yml`
- Docker / Docker Compose para inicialização do banco de dados local conforme `AGENTS.md` raiz, `server/api-aprimorar/AGENTS.md` e `server/api-aprimorar/docker-compose.yml`

**Produção:**
- O alvo de implantação não está explicitamente codificado na configuração do repositório
- O código atual assume um serviço HTTP Spring Boot expondo OpenAPI (`server/api-aprimorar/src/main/resources/application.yml`) e um SPA construído com Vite e hospedado separadamente ou como estático (`client/package.json`)

---

*Análise da stack: 17-04-2026*
