# Architecture

**Analysis Date:** 2026-04-17

## Pattern Overview

**Overall:** Full-stack monorepo with a generated-contract boundary between a Spring Boot backend and a React SPA.

**Key Characteristics:**
- Keep backend business logic in domain packages under `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*` using controller → service → repository/mapper flow.
- Keep frontend user flows inside feature folders under `client/src/features/*`, with routes in `client/src/App.tsx` and shared primitives in `client/src/components/ui/*`.
- Treat `client/src/kubb/*` as generated integration code sourced from backend OpenAPI via `client/kubb.config.ts`; hand-written frontend code should consume it, not replace it.

## Layers

**Frontend shell and routing:**
- Purpose: Bootstrap the SPA, install global providers, and map URLs to feature pages.
- Location: `client/src/main.tsx`, `client/src/App.tsx`, `client/src/components/layout/MainLayout.tsx`
- Contains: React root rendering, `QueryClientProvider`, `BrowserRouter`, lazy route registration, shared layout, toast/error boundaries.
- Depends on: `client/src/lib/shared/queryClient.ts`, `client/src/components/ui/error-boundary.tsx`, feature page modules.
- Used by: Every page under `client/src/features/*`.

**Frontend feature modules:**
- Purpose: Organize SPA behavior by business area.
- Location: `client/src/features/students/*`, `client/src/features/parents/*`, `client/src/features/employees/*`, `client/src/features/events/*`, `client/src/features/dashboard/*`, `client/src/features/address/*`
- Contains: `pages/`, `components/`, `forms/`, `hooks/` per feature.
- Depends on: Generated Kubb hooks in `client/src/kubb/hooks/*`, shared UI in `client/src/components/ui/*`, shared helpers in `client/src/lib/*`.
- Used by: Route definitions in `client/src/App.tsx` and cross-feature detail pages such as `client/src/features/parents/pages/ParentDetailPage.tsx`.

**Frontend integration layer:**
- Purpose: Centralize API contracts and shared client behavior.
- Location: `client/src/kubb/*`, `client/src/lib/shared/api.ts`, `client/src/lib/shared/api-errors.ts`, `client/src/lib/shared/queryClient.ts`
- Contains: Generated React Query hooks, generated DTO/Zod types, Axios instance, query defaults, API error formatting.
- Depends on: Backend OpenAPI at `http://localhost:8080/v3/api-docs` configured in `client/kubb.config.ts`.
- Used by: Feature pages, mutation wrappers, and forms across `client/src/features/*`.

**Backend HTTP layer:**
- Purpose: Expose REST endpoints and map HTTP concerns to services.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Controller.java`, especially `StudentController.java`, `ParentController.java`, `EmployeeController.java`, `EventController.java`, `DashboardController.java`
- Contains: `@RestController` classes, path mappings under `/v1/*`, pageable/query parameter handling, request validation, status codes.
- Depends on: Domain services, DTO records, `com.aprimorar.api.shared.PageDTO`.
- Used by: SPA-generated clients in `client/src/kubb/hooks/*` and OpenAPI generation from `server/api-aprimorar/pom.xml`.

**Backend domain services:**
- Purpose: Own business rules, orchestration, transactions, normalization, and lifecycle side effects.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Service.java`
- Contains: CRUD orchestration, archive/unarchive behavior, conflict checks, ghost/phantom reassignment flows, dashboard aggregation.
- Depends on: Repositories, mappers, shared utilities, `Clock`, exceptions.
- Used by: Controllers only.

**Backend persistence and mapping:**
- Purpose: Persist aggregates and translate entities into API DTOs.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Repository.java`, `*Specifications.java`, `*Mapper.java`, entity classes like `Student.java`, `Event.java`, `Parent.java`, `Employee.java`
- Contains: JPA entities, repository interfaces, JPQL/specification queries, DTO converters, embedded value objects like `Address`.
- Depends on: Spring Data JPA, shared base classes in `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/*`.
- Used by: Domain services.

**Backend shared infrastructure:**
- Purpose: Provide cross-domain runtime behavior.
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/*`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/*`, `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/*`
- Contains: CORS config, clock bean, page serialization mode, global exception mapping, `BaseEntity`, `PageDTO`, normalization helpers.
- Depends on: Spring Boot core/web/data features.
- Used by: All backend domains.

## Data Flow

**Read/list flow (SPA list page to paginated API):**

1. A route page such as `client/src/features/students/pages/StudentsPage.tsx` owns UI state like `searchTerm`, `currentPage`, and `showArchived`.
2. The page calls a generated hook such as `useGetStudents` from `client/src/kubb`, which uses the shared React Query client from `client/src/lib/shared/queryClient.ts`.
3. `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java` accepts pageable/query params and forwards them to `StudentService`.
4. `StudentService` composes a `StudentSpecifications` query, loads entities through `StudentRepository`, maps them through `StudentMapper`, and returns `PageDTO<StudentResponseDTO>` from `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`.
5. The page hands the result to a presentational table such as `client/src/features/students/components/StudentsTable.tsx` plus shared pagination UI in `client/src/components/ui/pagination.tsx`.

**Write flow (SPA form to persisted entity):**

1. A form page such as `client/src/features/students/pages/StudentCreatePage.tsx` validates user input with Zod schemas like `client/src/features/students/forms/studentFormSchema.ts`.
2. The page submits through a feature mutation wrapper such as `client/src/features/students/hooks/student-mutations.ts`, which delegates to generated Kubb mutations and invalidates query keys.
3. The backend controller validates the request DTO record, for example `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentRequestDTO.java`.
4. The service normalizes data with `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/MapperUtils.java`, resolves related entities, enforces uniqueness/conflict rules, and saves through the repository.
5. The mapper converts the entity to a response DTO, and the frontend redirects/invalidate-caches on success.

**Contract regeneration flow:**

1. Backend controllers and DTOs define the API surface under `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*`.
2. Springdoc exposes `/v3/api-docs` as configured in `server/api-aprimorar/src/main/resources/application.yml`.
3. The Maven `generate-openapi` profile in `server/api-aprimorar/pom.xml` and Kubb config in `client/kubb.config.ts` generate contract artifacts.
4. Generated files land in `client/src/kubb/*` and are imported by hand-written feature code.

**State Management:**
- Server state lives in TanStack Query via `client/src/lib/shared/queryClient.ts` and generated hooks in `client/src/kubb/hooks/*`.
- Route-local UI state stays inside page components with `useState`, such as pagination/search state in `client/src/features/students/pages/StudentsPage.tsx` and `client/src/features/employees/pages/EmployeesPage.tsx`.
- Backend persistence state lives in PostgreSQL with Flyway migrations from `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql` and dev seed data in `server/api-aprimorar/src/main/resources/data.sql`.

## Key Abstractions

**Feature folder:**
- Purpose: Keep UI orchestration, feature-specific widgets, and form logic together.
- Examples: `client/src/features/students/*`, `client/src/features/events/*`, `client/src/features/parents/*`
- Pattern: `pages/` orchestrate data loading, `components/` render domain UI, `forms/` hold schemas/form composition, `hooks/` wrap generated mutations.

**Generated API boundary:**
- Purpose: Make the backend contract the source of truth for client types and hooks.
- Examples: `client/src/kubb/index.ts`, `client/src/kubb/hooks/student/useGetStudents.ts`, `client/src/kubb/zod/studentRequestDTOSchema.ts`
- Pattern: Generate from OpenAPI and consume from hand-written code; do not place custom business logic inside generated files.

**Domain aggregate/entity:**
- Purpose: Represent core business records with validation and lifecycle fields.
- Examples: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java`, `.../event/Event.java`, `.../parent/Parent.java`, `.../employee/Employee.java`
- Pattern: Entities extend `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/BaseEntity.java`, keep mutation methods like `updateDetails(...)`, and enforce invariants internally.

**PageDTO wrapper:**
- Purpose: Standardize paginated responses sent to the SPA.
- Examples: `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`, generated frontend types such as `client/src/kubb/types/PageDTOStudentResponseDTO.ts`
- Pattern: Controllers/services return `PageDTO<T>` instead of raw Spring `Page` JSON.

**Repository specification/query abstraction:**
- Purpose: Keep dynamic filtering and optimized fetch plans out of controllers/services.
- Examples: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentSpecifications.java`, `.../event/repository/EventRepository.java`, `.../parent/repository/ParentSpecifications.java`
- Pattern: Use Spring Data specifications for text/archive filters and repository-level JPQL for schedule conflicts, aggregations, and reassignment updates.

**Ghost/phantom record fallback:**
- Purpose: Preserve event referential integrity when deleting linked students or employees.
- Examples: constants in `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and `.../employee/EmployeeService.java`, reassignment queries in `.../event/repository/EventRepository.java`
- Pattern: Services reassign foreign-key references before deleting primary records.

## Entry Points

**SPA bootstrap:**
- Location: `client/src/main.tsx`
- Triggers: Vite browser startup.
- Responsibilities: Mount React, install `QueryClientProvider`, load global CSS and Zod locale setup.

**SPA router:**
- Location: `client/src/App.tsx`
- Triggers: Browser navigation inside `BrowserRouter`.
- Responsibilities: Lazy-load feature pages, wrap the app in `ErrorBoundary` and `Suspense`, and attach `MainLayout` to all main routes.

**Backend application:**
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`
- Triggers: Spring Boot startup.
- Responsibilities: Boot the API application context.

**Backend REST surface:**
- Location: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*/*Controller.java`
- Triggers: HTTP requests to `/v1/students`, `/v1/parents`, `/v1/employees`, `/v1/events`, `/v1/dashboard/summary`.
- Responsibilities: Bind request data, call services, and shape HTTP responses.

**Contract/codegen pipeline:**
- Location: `server/api-aprimorar/pom.xml`, `client/kubb.config.ts`, root `package.json`
- Triggers: `./mvnw -Pgenerate-openapi generate-resources`, `npm run sync`, `npm run start:frontend`.
- Responsibilities: Publish backend docs and regenerate `client/src/kubb/*`.

## Error Handling

**Strategy:** Centralized backend exception mapping plus frontend page/component fallbacks.

**Patterns:**
- Backend services throw domain-specific exceptions from packages like `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/exception/*` and `.../event/exception/*`.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java` converts exceptions into `ProblemResponseDTO` payloads.
- Frontend pages short-circuit to `ErrorCard`/`LoadingCard` components, as in `client/src/features/parents/pages/ParentDetailPage.tsx` and `client/src/features/dashboard/DashboardPage.tsx`.
- A global React class boundary in `client/src/components/ui/error-boundary.tsx` catches render-time crashes outside query-level handling.

## Cross-Cutting Concerns

**Logging:** Backend services/controllers log business events with SLF4J in files such as `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` and `.../event/EventService.java`; frontend API helpers currently log through `console.error` in `client/src/lib/shared/api.ts` and `client/src/lib/shared/api-errors.ts`.

**Validation:** Backend request DTOs use Bean Validation annotations in files like `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentRequestDTO.java` and `.../event/dto/EventRequestDTO.java`; frontend forms extend generated schemas with Zod in files like `client/src/features/students/forms/studentFormSchema.ts` and `client/src/features/address/forms/addressSchema.ts`.

**Authentication:** Not detected. The SPA calls the API directly through generated hooks and Axios without auth middleware, and the backend exposes controllers without Spring Security configuration.

---

*Architecture analysis: 2026-04-17*
