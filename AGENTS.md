# AGENTS.md

## Scope
- This file applies to the whole repo.
- Also read `client/AGENTS.md` for SPA work and `server/api-aprimorar/AGENTS.md` for backend work.

## Trust These Sources
- Prefer repo config over prose docs. `README.md` still mentions a root `.env`, but the verified local dev path is the checked-in Postgres compose file plus `application-dev.yml`.

## Repo Shape
- `client/`: React 19 + TypeScript + Vite SPA.
- `server/api-aprimorar/`: Spring Boot 3.5 / Java 21 API.
- Main product areas visible in both apps: `dashboard`, `students`, `parents`, `employees`, `events`.

## Commands That Matter
- Root dev startup: `npm run dev`
- Backend only from root: `npm run start:backend`
- Frontend only from root without backend wait/sync: `npm run dev:frontend`
- Frontend startup that waits for backend docs and regenerates client first: `npm run start:frontend`

## Contract / Codegen Workflow
- Do not edit `client/src/kubb/` manually. `client/kubb.config.ts` writes there with `clean: true`, so regeneration replaces it.
- Both codegen steps depend on a live backend at `http://localhost:8080`:
  - backend OpenAPI generation: `./mvnw -Pgenerate-openapi generate-resources`
  - frontend client generation: `npm run sync` from `client/`
- If backend DTOs or endpoint signatures change, run them in this order: backend running -> generate OpenAPI -> `npm run sync` from `client/` -> frontend build.
- Root `npm run start:frontend` already waits on `/v3/api-docs`, runs `npm run sync`, then starts Vite.

## Backend Local Assumptions
- Default Spring profile is `dev`.
- Local DB settings are hardcoded for dev: PostgreSQL at `localhost:5432`, database `aprimorar`, user `myuser`, password `mypassword`.
- Start the required DB from `server/api-aprimorar/` with `docker compose up -d db`.
- Flyway is enabled and Hibernate runs with `ddl-auto: validate`; schema changes need a migration under `server/api-aprimorar/src/main/resources/db/migration/`.

## Cross-Stack Conventions
- Keep user-facing strings and backend validation/business messages in Portuguese.
- Backend pagination is a custom `PageDTO<T>` wrapper, not raw Spring `Page` JSON. Preserve that shape when changing contracts.
- Frontend uses the `@/*` alias for `client/src/*`.

## Verification
- Frontend: `npm run lint` and `npm run build` from `client/`.
- Backend: prefer focused Maven tests while iterating, then broaden as needed.
- Cross-stack contract changes usually need backend tests plus frontend build after Kubb sync.

<!-- GSD:project-start source:PROJECT.md -->
## Project

**Aprimorar**

Aprimorar is a school management application for a private class school that replaces spreadsheet-driven operational and financial control with a centralized system. It is currently oriented around secretary and administrator workflows, with the product expected to expand later to teachers, parents, students, and other employees.

**Core Value:** The secretary must be able to manage the school day to day from the app without depending on scattered spreadsheets.

### Constraints

- **Product scope**: v1 must focus on secretary/administrator workflows — later user portals are intentionally deferred to keep the first release operationally useful
- **Brownfield**: Existing student, parent, employee, event, and dashboard areas must be extended carefully rather than replaced — current code and patterns already exist
- **Tech stack**: Keep working within the current React SPA + Spring Boot + PostgreSQL architecture — this is the established repo direction
- **Integration boundary**: Frontend API types and hooks are generated from backend OpenAPI — backend contract changes must flow through codegen
- **Operational goal**: The app must be good enough to stop relying on spreadsheets for daily school administration — otherwise v1 has not delivered the core value
<!-- GSD:project-end -->

<!-- GSD:stack-start source:codebase/STACK.md -->
## Technology Stack

## Languages
- Java 21 - Backend API in `server/api-aprimorar/pom.xml` and `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`
- TypeScript 5.9 - Frontend SPA in `client/package.json`, `client/src/main.tsx`, and `client/src/App.tsx`
- SQL - Flyway schema migration in `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql`
- JavaScript (ES modules) - frontend/build config in `client/eslint.config.js` and root orchestration scripts in `package.json`
- YAML - Spring configuration in `server/api-aprimorar/src/main/resources/application.yml` and `server/api-aprimorar/src/main/resources/application-dev.yml`
## Runtime
- JVM 21 for the Spring Boot API, pinned by `server/api-aprimorar/pom.xml`
- Node.js runtime for frontend/build tooling; npm is used, but no repo-pinned Node version file was detected (`package-lock.json` present at repo root and in `client/`)
- Browser runtime for the React SPA in `client/src/main.tsx`
- npm - root workspace scripts in `package.json` and frontend dependencies in `client/package.json`
- Maven Wrapper (`./mvnw`) - backend build entrypoint in `server/api-aprimorar/mvnw`
- Lockfile: present in `package-lock.json` and `client/package-lock.json`
## Frameworks
- Spring Boot 3.5.7 - REST API platform in `server/api-aprimorar/pom.xml`
- Spring Web MVC - HTTP layer via `spring-boot-starter-web` in `server/api-aprimorar/pom.xml`
- Spring Data JPA - persistence layer via `spring-boot-starter-data-jpa` in `server/api-aprimorar/pom.xml`
- React 19.2 - SPA UI in `client/package.json` and `client/src/main.tsx`
- React Router 7 - client routing in `client/package.json` and `client/src/App.tsx`
- Spring Boot Test - backend test bundle in `server/api-aprimorar/pom.xml`
- JUnit 5 / Mockito / AssertJ - included through `spring-boot-starter-test`; test tree under `server/api-aprimorar/src/test/java/`
- No dedicated frontend test framework detected; `client/AGENTS.md` states none is configured
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
- `org.springframework.boot:spring-boot-starter-web` - serves the backend REST API from `server/api-aprimorar/pom.xml`
- `org.springframework.boot:spring-boot-starter-data-jpa` - backs entity/repository persistence in `server/api-aprimorar/pom.xml`
- `org.postgresql:postgresql` - runtime database driver in `server/api-aprimorar/pom.xml`
- `org.flywaydb:flyway-database-postgresql` - schema migration engine in `server/api-aprimorar/pom.xml`
- `@tanstack/react-query` - frontend server-state caching in `client/package.json` and `client/src/lib/shared/queryClient.ts`
- `axios` - frontend HTTP client in `client/package.json` and `client/src/lib/shared/api.ts`
- `react-hook-form` + `zod` + `@hookform/resolvers` - form and validation boundary stack in `client/package.json` and feature form files such as `client/src/features/students/pages/StudentCreatePage.tsx`
- `org.springdoc:springdoc-openapi-starter-webmvc-ui` - exposes `/v3/api-docs` and Swagger UI configured in `server/api-aprimorar/src/main/resources/application.yml`
- `@kubb/cli` and Kubb plugins - generate TS types, Zod schemas, and React Query hooks in `client/kubb.config.ts`
- `@vitejs/plugin-react` - React integration for Vite in `client/package.json` and `client/vite.config.ts`
- `lucide-react` - icon library across UI files such as `client/src/App.tsx`
- `react-datepicker` - date input UI in `client/src/components/ui/date-time-input.tsx`
- `recharts` - dashboard charts in `client/src/features/dashboard/components/PizzaChart.tsx`
- `sonner` - toast notifications in `client/src/App.tsx`
- `spring-boot-docker-compose` - Spring-side Docker Compose integration dependency in `server/api-aprimorar/pom.xml`
## Configuration
- Spring uses the `dev` profile by default in `server/api-aprimorar/src/main/resources/application.yml`
- Local backend datasource, Hibernate, logging, and Jackson dev settings live in `server/api-aprimorar/src/main/resources/application-dev.yml`
- Frontend API base URL is configurable with optional `VITE_API_URL` in `client/src/lib/shared/api.ts`
- No `.env` files were detected in the repository root, `client/`, or `server/api-aprimorar/`
- Root orchestration scripts live in `package.json`
- Frontend build config lives in `client/vite.config.ts`, `client/tsconfig.json`, and `client/eslint.config.js`
- Backend build config lives in `server/api-aprimorar/pom.xml`
- OpenAPI client generation config lives in `client/kubb.config.ts`
## Platform Requirements
- Java 21 for the backend per `server/api-aprimorar/pom.xml`
- Node.js + npm for the root and frontend scripts in `package.json` and `client/package.json`
- PostgreSQL available locally at `localhost:5432` for the dev profile in `server/api-aprimorar/src/main/resources/application-dev.yml`
- Docker / Docker Compose for local database startup per root `AGENTS.md`, `server/api-aprimorar/AGENTS.md`, and `server/api-aprimorar/docker-compose.yml`
- Deployment target is not explicitly codified in repo config
- Current code assumes a Spring Boot HTTP service exposing OpenAPI (`server/api-aprimorar/src/main/resources/application.yml`) and a separately hosted/static Vite-built SPA (`client/package.json`)
<!-- GSD:stack-end -->

<!-- GSD:conventions-start source:CONVENTIONS.md -->
## Conventions

## Naming Patterns
- Frontend React pages use `*Page.tsx` under `client/src/features/**/pages/`, for example `client/src/features/students/pages/StudentsPage.tsx` and `client/src/features/events/pages/EventCreatePage.tsx`.
- Frontend reusable UI files are kebab-case under `client/src/components/ui/`, for example `client/src/components/ui/list-search-input.tsx` and `client/src/components/ui/page-loading.tsx`.
- Frontend hooks are function-based and usually live in `hooks/` with kebab-case filenames, for example `client/src/features/events/hooks/use-event-mutations.ts` and `client/src/features/students/hooks/student-mutations.ts`.
- Frontend schemas follow `*Schema.ts` naming, for example `client/src/features/students/forms/studentFormSchema.ts` and `client/src/features/address/forms/addressSchema.ts`.
- Backend classes, records, and tests use PascalCase in mirrored package paths, for example `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`.
- Frontend components and helpers use `camelCase` or `PascalCase` depending on export type: `StudentsPage` in `client/src/features/students/pages/StudentsPage.tsx`, `getFriendlyErrorMessage` in `client/src/lib/shared/api-errors.ts`, and `useEmployeeMutations` in `client/src/features/employees/hooks/emlpoyee-mutations.ts`.
- Backend methods use `camelCase` with intention-revealing verbs, such as `createStudent`, `findById`, `archiveStudent`, and `ensureStudentUniqueness` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- Use descriptive `camelCase` names for local state and query results, such as `searchTerm`, `currentPage`, `showArchived`, and `studentsQuery` in `client/src/features/students/pages/StudentsPage.tsx`.
- Backend services prefer explicit names like `normalizedContact`, `normalizedEmail`, `savedStudent`, and `studentPage` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- Frontend exported props and schema types use `PascalCase`, such as `ListSearchInputProps` in `client/src/components/ui/list-search-input.tsx` and `StudentFormSchema` in `client/src/features/students/forms/studentFormSchema.ts`.
- Backend request/response contracts are Java records with `*RequestDTO`, `*ResponseDTO`, `*OptionsDTO`, and shared wrappers like `PageDTO<T>` in `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`.
## Code Style
- Frontend formatting is enforced indirectly through `eslint .` in `client/package.json` and the flat config in `client/eslint.config.js`.
- `prettier` is installed in `client/package.json`, but no checked-in Prettier config file was detected under `client/`; follow the surrounding file style instead of introducing mass reformatting.
- TypeScript strict mode is enabled in `client/tsconfig.app.json` with `strict: true`, while unused locals and parameters are not compile-blocking (`noUnusedLocals: false`, `noUnusedParameters: false`).
- No dedicated backend formatter or lint config was detected; backend style is established by the existing Java source layout in `server/api-aprimorar/src/main/java/` and tests in `server/api-aprimorar/src/test/java/`.
- Frontend ESLint extends `@eslint/js`, `typescript-eslint`, `react-hooks`, and `react-refresh` in `client/eslint.config.js`.
- Frontend warnings are configured for `@typescript-eslint/no-unused-vars` and `@typescript-eslint/no-explicit-any` in `client/eslint.config.js`; these are tolerated as warnings, not errors.
- Backend quality gates are test-oriented through Maven and JaCoCo in `server/api-aprimorar/pom.xml`; no Checkstyle, SpotBugs, or Spotless config was detected.
## Import Organization
- Use the `@/*` alias mapped to `client/src/*` in `client/tsconfig.app.json`.
- Prefer `import type` for type-only frontend imports, as seen in `client/src/features/events/forms/EventForm.tsx`, `client/src/components/layout/PageLayout.tsx`, and `client/src/features/address/components/AddressDetails.tsx`.
## Error Handling
- Frontend mutation handlers surface failures through toast notifications, usually with Portuguese messages or `getFriendlyErrorMessage(error)`, as in `client/src/features/events/hooks/use-event-mutations.ts`, `client/src/features/employees/hooks/emlpoyee-mutations.ts`, and `client/src/features/parents/hooks/parent-mutations.ts`.
- Frontend page-level failures are routed into dedicated UI states such as `ErrorBoundary` in `client/src/App.tsx` and shared error components under `client/src/components/ui/`.
- Frontend form validation should flow through Zod schemas and `zodResolver`, as in `client/src/features/students/pages/StudentCreatePage.tsx`, `client/src/features/events/pages/EventCreatePage.tsx`, and `client/src/features/students/forms/studentFormSchema.ts`.
- Backend services throw domain-specific exceptions and centralize response shaping in `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- Backend validation and business-rule messages are written in Portuguese, for example `"Aluno não encontrado no banco de dados"` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and handler responses in `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
## Logging
- Backend logging uses SLF4J via `LoggerFactory`, for example in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- Frontend still uses `console.error` in `client/src/lib/shared/api-errors.ts` and `client/src/lib/shared/api.ts`; there is no dedicated browser logging abstraction yet.
- Backend services log successful business actions with user-facing context, such as create/update/archive/delete flows in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- Backend exception handling logs errors centrally in `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`.
- Frontend should prefer shared error-message helpers over raw thrown text, using `getFriendlyErrorMessage` from `client/src/lib/shared/api-errors.ts`.
## Comments
- Existing code keeps comments sparse in production files and uses them mainly for TODOs or test section labels, for example `//TODO implementar o logging mais pra frente` in `client/src/lib/shared/api-errors.ts` and `/* ----- Query Methods ----- */` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- Tests sometimes use Arrange/Act/Assert comments for readability, especially in `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java`.
- Not detected in the sampled frontend files under `client/src/`.
- Backend documentation is annotation-driven rather than Javadoc-heavy, using Swagger annotations in controllers such as `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`.
## Function Design
- Keep frontend pages orchestration-focused and push reusable UI into child components or feature helpers, matching `client/src/features/students/pages/StudentsPage.tsx` and `client/src/features/events/pages/EventCreatePage.tsx`.
- Backend services keep public methods focused and move repeated lookups into private helpers such as `findStudentOrThrow` and `findParentOrThrow` in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
- Frontend form pages strongly type `useForm` with schema input types, for example `useForm<StudentFormSchema>` in `client/src/features/students/pages/StudentCreatePage.tsx`.
- Backend controllers accept `@Valid` request DTO records and `UUID` path variables, as in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`.
- Frontend mutation hooks usually return a bag of TanStack Query mutation objects, as in `client/src/features/employees/hooks/emlpoyee-mutations.ts` and `client/src/features/events/hooks/use-event-mutations.ts`.
- Backend services return DTOs or `PageDTO<T>` for reads and commands, and `void` for archive/delete actions, as in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`.
## Module Design
- Frontend prefers named exports for most components, hooks, and helpers, for example `StudentsPage`, `ListSearchInput`, and `useEventMutations`.
- `export default` is limited to special entry/config files such as `client/src/App.tsx` and `client/eslint.config.js`.
- Backend uses one public top-level type per file throughout `server/api-aprimorar/src/main/java/` and `server/api-aprimorar/src/test/java/`.
- No handwritten barrel files were detected in the sampled frontend features; imports usually point directly to implementation files.
- Generated exports under `client/src/kubb/` are consumed directly, for example `useGetStudents` in `client/src/features/students/pages/StudentsPage.tsx` and query-key helpers in `client/src/features/employees/hooks/emlpoyee-mutations.ts`.
<!-- GSD:conventions-end -->

<!-- GSD:architecture-start source:ARCHITECTURE.md -->
## Architecture

## Pattern Overview
- Keep backend business logic in domain packages under `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*` using controller → service → repository/mapper flow.
- Keep frontend user flows inside feature folders under `client/src/features/*`, with routes in `client/src/App.tsx` and shared primitives in `client/src/components/ui/*`.
- Treat `client/src/kubb/*` as generated integration code sourced from backend OpenAPI via `client/kubb.config.ts`; hand-written frontend code should consume it, not replace it.
## Layers
- Purpose: Bootstrap the SPA, install global providers, and map URLs to feature pages.
- Location: `client/src/main.tsx`, `client/src/App.tsx`, `client/src/components/layout/MainLayout.tsx`
- Contains: React root rendering, `QueryClientProvider`, `BrowserRouter`, lazy route registration, shared layout, toast/error boundaries.
- Depends on: `client/src/lib/shared/queryClient.ts`, `client/src/components/ui/error-boundary.tsx`, feature page modules.
- Used by: Every page under `client/src/features/*`.
- Purpose: Organize SPA behavior by business area.
- Location: `client/src/features/students/*`, `client/src/features/parents/*`, `client/src/features/employees/*`, `client/src/features/events/*`, `client/src/features/dashboard/*`, `client/src/features/address/*`
- Contains: `pages/`, `components/`, `forms/`, `hooks/` per feature.
- Depends on: Generated Kubb hooks in `client/src/kubb/hooks/*`, shared UI in `client/src/components/ui/*`, shared helpers in `client/src/lib/*`.
- Used by: Route definitions in `client/src/App.tsx` and cross-feature detail pages such as `client/src/features/parents/pages/ParentDetailPage.tsx`.
- Purpose: Centralize API contracts and shared client behavior.
- Location: `client/src/kubb/*`, `client/src/lib/shared/api.ts`, `client/src/lib/shared/api-errors.ts`, `client/src/lib/shared/queryClient.ts`
- Contains: Generated React Query hooks, generated DTO/Zod types, Axios instance, query defaults, API error formatting.
- Depends on: Backend OpenAPI at `http://localhost:8080/v3/api-docs` configured in `client/kubb.config.ts`.
- Used by: Feature pages, mutation wrappers, and forms across `client/src/features/*`.
- Purpose: Expose REST endpoints and map HTTP concerns to services.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Controller.java`, especially `StudentController.java`, `ParentController.java`, `EmployeeController.java`, `EventController.java`, `DashboardController.java`
- Contains: `@RestController` classes, path mappings under `/v1/*`, pageable/query parameter handling, request validation, status codes.
- Depends on: Domain services, DTO records, `com.aprimorar.api.shared.PageDTO`.
- Used by: SPA-generated clients in `client/src/kubb/hooks/*` and OpenAPI generation from `server/api-aprimorar/pom.xml`.
- Purpose: Own business rules, orchestration, transactions, normalization, and lifecycle side effects.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Service.java`
- Contains: CRUD orchestration, archive/unarchive behavior, conflict checks, ghost/phantom reassignment flows, dashboard aggregation.
- Depends on: Repositories, mappers, shared utilities, `Clock`, exceptions.
- Used by: Controllers only.
- Purpose: Persist aggregates and translate entities into API DTOs.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Repository.java`, `*Specifications.java`, `*Mapper.java`, entity classes like `Student.java`, `Event.java`, `Parent.java`, `Employee.java`
- Contains: JPA entities, repository interfaces, JPQL/specification queries, DTO converters, embedded value objects like `Address`.
- Depends on: Spring Data JPA, shared base classes in `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/*`.
- Used by: Domain services.
- Purpose: Provide cross-domain runtime behavior.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/*`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/*`, `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/*`
- Contains: CORS config, clock bean, page serialization mode, global exception mapping, `BaseEntity`, `PageDTO`, normalization helpers.
- Depends on: Spring Boot core/web/data features.
- Used by: All backend domains.
## Data Flow
- Server state lives in TanStack Query via `client/src/lib/shared/queryClient.ts` and generated hooks in `client/src/kubb/hooks/*`.
- Route-local UI state stays inside page components with `useState`, such as pagination/search state in `client/src/features/students/pages/StudentsPage.tsx` and `client/src/features/employees/pages/EmployeesPage.tsx`.
- Backend persistence state lives in PostgreSQL with Flyway migrations from `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql` and dev seed data in `server/api-aprimorar/src/main/resources/data.sql`.
## Key Abstractions
- Purpose: Keep UI orchestration, feature-specific widgets, and form logic together.
- Examples: `client/src/features/students/*`, `client/src/features/events/*`, `client/src/features/parents/*`
- Pattern: `pages/` orchestrate data loading, `components/` render domain UI, `forms/` hold schemas/form composition, `hooks/` wrap generated mutations.
- Purpose: Make the backend contract the source of truth for client types and hooks.
- Examples: `client/src/kubb/index.ts`, `client/src/kubb/hooks/student/useGetStudents.ts`, `client/src/kubb/zod/studentRequestDTOSchema.ts`
- Pattern: Generate from OpenAPI and consume from hand-written code; do not place custom business logic inside generated files.
- Purpose: Represent core business records with validation and lifecycle fields.
- Examples: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java`, `.../event/Event.java`, `.../parent/Parent.java`, `.../employee/Employee.java`
- Pattern: Entities extend `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/BaseEntity.java`, keep mutation methods like `updateDetails(...)`, and enforce invariants internally.
- Purpose: Standardize paginated responses sent to the SPA.
- Examples: `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`, generated frontend types such as `client/src/kubb/types/PageDTOStudentResponseDTO.ts`
- Pattern: Controllers/services return `PageDTO<T>` instead of raw Spring `Page` JSON.
- Purpose: Keep dynamic filtering and optimized fetch plans out of controllers/services.
- Examples: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentSpecifications.java`, `.../event/repository/EventRepository.java`, `.../parent/repository/ParentSpecifications.java`
- Pattern: Use Spring Data specifications for text/archive filters and repository-level JPQL for schedule conflicts, aggregations, and reassignment updates.
- Purpose: Preserve event referential integrity when deleting linked students or employees.
- Examples: constants in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and `.../employee/EmployeeService.java`, reassignment queries in `.../event/repository/EventRepository.java`
- Pattern: Services reassign foreign-key references before deleting primary records.
## Entry Points
- Location: `client/src/main.tsx`
- Triggers: Vite browser startup.
- Responsibilities: Mount React, install `QueryClientProvider`, load global CSS and Zod locale setup.
- Location: `client/src/App.tsx`
- Triggers: Browser navigation inside `BrowserRouter`.
- Responsibilities: Lazy-load feature pages, wrap the app in `ErrorBoundary` and `Suspense`, and attach `MainLayout` to all main routes.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`
- Triggers: Spring Boot startup.
- Responsibilities: Boot the API application context.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Controller.java`
- Triggers: HTTP requests to `/v1/students`, `/v1/parents`, `/v1/employees`, `/v1/events`, `/v1/dashboard/summary`.
- Responsibilities: Bind request data, call services, and shape HTTP responses.
- Location: `server/api-aprimorar/pom.xml`, `client/kubb.config.ts`, root `package.json`
- Triggers: `./mvnw -Pgenerate-openapi generate-resources`, `npm run sync`, `npm run start:frontend`.
- Responsibilities: Publish backend docs and regenerate `client/src/kubb/*`.
## Error Handling
- Backend services throw domain-specific exceptions from packages like `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/exception/*` and `.../event/exception/*`.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java` converts exceptions into `ProblemResponseDTO` payloads.
- Frontend pages short-circuit to `ErrorCard`/`LoadingCard` components, as in `client/src/features/parents/pages/ParentDetailPage.tsx` and `client/src/features/dashboard/DashboardPage.tsx`.
- A global React class boundary in `client/src/components/ui/error-boundary.tsx` catches render-time crashes outside query-level handling.
## Cross-Cutting Concerns
<!-- GSD:architecture-end -->

<!-- GSD:skills-start source:skills/ -->
## Project Skills

No project skills found. Add skills to any of: `.claude/skills/`, `.agents/skills/`, `.cursor/skills/`, or `.github/skills/` with a `SKILL.md` index file.
<!-- GSD:skills-end -->

<!-- GSD:workflow-start source:GSD defaults -->
## GSD Workflow Enforcement

Before using Edit, Write, or other file-changing tools, start work through a GSD command so planning artifacts and execution context stay in sync.

Use these entry points:
- `/gsd-quick` for small fixes, doc updates, and ad-hoc tasks
- `/gsd-debug` for investigation and bug fixing
- `/gsd-execute-phase` for planned phase work

Do not make direct repo edits outside a GSD workflow unless the user explicitly asks to bypass it.
<!-- GSD:workflow-end -->

<!-- GSD:profile-start -->
## Developer Profile

> Profile not yet configured. Run `/gsd-profile-user` to generate your developer profile.
> This section is managed by `generate-claude-profile` -- do not edit manually.
<!-- GSD:profile-end -->
