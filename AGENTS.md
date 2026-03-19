# AGENTS.md
Operational guide for coding agents working in this repository.

## Scope and context
- Project: educational operations system (students, parents, employees, events, dashboard).
- Architecture: React SPA in `client/` consuming Spring Boot API in `server/api-aprimorar/`.
- Database: PostgreSQL (local Docker Compose for development).
- Default language for user-facing text and validation messages: Portuguese.
- There is no single root build command; run commands from each app directory.

## Repository map
- `client/`: React 19 + TypeScript + Vite frontend.
- `server/api-aprimorar/`: Java 21 + Spring Boot 3.5 backend.
- `docs/`: planning/refactor documentation and automation docs.
- `server/api-aprimorar/src/main/resources/db/migration/`: Flyway SQL migrations.

Important frontend paths:
- `client/src/features/**`: route pages and feature-specific code.
- `client/src/components/ui/**`: reusable UI primitives.
- `client/src/components/layout/**`: layout/shell components.
- `client/src/lib/schemas/**`: Zod schemas and inferred types.
- `client/src/services/api.ts`: Axios client and API wrappers.
- `client/src/services/api-errors.ts`: friendly error mapping.

Important backend paths:
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/**`: domain modules.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/config/**`: Spring configuration.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/**`: global exception handling.
- `server/api-aprimorar/src/test/java/**`: JUnit tests.

## Cursor/Copilot rules status
Checked locations:
- `.cursorrules`: not present.
- `.cursor/rules/`: not present.
- `.github/copilot-instructions.md`: not present.

If any of these files are added later, treat them as higher-priority constraints and update this guide.

## Build, lint, test commands

### Frontend (`client/`)
- Install dependencies: `npm install`
- Start dev server: `npm run dev`
- Lint all frontend code: `npm run lint`
- Production build (includes TypeScript project build): `npm run build`
- Preview built app: `npm run preview`

Frontend testing status:
- No `test` script currently configured in `client/package.json`.
- Do not invent frontend test commands in automation.
- Use `npm run build` as the strongest validation gate for frontend changes.

### Backend (`server/api-aprimorar/`)
- Start local Postgres: `docker compose up -d db`
- Stop local Postgres: `docker compose down`
- Run API locally: `./mvnw spring-boot:run`
- Run all tests: `./mvnw test`
- Run full verification lifecycle: `./mvnw verify`
- Build package artifact: `./mvnw package`

Single-test commands (backend, Maven Surefire):
- Single test class: `./mvnw -Dtest=StudentServiceTest test`
- Single test method: `./mvnw -Dtest=StudentServiceTest#shouldCreateStudentUsingExistingParentWhenParentIdIsInformed test`
- Spring context smoke test class: `./mvnw -Dtest=ApiAprimorarApplicationTests test`
- Spring context smoke test method: `./mvnw -Dtest=ApiAprimorarApplicationTests#contextLoads test`

## Verification expectations
- Prefer the narrowest command that proves your change.
- Frontend UI/schema/service changes: run `npm run lint` and `npm run build`.
- Backend service/controller/repository changes: run at least `./mvnw test`.
- Persistence/migration changes: ensure DB is running and execute `./mvnw test`.
- Cross-stack contract changes: run both `./mvnw test` and `npm run build`.

## Frontend code style

### Imports and modules
- Prefer alias imports with `@/` instead of deep relative imports.
- Use `import type` for type-only imports.
- Keep import groups stable and avoid unnecessary reordering noise.
- Follow existing quote/formatting style per file (current codebase is mixed; preserve local style).

### Formatting and structure
- Keep files in TypeScript (`.ts`/`.tsx`); avoid introducing `.js`/`.jsx`.
- Prefer named exports for shared helpers/components; keep consistency with nearby code.
- Feature pages use `*Page.tsx` naming under `client/src/features/**`.
- Reusable UI should live in `client/src/components/ui/`.
- Keep route-level lazy loading patterns consistent with `client/src/App.tsx`.

### Types and schemas
- Model API contracts with Zod and parse API responses before use.
- Prefer `z.infer<typeof schema>` for app-level types.
- Reuse shared types (e.g., paginated response types) instead of duplicating interfaces.
- Keep strict typing in public functions/hooks; avoid `any`.

### Naming conventions
- Components/types: PascalCase.
- Functions/variables/hooks: camelCase.
- Constants: UPPER_SNAKE_CASE.
- Query keys centralized in `client/src/lib/query/queryKeys.ts`.

### Error handling
- Map network/validation errors through `getFriendlyErrorMessage(error)`.
- In async UI flows, expose explicit loading/error/empty states.
- Prefer `try/catch` around imperative async workflows and mutation side effects.
- Log diagnostics with `console.error` when useful for troubleshooting.

### React Query patterns
- Keep query keys deterministic and colocated in `queryKeys`.
- Invalidate affected keys after create/update/delete mutations.
- Use `enabled` for queries dependent on route params.
- Use placeholder/previous data patterns where pagination UX benefits.

## Backend code style

### Architecture and layering
- Keep HTTP concerns in controllers.
- Keep business logic in services and `*Rules` helpers.
- Keep persistence logic in repositories/specifications.
- Keep exception-to-HTTP mapping centralized in `GlobalExceptionHandler`.

### Java and Spring conventions
- Use constructor injection with `final` dependencies.
- Use `ResponseEntity` in controllers for explicit status codes.
- Prefer immutable DTO records for request/response payloads.
- Put Jakarta Validation annotations on DTO fields.
- Keep `@Transactional(readOnly = true)` for query methods and `@Transactional` for commands.
- Use `UUID` IDs consistently across domain APIs.

### Imports, formatting, naming
- Keep explicit imports; avoid wildcard imports.
- Class/record names: PascalCase.
- Methods/fields/locals: camelCase.
- Repository classes end with `Repository`.
- Mapper classes end with `Mapper`.
- Exception classes end with `Exception` and should be domain-specific.

### Domain and error handling
- Throw specific domain exceptions (not generic `RuntimeException`) for business errors.
- Preserve API error envelope shape produced by `GlobalExceptionHandler` / `ApiError`.
- Do not swallow exceptions in empty catch blocks.
- Avoid broad `catch (Exception)` unless intentionally translating errors.
- Preserve Portuguese messages for validation/domain errors unless task scope requires change.

### Time and deterministic behavior
- Prefer injected `Clock` (`applicationClock`) for shared time-sensitive logic when feasible.
- Keep deterministic test data and fixed IDs/timestamps in tests when possible.

## Testing guidelines
- Backend tests use JUnit 5 + Mockito + AssertJ.
- Use `@DisplayName` with behavior-oriented names.
- Keep tests close to changed logic and assert observable behavior.
- Prefer focused unit/service tests before broad integration tests.
- For bug fixes, add or update at least one regression test.

## Change discipline for agents
- Keep diffs scoped; avoid unrelated refactors.
- Do not rename DTO fields/endpoints/contracts without full-stack alignment.
- For new persisted fields, update migration + entity + DTO + mapper + API consumer.
- For frontend-only work, confirm backend contract exists before inventing payloads.
- If commands/workflows/architecture change, update this `AGENTS.md` in the same task.
