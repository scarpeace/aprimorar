# AGENTS.md
Operational guide for coding agents working in this repository.

## Product context
- Aprimorar is an internal educational operations system.
- Main flows include student registration, employee management, parent records, event scheduling, and dashboards.
- Architecture is API-first: React SPA (`client/`) consumes Spring Boot APIs (`server/api-aprimorar/`).
- Runtime database is PostgreSQL.
- Keep user-facing text and validation messages in Portuguese unless the surrounding feature is already in English.

## Repository map
- `client/`: React 19 + TypeScript + Vite frontend.
- `server/api-aprimorar/`: Java 21 + Spring Boot 3.5 backend.
- `docs/planning/`: roadmap, epics, prompt style guide, diagrams.
- `docs/refactor/`: refactor notes and review docs.
- There is no root build command; always run commands from the target app directory.

Important frontend paths:
- `client/src/features/**`: route-level screens (for example `StudentsPage.tsx`).
- `client/src/components/ui/**`: shared reusable primitives.
- `client/src/components/layout/**`: app shell and navigation.
- `client/src/lib/schemas/**`: Zod schemas and inferred types.
- `client/src/services/api.ts`: Axios client, API wrappers, friendly error mapping.

Important backend paths:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/**`: controllers, services, repositories, DTOs, rules, mappers, exceptions.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/config/**`: Spring configuration.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/**`: shared API exception handling.
- `server/api-aprimorar/src/main/resources/db/migration/**`: Flyway SQL migrations.
- `server/api-aprimorar/src/test/java/**`: backend tests.

## Rule files (checked)
- `.cursorrules`: not present.
- `.cursor/rules/`: not present.
- `.github/copilot-instructions.md`: not present.

## Canonical docs
Use these as source-of-truth before planning or architecture changes:
- `AGENTS.md`
- `docs/planning/roadmap.md`
- `docs/planning/epics/E-000-template.md`
- `docs/planning/epics/*.md`
- `docs/planning/prompt-style-guide.md`
- `docs/planning/diagrams.md`
- `docs/refactor/*.md`

Documentation discipline:
- Do not invent completed work.
- Keep epic/task IDs stable.
- Update docs when commands, workflows, contracts, or architecture meaningfully change.

## Build, lint, and test commands
### Frontend (`client/`)
- Install deps: `npm install`
- Dev server: `npm run dev`
- Lint: `npm run lint`
- Build (includes TypeScript check): `npm run build`
- Preview build: `npm run preview`

Frontend testing notes:
- `client/package.json` currently has no `test` script.
- Do not invent frontend test commands in automation.
- Use `npm run build` as the TypeScript validation gate (`tsc -b && vite build`).

### Backend (`server/api-aprimorar/`)
- Start local Postgres: `docker compose up -d db`
- Stop local Postgres: `docker compose down`
- Run API locally: `./mvnw spring-boot:run`
- Run all tests: `./mvnw test`
- Run full verification: `./mvnw verify`
- Build package artifact: `./mvnw package`

Single-test commands (backend):
- Single test class: `./mvnw -Dtest=ApiAprimorarApplicationTests test`
- Single test method: `./mvnw -Dtest=ApiAprimorarApplicationTests#contextLoads test`
- Replace class and method names with the real target under `src/test/java`.

### Planning automation (`ops/`)
- Validate sprint contract: `python3 ops/sprint.py validate --file sprint.md`
- Dry-run sync (no writes): `python3 ops/sprint.py sync --file sprint.md --dry-run`
- Apply sync to GitHub: `python3 ops/sprint.py sync --file sprint.md --apply`
- Requires `gh` auth scopes: `repo`, `read:project`, `project`
- If scope is missing: `gh auth refresh -s repo -s read:project -s project`

## Verification expectations
- Prefer the narrowest command that proves the change.
- Frontend UI/schema updates: run `npm run lint` and `npm run build`.
- Backend logic changes: run at least `./mvnw test`.
- Backend API contract changes: run `./mvnw test` and `npm run build`.
- Migration/persistence changes: start Postgres with Docker and run backend tests.

## Frontend coding conventions
Language and structure:
- Use TypeScript everywhere; do not add app code in `.js`/`.jsx`.
- Prefer named exports for components and helpers.
- Keep route screens in feature folders with `*Page.tsx` naming.
- Keep feature-local styles in `*.module.css`.
- Put reusable primitives in `client/src/components/ui/`.
- Put schemas in `client/src/lib/schemas/` and derive types with `z.infer`.

Imports:
- Prefer the `@/` alias over deep relative imports.
- Use `import type` for type-only imports.
- Keep imports grouped logically (React/router, shared UI, schemas/services, local modules).
- Match existing import ordering and quote style; avoid unrelated reformatting.

Formatting and types:
- Follow existing semicolon-free style in frontend files.
- Prefer explicit state types when inference is unclear (for example `useState<string | null>(null)`).
- Reuse shared API shapes like `PageResponse<T>` instead of duplicating types.
- Keep schema names descriptive (for example `createStudentSchema`, `studentResponseSchema`).
- Respect strict TypeScript settings (`strict`, `noUnusedLocals`, `noUnusedParameters`).

Naming and UX:
- Components: PascalCase.
- Hooks/functions/variables: camelCase.
- Constants: UPPER_SNAKE_CASE.
- CSS module class names: camelCase.
- Keep identifiers in English unless domain language strongly favors Portuguese.
- Keep user-facing copy in Portuguese unless that feature is already English.

State and error handling:
- Follow `loading / error / data` patterns on pages.
- Wrap async flows in `try/catch/finally`.
- Clear stale errors before retries.
- Use `getFriendlyErrorMessage(error)` for API-facing messages.
- Keep `console.error(...)` for diagnostics.
- Prefer explicit loading/empty/error states over silent failures.
- Reuse `LoadingState`, `ErrorState`, and `EmptyState` when possible.

## Backend coding conventions
Architecture and layering:
- Follow package organization under `domain/<area>/`.
- Keep HTTP concerns in controllers.
- Keep business rules in services and `*Rules` helpers.
- Keep persistence concerns in repositories/specifications.
- Keep cross-cutting API exception mapping in `GlobalExceptionHandler`.

Spring and Java patterns:
- Prefer constructor injection with `private final` fields.
- Use `ResponseEntity` in controllers for explicit status codes.
- Prefer DTO records for request/response payloads where suitable.
- Put Jakarta Validation annotations on DTO fields.
- Use `UUID` where the domain already uses UUID identifiers.
- Inject `Clock` for shared time-sensitive logic instead of direct system clock calls.

Naming and errors:
- Classes/records: PascalCase; methods/fields/locals: camelCase.
- Repository names end with `Repository`; mapper names end with `Mapper`.
- Use precise exception names (for example `StudentNotFoundException`).
- Throw domain-specific exceptions, not generic runtime failures.
- Preserve existing `ApiError` response structure via global exception handling.
- Do not swallow exceptions with empty catch blocks.
- Avoid broad `catch (Exception)` unless intentionally translating/recovering.
- Preserve Portuguese validation/domain messages unless task scope changes them.

## Testing conventions
- Backend tests use JUnit 5 and Spring Boot test support.
- Use `@DisplayName` for readable intent.
- Add tests close to the layer you changed.
- Keep service tests deterministic (fixed inputs/timestamps).
- If logic depends on time, inject/stub `Clock`.

## Change discipline
- Keep changes scoped; avoid drive-by refactors.
- Do not rename endpoints/DTO fields/schema fields without full-stack checks.
- For backend API changes, inspect both backend domain code and `client/src/services/api.ts`.
- For new persisted fields, update migration + entity + DTO + mapper + validation + frontend consumers.
- For frontend-only changes, confirm the backend contract exists before inventing payload shapes.
- If implementation changes make docs stale, update docs in the same task.
