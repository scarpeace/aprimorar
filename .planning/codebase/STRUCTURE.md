# Codebase Structure

**Analysis Date:** 2026-04-17

## Directory Layout

```text
aprimorar/
├── client/                     # React 19 + Vite SPA workspace
│   ├── src/
│   │   ├── components/         # Shared layout and UI primitives
│   │   ├── features/           # Domain-oriented frontend modules
│   │   ├── kubb/               # Generated API hooks, DTO types, Zod schemas
│   │   └── lib/                # Shared frontend infrastructure and utilities
│   ├── kubb.config.ts          # OpenAPI → client generation config
│   ├── vite.config.ts          # Vite config with `@` alias
│   └── package.json            # Frontend scripts and dependencies
├── server/
│   └── api-aprimorar/          # Spring Boot API workspace
│       ├── src/main/java/com/aprimorar/api/
│       │   ├── config/         # Spring configuration beans
│       │   ├── domain/         # Business modules by entity/use case
│       │   ├── exception/      # Global exception payloads/handlers
│       │   └── shared/         # Base entity, page wrapper, mapper utils
│       ├── src/main/resources/ # Spring config, Flyway, seed data
│       ├── src/test/java/      # Backend tests
│       └── pom.xml             # Maven build and OpenAPI generation
├── .planning/codebase/         # Generated codebase mapping documents
├── AGENTS.md                   # Repo-wide instructions
└── package.json                # Root dev orchestration scripts
```

## Directory Purposes

**`client/src/components/`:**
- Purpose: Hold UI shared across business areas.
- Contains: Layout wrappers and reusable controls.
- Key files: `client/src/components/layout/MainLayout.tsx`, `client/src/components/layout/PageLayout.tsx`, `client/src/components/ui/button.tsx`, `client/src/components/ui/pagination.tsx`, `client/src/components/ui/error-boundary.tsx`

**`client/src/features/`:**
- Purpose: Group SPA code by business area.
- Contains: Feature `pages/`, `components/`, `forms/`, and `hooks/`.
- Key files: `client/src/features/students/pages/StudentsPage.tsx`, `client/src/features/events/forms/EventForm.tsx`, `client/src/features/dashboard/DashboardPage.tsx`, `client/src/features/address/forms/addressSchema.ts`

**`client/src/kubb/`:**
- Purpose: Store generated frontend contract artifacts.
- Contains: `hooks/`, `types/`, `zod/`, `.kubb/`, and a large barrel file.
- Key files: `client/src/kubb/index.ts`, `client/src/kubb/hooks/student/useGetStudents.ts`, `client/src/kubb/types/PageDTOStudentResponseDTO.ts`

**`client/src/lib/`:**
- Purpose: Centralize frontend infrastructure that is not feature-owned.
- Contains: Shared API helpers, query client config, debounce hook, formatters, validation setup.
- Key files: `client/src/lib/shared/queryClient.ts`, `client/src/lib/shared/api.ts`, `client/src/lib/shared/api-errors.ts`, `client/src/lib/validations/zod.ts`

**`server/api-aprimorar/src/main/java/com/aprimorar/api/domain/`:**
- Purpose: Group backend code by domain area.
- Contains: One package per product area, usually with entity, controller, service, mapper, `dto/`, `repository/`, and `exception/`.
- Key files: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java`, `.../parent/ParentController.java`, `.../event/repository/EventRepository.java`, `.../dashboard/DashboardService.java`

**`server/api-aprimorar/src/main/java/com/aprimorar/api/config/`:**
- Purpose: Keep application-wide Spring beans and MVC configuration.
- Contains: Clock, CORS, and page serialization support.
- Key files: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/ClockConfig.java`, `.../WebCorsConfig.java`, `.../JacksonConfig.java`

**`server/api-aprimorar/src/main/java/com/aprimorar/api/exception/`:**
- Purpose: Centralize HTTP error payloads and mapping.
- Contains: Error code enum, global handler, problem response DTO.
- Key files: `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`, `.../ProblemResponseDTO.java`, `.../ErrorCode.java`

**`server/api-aprimorar/src/main/java/com/aprimorar/api/shared/`:**
- Purpose: Hold backend types reused across domains.
- Contains: Base JPA entity, page wrapper, mapper normalization helpers.
- Key files: `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/BaseEntity.java`, `.../PageDTO.java`, `.../MapperUtils.java`

**`server/api-aprimorar/src/main/resources/`:**
- Purpose: Keep runtime configuration and database bootstrap artifacts.
- Contains: `application.yml`, `application-dev.yml`, Flyway SQL, seed SQL.
- Key files: `server/api-aprimorar/src/main/resources/application.yml`, `.../application-dev.yml`, `.../db/migration/V1__init.sql`, `.../data.sql`

**`server/api-aprimorar/src/test/java/`:**
- Purpose: Hold backend automated tests.
- Contains: Application smoke tests, domain service/entity tests, exception handler tests.
- Key files: `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`, `.../event/EventServiceTest.java`, `.../exception/GlobalExceptionHandlerTest.java`

## Key File Locations

**Entry Points:**
- `client/src/main.tsx`: Frontend bootstrap and provider setup.
- `client/src/App.tsx`: Frontend router and lazy route registration.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/ApiAprimorarApplication.java`: Backend Spring Boot entry point.
- `package.json`: Root orchestration for concurrent backend/frontend startup.

**Configuration:**
- `client/vite.config.ts`: Vite plugin setup and `@` alias to `client/src`.
- `client/kubb.config.ts`: Kubb generation from backend OpenAPI.
- `client/eslint.config.js`: Frontend lint rules.
- `client/prettier.json`: Frontend formatter settings.
- `server/api-aprimorar/pom.xml`: Maven build, test, and OpenAPI generation config.
- `server/api-aprimorar/src/main/resources/application.yml`: Default Spring profile and springdoc/flyway setup.
- `server/api-aprimorar/src/main/resources/application-dev.yml`: Dev datasource/JPA/logging settings.

**Core Logic:**
- `client/src/features/*`: Frontend business flows and pages.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/*`: Backend entities, services, controllers, DTOs, repositories.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/PageDTO.java`: Shared backend pagination contract.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`: Shared backend error translation.

**Testing:**
- `server/api-aprimorar/src/test/java/com/aprimorar/api/`: Backend test root.
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentTest.java`: Entity behavior tests.
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java`: Service-layer tests.
- Frontend tests: Not detected in `client/`.

## Naming Conventions

**Files:**
- Frontend route pages use `*Page.tsx`: `client/src/features/students/pages/StudentsPage.tsx`
- Frontend feature forms use `*Schema.ts`/`*Form.tsx`: `client/src/features/students/forms/studentFormSchema.ts`, `client/src/features/events/forms/EventForm.tsx`
- Frontend hooks use `use*` or `*-mutations.ts`: `client/src/features/students/hooks/student-mutations.ts`, `client/src/lib/shared/use-debounce.ts`
- Backend controllers/services/mappers/entities use singular domain names with role suffixes: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentController.java`, `StudentService.java`, `StudentMapper.java`, `Student.java`
- Backend DTOs use `*RequestDTO`, `*ResponseDTO`, `*OptionsDTO`: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/event/dto/EventRequestDTO.java`

**Directories:**
- Frontend business directories are pluralized features: `client/src/features/students`, `client/src/features/parents`
- Frontend feature internals use stable subfolders: `pages/`, `components/`, `forms/`, `hooks/`
- Backend domain directories are singular nouns: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student`, `.../event`
- Backend nested support folders follow role-based names: `dto/`, `repository/`, `exception/`

## Where to Add New Code

**New frontend feature page or flow:**
- Primary code: `client/src/features/<feature>/pages/`
- Feature-specific child components: `client/src/features/<feature>/components/`
- Feature-specific forms and Zod schemas: `client/src/features/<feature>/forms/`
- Mutation wrappers around generated hooks: `client/src/features/<feature>/hooks/`

**New reusable frontend component/module:**
- Shared layout: `client/src/components/layout/`
- Reusable UI primitive: `client/src/components/ui/`
- Cross-feature helpers/query config/API helpers: `client/src/lib/shared/` or `client/src/lib/utils/`

**New backend domain capability:**
- Entity/service/controller/mapper: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/<domain>/`
- Request/response records: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/<domain>/dto/`
- Repository/specification code: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/<domain>/repository/`
- Domain-specific exceptions: `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/<domain>/exception/`

**New backend shared utility or infrastructure:**
- Shared JPA/API helpers: `server/api-aprimorar/src/main/java/com/aprimorar/api/shared/`
- App-wide config beans/MVC setup: `server/api-aprimorar/src/main/java/com/aprimorar/api/config/`
- Global exception behavior: `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/`

**New persistence change:**
- Schema migration: `server/api-aprimorar/src/main/resources/db/migration/`
- Dev seed adjustments: `server/api-aprimorar/src/main/resources/data.sql`

**New tests:**
- Backend domain tests: mirror package structure under `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/`
- Frontend tests: no current home is established; introducing one requires adding a frontend test runner first.

## Special Directories

**`client/src/kubb/`:**
- Purpose: Generated client code from backend OpenAPI.
- Generated: Yes
- Committed: Yes

**`server/api-aprimorar/src/main/resources/db/migration/`:**
- Purpose: Versioned Flyway schema migrations.
- Generated: No
- Committed: Yes

**`server/api-aprimorar/src/main/resources/data.sql`:**
- Purpose: Dev/test bootstrap seed data loaded by Spring SQL init in the dev profile.
- Generated: No
- Committed: Yes

**`server/api-aprimorar/target/`:**
- Purpose: Maven build output, generated OpenAPI artifacts, and reports.
- Generated: Yes
- Committed: No

**`client/dist/`:**
- Purpose: Vite production bundle output.
- Generated: Yes
- Committed: No

**`.planning/codebase/`:**
- Purpose: Repository mapping documents for other GSD workflows.
- Generated: Yes
- Committed: Yes

---

*Structure analysis: 2026-04-17*
