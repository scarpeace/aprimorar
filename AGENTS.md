# AGENTS.md

This is the canonical guide for coding agents working in this repository.

## Project overview

Aprimorar is an educational course management system for internal administrative use.

Core flows:
- student registration and profile management
- employee management
- event and class scheduling
- dashboard and operational workflows
- planned financial and reporting features

Current scope:
- in scope: operations, scheduling, CRUD flows, API-first architecture, PostgreSQL persistence
- out of scope for now: student portal, parent portal, LMS features, advanced pedagogy analytics, SaaS multi-tenancy

## Repository structure

This repository has two apps:
- `client/`: React 19 + TypeScript + Vite frontend
- `server/api-aprimorar/`: Java 21 + Spring Boot 3.5 backend

Important paths:
- `client/src/features/**`: feature pages
- `client/src/components/ui/**`: reusable UI primitives
- `client/src/components/layout/**`: layout and navigation
- `client/src/lib/schemas/**`: Zod schemas and inferred types
- `client/src/services/api.ts`: frontend API client
- `server/api-aprimorar/src/main/java/com/aprimorar/api/controller/**`: REST controllers
- `server/api-aprimorar/src/main/java/com/aprimorar/api/service/**`: business services
- `server/api-aprimorar/src/main/java/com/aprimorar/api/repository/**`: repositories and specifications
- `server/api-aprimorar/src/main/java/com/aprimorar/api/dto/**`: request/response DTOs
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/**`: exceptions and handlers
- `server/api-aprimorar/src/main/resources/db/migration/**`: Flyway migrations
- `docs/planning/**`: roadmap, epics, diagrams, planning guides
- `docs/refactor/**`: refactor review notes

There is no root-level build orchestration. Run commands from the correct subproject directory.

## Architecture summary

- Frontend is a React SPA using React Router, Axios, Zod, and React Hook Form patterns.
- Backend is a Spring Boot REST API using Spring MVC, JPA/Hibernate, Jakarta Validation, and Flyway.
- PostgreSQL is the runtime database.
- Frontend talks directly to backend endpoints; no BFF layer exists.
- Validation is intentionally duplicated across layers: Zod in frontend, Jakarta Validation in backend DTOs.

## Tooling and environment

- Frontend uses Node/npm with `package-lock.json` committed.
- Frontend alias: `@/* -> client/src/*`.
- Frontend TypeScript is `strict` and enables `noUnusedLocals` and `noUnusedParameters`.
- Frontend linting is ESLint-based.
- Backend uses Maven Wrapper (`./mvnw`), not Gradle.
- Backend targets Java 21.
- Backend uses PostgreSQL and Flyway.
- Local database helper: `server/api-aprimorar/docker-compose.yml` starts Postgres 15.

## Canonical docs

Use these files as the current sources of truth:
- `AGENTS.md`: repo, product, workflow, and coding guidance
- `docs/planning/roadmap.md`: phase order and epic index
- `docs/planning/epics/E-XXX-*.md`: epic plans and task tracking
- `docs/planning/epics/E-000-template.md`: epic template
- `docs/planning/prompt-style-guide.md`: planning/doc update guide
- `docs/planning/diagrams.md`: shared Mermaid diagrams
- `docs/refactor/server-refactor-review.md`: backend refactor outcomes

Documentation rules:
- never invent completed work
- only document approved decisions
- keep epic and task IDs stable
- update docs when commands, contracts, workflow, or architecture meaningfully change

## Planning workflow

Preferred flow:
1. clarify scope
2. organize work into epics/tasks
3. update docs for approved decisions
4. implement one focused task at a time
5. review quality and tests

## Commands

### Frontend (`client/`)

- install deps: `npm install`
- dev server: `npm run dev`
- build: `npm run build`
- lint: `npm run lint`
- preview: `npm run preview`

### Backend (`server/api-aprimorar/`)

- start local DB: `docker compose up -d db`
- stop local DB: `docker compose down`
- run app: `./mvnw spring-boot:run`
- package: `./mvnw package`
- verify: `./mvnw verify`
- all tests: `./mvnw test`

### Single test commands

- single backend test class: `./mvnw -Dtest=StudentServiceTest test`
- single backend test method: `./mvnw -Dtest=StudentServiceTest#findByIdFound test`
- another example: `./mvnw -Dtest=EventControllerTest test`

Frontend test status:
- there is currently no frontend `test` script in `client/package.json`
- do not invent a frontend test command in automation

## Verification expectations

- frontend linting: `npm run lint`
- frontend build also type-checks: `npm run build`
- backend validation: `./mvnw test` or `./mvnw verify`
- prefer the narrowest command that proves the change

Typical verification:
- frontend UI change: `npm run lint` and `npm run build`
- backend logic change: `./mvnw test`
- backend API contract change: `./mvnw test` plus frontend `npm run build`
- migration or data model change: `docker compose up -d db` and `./mvnw test`

## Frontend conventions

- use TypeScript everywhere; do not add plain `.js` or `.jsx` app code
- prefer named exports for components and helpers; `App` is the main exception
- keep route screens in feature folders with `*Page.tsx` naming
- keep component-local styling in `*.module.css`
- keep shared primitives under `client/src/components/ui/`
- keep validation schemas in `client/src/lib/schemas/`
- derive TS types with `z.infer` instead of duplicating interfaces
- use the `@/` alias instead of deep relative imports
- prefer `import type` for type-only imports
- match the formatting style of the file you edit; do not reformat unrelated files

Naming and UX:
- components: PascalCase
- hooks, functions, variables: camelCase
- constants: UPPER_SNAKE_CASE only for true constants
- CSS module class names: camelCase
- keep user-facing strings in Portuguese unless the surrounding UI is already English
- keep code identifiers in English unless a local convention clearly differs

State and error handling:
- follow the existing `loading / error / data` page pattern
- wrap async page loads in `try/catch/finally`
- reset stale error state before retrying requests
- use `getFriendlyErrorMessage(error)` for API-facing errors
- keep `console.error(...)` for developer diagnostics on failures
- prefer explicit loading, empty, and error states over silent failures
- reuse existing UI states such as `LoadingState`, `ErrorState`, and `EmptyState`

## Backend conventions

- follow package layering: controller -> service -> repository/mapper
- keep HTTP concerns in controllers and business rules in services
- prefer constructor injection with `private final` fields
- use DTO records for request and response payloads when appropriate
- keep validation annotations on DTOs, not scattered across controllers
- use `ResponseEntity` in controllers to make status codes explicit
- keep pagination and sorting defaults centralized
- use `UUID` for entity identifiers where the model already does
- inject `Clock` for time-sensitive logic instead of calling system time directly
- keep persistence details in repositories/specifications, not controllers

Naming and validation:
- classes: PascalCase
- methods, fields, locals: camelCase
- exception classes should use precise suffixes such as `NotFoundException`
- DTO names use `Create*DTO`, `Update*DTO`, `*ResponseDTO`, `*SummaryDTO`
- repositories end with `Repository`
- mappers end with `Mapper`
- specification helpers should use descriptive predicate names

Error handling:
- throw specific domain exceptions such as `StudentNotFoundException`
- let `GlobalExceptionHandler` translate exceptions into API responses
- do not swallow exceptions with empty catch blocks
- do not catch broad `Exception` in services unless real recovery or translation is needed
- preserve the existing structured error response format

## Testing conventions

- backend tests use JUnit 5, Mockito, and Spring test support
- service tests commonly use `@ExtendWith(MockitoExtension.class)`
- group related tests with `@Nested` classes and `@DisplayName`
- use fixed timestamps and `Clock` stubbing for deterministic assertions
- verify repository and mapper interactions when behavior matters
- keep test names descriptive and behavior-oriented
- add tests next to the layer you changed

## Change discipline

- keep changes scoped; avoid drive-by cleanup
- preserve Portuguese validation and API messages unless the task requires changing them
- do not rename files, endpoints, DTOs, or schema fields without checking full-stack impact
- for backend API changes, inspect both `server/api-aprimorar/` and `client/src/services/api.ts` plus affected schemas/pages
- for new backend fields, update migration, entity, DTO, mapper, tests, and frontend consumers as needed
- for frontend-only changes, confirm backend contracts already exist before inventing payload shapes
- when docs become stale because of your change, update them in the same task

## Repo-specific rules discovered

- no `.cursorrules` file was found
- no `.cursor/rules/` directory was found
- no Copilot instruction file is currently present in the repository
