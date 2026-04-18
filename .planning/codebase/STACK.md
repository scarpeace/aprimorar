# Technology Stack

**Analysis Date:** 2026-04-17

## Languages

**Primary:**
- Java 21 - Backend API in `server/api-aprimorar/pom.xml` and `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`
- TypeScript 5.9 - Frontend SPA in `client/package.json`, `client/src/main.tsx`, and `client/src/App.tsx`

**Secondary:**
- SQL - Flyway schema migration in `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql`
- JavaScript (ES modules) - frontend/build config in `client/eslint.config.js` and root orchestration scripts in `package.json`
- YAML - Spring configuration in `server/api-aprimorar/src/main/resources/application.yml` and `server/api-aprimorar/src/main/resources/application-dev.yml`

## Runtime

**Environment:**
- JVM 21 for the Spring Boot API, pinned by `server/api-aprimorar/pom.xml`
- Node.js runtime for frontend/build tooling; npm is used, but no repo-pinned Node version file was detected (`package-lock.json` present at repo root and in `client/`)
- Browser runtime for the React SPA in `client/src/main.tsx`

**Package Manager:**
- npm - root workspace scripts in `package.json` and frontend dependencies in `client/package.json`
- Maven Wrapper (`./mvnw`) - backend build entrypoint in `server/api-aprimorar/mvnw`
- Lockfile: present in `package-lock.json` and `client/package-lock.json`

## Frameworks

**Core:**
- Spring Boot 3.5.7 - REST API platform in `server/api-aprimorar/pom.xml`
- Spring Web MVC - HTTP layer via `spring-boot-starter-web` in `server/api-aprimorar/pom.xml`
- Spring Data JPA - persistence layer via `spring-boot-starter-data-jpa` in `server/api-aprimorar/pom.xml`
- React 19.2 - SPA UI in `client/package.json` and `client/src/main.tsx`
- React Router 7 - client routing in `client/package.json` and `client/src/App.tsx`

**Testing:**
- Spring Boot Test - backend test bundle in `server/api-aprimorar/pom.xml`
- JUnit 5 / Mockito / AssertJ - included through `spring-boot-starter-test`; test tree under `server/api-aprimorar/src/test/java/`
- No dedicated frontend test framework detected; `client/AGENTS.md` states none is configured

**Build/Dev:**
- Vite 7 - frontend dev server and bundler in `client/package.json` and `client/vite.config.ts`
- TypeScript compiler - frontend type-check/build in `client/package.json`
- Tailwind CSS 4 with `@tailwindcss/vite` - styling pipeline in `client/package.json` and `client/vite.config.ts`
- ESLint 9 + typescript-eslint - frontend linting in `client/eslint.config.js`
- Kubb 4 - OpenAPI client/codegen in `client/package.json` and `client/kubb.config.ts`
- Springdoc OpenAPI 2.8.9 - API documentation generation in `server/api-aprimorar/pom.xml` and `server/api-aprimorar/src/main/resources/application.yml`
- Flyway - database migrations in `server/api-aprimorar/pom.xml` and `server/api-aprimorar/src/main/resources/application.yml`
- JaCoCo - backend coverage reporting in `server/api-aprimorar/pom.xml`
- Docker Compose - local Postgres dependency per `AGENTS.md` and `server/api-aprimorar/docker-compose.yml`

## Key Dependencies

**Critical:**
- `org.springframework.boot:spring-boot-starter-web` - serves the backend REST API from `server/api-aprimorar/pom.xml`
- `org.springframework.boot:spring-boot-starter-data-jpa` - backs entity/repository persistence in `server/api-aprimorar/pom.xml`
- `org.postgresql:postgresql` - runtime database driver in `server/api-aprimorar/pom.xml`
- `org.flywaydb:flyway-database-postgresql` - schema migration engine in `server/api-aprimorar/pom.xml`
- `@tanstack/react-query` - frontend server-state caching in `client/package.json` and `client/src/lib/shared/queryClient.ts`
- `axios` - frontend HTTP client in `client/package.json` and `client/src/lib/shared/api.ts`
- `react-hook-form` + `zod` + `@hookform/resolvers` - form and validation boundary stack in `client/package.json` and feature form files such as `client/src/features/students/pages/StudentCreatePage.tsx`

**Infrastructure:**
- `org.springdoc:springdoc-openapi-starter-webmvc-ui` - exposes `/v3/api-docs` and Swagger UI configured in `server/api-aprimorar/src/main/resources/application.yml`
- `@kubb/cli` and Kubb plugins - generate TS types, Zod schemas, and React Query hooks in `client/kubb.config.ts`
- `@vitejs/plugin-react` - React integration for Vite in `client/package.json` and `client/vite.config.ts`
- `lucide-react` - icon library across UI files such as `client/src/App.tsx`
- `react-datepicker` - date input UI in `client/src/components/ui/date-time-input.tsx`
- `recharts` - dashboard charts in `client/src/features/dashboard/components/PizzaChart.tsx`
- `sonner` - toast notifications in `client/src/App.tsx`
- `spring-boot-docker-compose` - Spring-side Docker Compose integration dependency in `server/api-aprimorar/pom.xml`

## Configuration

**Environment:**
- Spring uses the `dev` profile by default in `server/api-aprimorar/src/main/resources/application.yml`
- Local backend datasource, Hibernate, logging, and Jackson dev settings live in `server/api-aprimorar/src/main/resources/application-dev.yml`
- Frontend API base URL is configurable with optional `VITE_API_URL` in `client/src/lib/shared/api.ts`
- No `.env` files were detected in the repository root, `client/`, or `server/api-aprimorar/`

**Build:**
- Root orchestration scripts live in `package.json`
- Frontend build config lives in `client/vite.config.ts`, `client/tsconfig.json`, and `client/eslint.config.js`
- Backend build config lives in `server/api-aprimorar/pom.xml`
- OpenAPI client generation config lives in `client/kubb.config.ts`

## Platform Requirements

**Development:**
- Java 21 for the backend per `server/api-aprimorar/pom.xml`
- Node.js + npm for the root and frontend scripts in `package.json` and `client/package.json`
- PostgreSQL available locally at `localhost:5432` for the dev profile in `server/api-aprimorar/src/main/resources/application-dev.yml`
- Docker / Docker Compose for local database startup per root `AGENTS.md`, `server/api-aprimorar/AGENTS.md`, and `server/api-aprimorar/docker-compose.yml`

**Production:**
- Deployment target is not explicitly codified in repo config
- Current code assumes a Spring Boot HTTP service exposing OpenAPI (`server/api-aprimorar/src/main/resources/application.yml`) and a separately hosted/static Vite-built SPA (`client/package.json`)

---

*Stack analysis: 2026-04-17*
