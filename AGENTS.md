# AGENTS.md

Operational guide for coding agents working in the Aprimorar repository.

## Project Architecture Snapshot
- **Frontend**: React 19 + TypeScript + Vite SPA located in `client/`
- **Backend**: Spring Boot 3.5 + Java 21 API located in `server/api-aprimorar/`
- **Database**: PostgreSQL (local dev via Docker) with Flyway migrations in `server/api-aprimorar/src/main/resources/db/migration/`
- **API Contracts**: OpenAPI definitions generate React hooks, types, and schemas using Kubb into `client/src/kubb/`
- **Language**: User-facing strings, validation messages, and API error messages MUST be written in Portuguese

## Build, Lint, and Test Commands

### Full-Stack & Contracts
- **DB (Docker)**: `docker compose up -d db` (run from `server/api-aprimorar/` before backend starts)
- **Sync Contracts**: 
  1. Generate OpenAPI spec: `./mvnw -Pgenerate-openapi generate-resources` in backend.
  2. Generate React hooks/schemas: `npm run sync` in `client/`.

### Frontend (`client/`)
- **Install**: `npm install`
- **Dev Server**: `npm run dev` (starts Vite dev server)
- **Build & Type Check**: `npm run build` (Runs `tsc -b && vite build`)
- **Lint**: `npm run lint` (ESLint on `.ts`/`.tsx` files; uses `typescript-eslint` and `react-hooks` rules)
- **Preview**: `npm run preview`
- **Tests**: No dedicated frontend test script is configured right now.

### Backend (`server/api-aprimorar/`)
- **Run Locally**: `./mvnw spring-boot:run`
- **Build / Package**: `./mvnw package`
- **Test (All)**: `./mvnw test`
- **Verify (Tests + Integration)**: `./mvnw verify` (Includes Jacoco coverage report generation at `target/site/jacoco/index.html`)

### Running a Single Test (Backend)
Prefer focused Maven runs while iterating to keep feedback loops fast:
- **Specific Class**: `./mvnw -Dtest=ParentServiceTest test`
- **Specific Method**: `./mvnw -Dtest=ParentServiceTest#shouldReturnPagedParents test`
- **Wildcard Match**: `./mvnw -Dtest='*ServiceTest' test`

## Code Style Guidelines

### 1. General Workflow
- **Scope**: Keep diffs tightly scoped to the user request. Avoid drive-by refactors of unrelated code.
- **Local Style**: Preserve the local style of touched files. Do not mass-reformat unrelated lines just to normalize style.
- **Root Causes**: Prefer fixing root causes over unsafe casts, schema loosening, or one-off workarounds.
- **Artifacts**: When persistence or API contracts change, update backend, generated artifacts, and frontend consumers in the same task when applicable.

### 2. Frontend Guidelines (React/TS)
- **Component Structure**: 
  - Route pages use `*Page.tsx`
  - Feature-specific UI belongs under `client/src/features/<feature>/components/`
  - Shared reusable UI belongs under `client/src/components/ui/`
  - Shared utilities and infrastructure live under `client/src/lib/`
  - Keep page components focused on orchestration; move reusable logic into hooks, helpers, or child components
- **Imports and Modules**: 
  - Prefer import order of React/core, third-party libraries, `@/` aliases, then relative imports. 
  - Use `import type` for type-only imports.
  - Prefer `@/` aliases for cross-feature imports; relative imports are fine inside a single feature.
  - Prefer named exports for components, hooks, and helpers, but preserve existing exceptions such as `client/src/App.tsx`.
- **Formatting**: 
  - Follow the formatting style already used in the touched file; semicolons and import ordering are not fully uniform across the app.
  - Reuse shared UI primitives, existing Tailwind patterns, and established feature layouts before creating new abstractions.
- **Types & Forms**: 
  - Use Zod schemas and inferred types at form/API boundaries. 
  - React Hook Form typically uses `zodResolver(schema)` and `mode: "onBlur"` when it matches existing patterns. 
  - Treat generated Kubb types (`client/src/kubb/`) as the source of truth for contracts. NEVER hand-edit `src/kubb/`.
  - Prefer explicit props types near the top of the file.
- **State & Error Handling**: 
  - Use TanStack Query for server state with stable query keys instead of ad-hoc string keys. 
  - Use `enabled` for queries that depend on route params or deferred inputs.
  - Invalidate relevant queries after successful mutations.
  - Prefer Portuguese UI errors and shared helpers like `getFriendlyErrorMessage` when the touched code already follows that pattern.
  - Avoid unsafe casts (`as unknown as ...`) unless fully understood.

### 3. Backend Guidelines (Java/Spring)
- **Architecture Layers**: 
  - Controllers should stay focused on HTTP concerns only.
  - Services own business rules and transaction boundaries.
  - Repositories handle persistence and query definitions.
  - Mappers convert DTOs to and from entities; do not leak entities into controllers.
  - Domain packages commonly include `dto/`, `repository/`, `web/`, and `exception/`.
- **Java Conventions**: 
  - Use Java 21 features (Records for DTOs with Bean Validation annotations). 
  - Prefer constructor injection (via Lombok's `@RequiredArgsConstructor` or explicit). 
  - Use `@Transactional(readOnly = true)` for reads and `@Transactional` for writes. 
  - Favor explicit helper methods such as `findXOrThrow` and `resolveXOrThrow`.
- **Naming Conventions**: 
  - Classes use `PascalCase`; methods and fields use `camelCase`. 
  - Exception classes should be domain-specific and end with `Exception`.
- **Persistence & Contracts**: 
  - Entities generally follow shared UUID and audit timestamp conventions from base entities.
  - Keep repository queries explicit and intention-revealing. Use Specifications or explicit queries when filtering becomes dynamic.
  - API DTO field types must stay aligned with the OpenAPI contract consumed by Kubb.
  - Be explicit about timezone handling at API boundaries.
- **Error Handling**: 
  - Throw domain-specific exceptions instead of returning `null` or swallowing invalid states. 
  - Centralize HTTP error shaping in `GlobalExceptionHandler`, typically with `ProblemDetail`. 
  - Validation messages and business-rule messages MUST be in Portuguese.

### 4. Backend Testing Guidelines
- **Frameworks**: Tests use JUnit 5, AssertJ, and Mockito.
- **Structure**: Use `@Nested` classes when they improve organization. Prefer deterministic fixtures with fixed UUIDs and timestamps.
- **Assertions**: Assert both returned DTOs and important side effects such as repository calls, conflict checks, and archive behavior.

## Generated Code And Contract Changes
- `client/src/kubb/` is generated; never hand-edit it.
- If backend DTOs, endpoint signatures, or enums change, regenerate OpenAPI and Kubb in the same task.
- If generated schemas reject backend dates or enums, fix the backend DTO or mapper first.
- Do not patch generated frontend artifacts to compensate for backend contract bugs unless the user explicitly asks for a temporary workaround.

## Quick Checklist Before Finishing a Task
- [ ] Did you keep messages, validation text, and API-facing user copy in Portuguese?
- [ ] Did you preserve the touched file's existing style instead of normalizing unrelated code?
- [ ] Did you avoid editing `client/src/kubb/` manually?
- [ ] Did you run the appropriate build, lint, or targeted test commands for the change?

## Cursor and Copilot Rules
*Checked for additional repository instructions in: `.cursorrules`, `.cursor/rules/`, and `.github/copilot-instructions.md`.*

No Cursor or Copilot rule files are present in this repository today. Agents should strictly follow this `AGENTS.md` file.