# AGENTS.md
Operational guide for coding agents working in this repository.

## Product and scope
- Aprimorar is an internal educational operations system.
- Main workflows: student registration, employee management, parent records, event scheduling, dashboard flows.
- Current architecture is API-first: React frontend talks directly to the Spring Boot backend.
- PostgreSQL is the runtime database.
- Keep user-facing strings and validation messages in Portuguese unless the surrounding feature is already English.

## Repository layout
- `client/`: React 19 + TypeScript + Vite SPA.
- `server/api-aprimorar/`: Java 21 + Spring Boot 3.5 backend.
- `docs/planning/`: roadmap, epic plans, prompt style guide, shared diagrams.
- `docs/refactor/`: refactor notes and review docs.
- There is no root build command; run commands from the correct app directory.
Important frontend paths:
- `client/src/features/**`: route-level screens such as `StudentsPage.tsx`.
- `client/src/components/ui/**`: shared UI primitives.
- `client/src/components/layout/**`: layout and navigation.
- `client/src/lib/schemas/**`: Zod schemas and inferred types.
- `client/src/services/api.ts`: Axios client, API wrappers, shared frontend error mapping.
Important backend paths:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/**`: domain packages containing controllers, services, repositories, DTOs, rules, mappers, exceptions.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/config/**`: Spring configuration.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/**`: shared API error handling.
- `server/api-aprimorar/src/main/resources/db/migration/**`: Flyway SQL migrations.
- `server/api-aprimorar/src/test/java/**`: backend tests.

## Agent rules discovered
- `.cursorrules`: not present.
- `.cursor/rules/`: not present.
- `.github/copilot-instructions.md`: not present.

## Canonical docs
Use these as source-of-truth before making planning or architecture changes:
- `AGENTS.md`
- `docs/planning/roadmap.md`
- `docs/planning/epics/E-000-template.md`
- `docs/planning/epics/*.md`
- `docs/planning/prompt-style-guide.md`
- `docs/planning/diagrams.md`
- `docs/refactor/*.md`
Documentation rules:
- Do not invent completed work.
- Keep epic and task IDs stable.
- Update docs when contracts, workflows, commands, or architecture meaningfully change.

## Build, lint, and test commands
### Frontend (`client/`)
- Install dependencies: `npm install`
- Start dev server: `npm run dev`
- Lint: `npm run lint`
- Build: `npm run build`
- Preview production build: `npm run preview`
Frontend testing notes:
- There is currently no `test` script in `client/package.json`.
- Do not invent a frontend test command in automation.
- `npm run build` runs `tsc -b` before Vite build, so it also validates TypeScript compilation.

### Backend (`server/api-aprimorar/`)
- Start local database: `docker compose up -d db`
- Stop local database: `docker compose down`
- Run application: `./mvnw spring-boot:run`
- Run all tests: `./mvnw test`
- Run full verification: `./mvnw verify`
- Build package: `./mvnw package`
Single-test commands:
- Single backend test class: `./mvnw -Dtest=ApiAprimorarApplicationTests test`
- Single backend test method: `./mvnw -Dtest=ApiAprimorarApplicationTests#contextLoads test`
- Replace the class and method names with the target test you add.

## Verification expectations
- Prefer the narrowest command that proves the change.
- Frontend UI or schema change: run `npm run lint` and `npm run build`.
- Backend logic change: run at least `./mvnw test`.
- Backend API contract change: run `./mvnw test` and `npm run build`.
- Migration or persistence change: start Postgres with Docker and run backend tests.

## Frontend coding conventions
Language and structure:
- Use TypeScript everywhere; do not add app code in `.js` or `.jsx`.
- Prefer named exports for components and helpers.
- Keep route screens in feature folders and use `*Page.tsx` naming.
- Keep feature-local styling in `*.module.css`.
- Put reusable primitives under `client/src/components/ui/`.
- Put schemas in `client/src/lib/schemas/` and derive types with `z.infer`.
Imports:
- Use the `@/` alias instead of deep relative imports.
- Use `import type` for type-only imports when possible.
- Keep imports grouped logically: React/router, shared UI, schemas/services, local modules.
- Match the file's existing quote and import ordering style; do not reformat unrelated files.
Formatting and types:
- Follow the existing semicolon-free style in frontend files.
- Prefer explicit state types when inference is unclear, for example `useState<string | null>(null)`.
- Keep schema names descriptive, such as `createStudentSchema` and `studentResponseSchema`.
- Reuse shared API types like `PageResponse<T>` instead of duplicating shapes.
- Let TypeScript strictness guide the implementation; the app uses `strict`, `noUnusedLocals`, and `noUnusedParameters`.
Naming and UX:
- Components: PascalCase.
- Hooks, functions, variables: camelCase.
- True constants: UPPER_SNAKE_CASE.
- CSS module class names: camelCase.
- Keep identifiers in English unless a local domain convention clearly uses Portuguese.
- Keep user-facing copy in Portuguese unless the feature already uses English.
State and error handling:
- Follow the established `loading / error / data` page pattern.
- Wrap async loads and submissions in `try/catch/finally`.
- Reset stale error state before retries.
- Use `getFriendlyErrorMessage(error)` for API-facing errors.
- Keep `console.error(...)` for developer diagnostics.
- Prefer explicit loading, empty, and error states over silent failures.
- Reuse existing UI helpers such as `LoadingState`, `ErrorState`, and `EmptyState`.

## Backend coding conventions
Architecture and layering:
- Follow the current package layout under `domain/<area>/`.
- Keep HTTP concerns in controllers.
- Keep business rules in services and `*Rules` helpers.
- Keep persistence concerns in repositories and specifications.
- Keep shared exception translation in `GlobalExceptionHandler`.
Spring and Java patterns:
- Prefer constructor injection with `private final` fields.
- Use `ResponseEntity` in controllers to make status codes explicit.
- Use DTO records for request and response payloads where appropriate.
- Put Jakarta Validation annotations on DTO fields instead of scattering checks across controllers.
- Use `UUID` where the domain model already uses it.
- Inject `Clock` for time-sensitive shared logic instead of calling the system clock directly.
Naming:
- Classes and records: PascalCase.
- Methods, fields, locals: camelCase.
- Repositories end with `Repository`.
- Mappers end with `Mapper`.
- Exception classes use precise names such as `StudentNotFoundException`.
- Validation helpers and domain policies use descriptive names such as `StudentRules` and `AddressRules`.
Error handling:
- Throw specific domain exceptions instead of generic runtime failures.
- Let `GlobalExceptionHandler` map exceptions into the API error response.
- Preserve the existing structured error format from `ApiError`.
- Do not swallow exceptions with empty catch blocks.
- Do not catch broad `Exception` in services unless you are translating or recovering intentionally.
- Preserve Portuguese validation and domain error messages unless the task explicitly changes them.

## Testing conventions
- Backend tests use JUnit 5 and Spring Boot test support.
- Use `@DisplayName` for readable test intent.
- Add tests near the layer you changed.
- When adding service tests, prefer deterministic inputs and fixed timestamps.
- If you introduce clock-sensitive logic, stub or inject `Clock` so assertions stay stable.

## Change discipline
- Keep changes scoped; avoid drive-by cleanup.
- Do not rename endpoints, DTO fields, or schema fields without checking full-stack impact.
- For backend API changes, inspect both `server/api-aprimorar/` and `client/src/services/api.ts` plus affected schemas/pages.
- For new persisted fields, update migration, entity, DTO, mapper, validation, and frontend consumers together.
- For frontend-only changes, confirm the backend contract already exists before inventing payload shapes.
- If a code change makes docs stale, update the docs in the same task.
