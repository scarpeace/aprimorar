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
  1. Generate OpenAPI spec: `./mvnw -Pgenerate-openapi generate-resources` in backend
  2. Generate React hooks/schemas: `npm run sync` in `client/`

### Frontend (`client/`)
- **Install**: `npm install`
- **Dev Server**: `npm run dev`
- **Build & Type Check**: `npm run build` (Runs `tsc -b && vite build`)
- **Lint**: `npm run lint` (ESLint with `typescript-eslint` and `react-hooks` rules)
- **Preview**: `npm run preview`
- **Tests**: No dedicated frontend test script is configured right now

### Backend (`server/api-aprimorar/`)
- **Run Locally**: `./mvnw spring-boot:run`
- **Build / Package**: `./mvnw package`
- **Test (All)**: `./mvnw test`
- **Verify (Tests + Coverage)**: `./mvnw verify` (Jacoco report at `target/site/jacoco/index.html`)

### Running a Single Test (Backend)
Prefer focused Maven runs while iterating to keep feedback loops fast:
- **Specific Class**: `./mvnw -Dtest=ParentServiceTest test`
- **Specific Method**: `./mvnw -Dtest=ParentServiceTest#shouldReturnPagedParents test`
- **Wildcard Match**: `./mvnw -Dtest='*ServiceTest' test`

## Code Style Guidelines

### 1. General Workflow
- **Scope**: Keep diffs tightly scoped to the user request. Avoid drive-by refactors of unrelated code
- **Local Style**: Preserve the local style of touched files; do not mass-reformat unrelated lines
- **Root Causes**: Prefer fixing root causes over unsafe casts, schema loosening, or one-off workarounds
- **Artifacts**: When persistence or API contracts change, update backend, generated artifacts, and frontend consumers in the same task

### 2. Frontend Guidelines (React/TS)

#### Component Structure
- Route pages use `*Page.tsx`
- Feature-specific UI: `client/src/features/<feature>/components/`
- Shared reusable UI: `client/src/components/ui/`
- Shared utilities: `client/src/lib/`
- Keep page components focused on orchestration; move reusable logic into hooks or child components

#### Imports and Modules
- **Order**: React/core → third-party → `@/` aliases → relative imports
- Use `import type` for type-only imports
- Prefer `@/` aliases for cross-feature imports; relative imports are fine inside a single feature
- Use named exports for components, hooks, and helpers

#### Types, Forms, and Data Fetching
- Use Zod schemas with inferred types at form/API boundaries
- React Hook Form typically uses `zodResolver(schema)` and `mode: "onBlur"`
- Treat generated Kubb types (`client/src/kubb/`) as source of truth for contracts; never hand-edit `src/kubb/`
- Use TanStack Query for server state with stable query keys
- Use `enabled` for queries that depend on route params or deferred inputs
- Invalidate relevant queries after successful mutations

#### Error Handling
- Prefer Portuguese UI errors and shared helpers like `getFriendlyErrorMessage`
- Avoid unsafe casts (`as unknown as ...`) unless fully understood

### 3. Backend Guidelines (Java/Spring)

#### Architecture Layers
- **Controllers**: Handle HTTP concerns only (routing, status codes, validation, response bodies)
- **Services**: Own business rules and transaction boundaries
- **Repositories**: Handle persistence and query definitions
- **Mappers**: Convert DTOs to and from entities; do not leak entities into controllers
- Domain packages follow pattern: `dto/`, `repository/`, `web/`, `exception/`

#### Java Conventions
- Use Java 21 features (Records for DTOs with Bean Validation annotations)
- Prefer constructor injection (via `@RequiredArgsConstructor` or explicit)
- Use `@Transactional(readOnly = true)` for reads and `@Transactional` for writes
- Favor explicit helper methods such as `findXOrThrow` and `resolveXOrThrow`
- Classes use `PascalCase`; methods and fields use `camelCase`
- Exception classes should be domain-specific and end with `Exception`

#### Persistence and Contracts
- Entities follow shared UUID and audit timestamp conventions from base entities
- Keep repository queries explicit and intention-revealing
- API DTO field types must stay aligned with the OpenAPI contract consumed by Kubb
- Be explicit about timezone handling at API boundaries
- Throw domain-specific exceptions instead of returning `null` or swallowing invalid states

#### Error Handling
- Centralize HTTP error shaping in `GlobalExceptionHandler` with `ProblemDetail`
- Validation messages and business-rule errors MUST be in Portuguese

### 4. Backend Testing Guidelines
- **Frameworks**: JUnit 5, AssertJ, and Mockito
- **Structure**: Use `@Nested` classes when they improve organization
- **Fixtures**: Prefer deterministic fixtures with fixed UUIDs and timestamps
- **Assertions**: Assert both returned DTOs and important side effects (repository calls, conflict checks, archive behavior)

## Common Change Patterns

### New persistent field
1. Add Flyway migration
2. Update entity
3. Update request/response DTOs
4. Update mapper
5. Update service rules and repository queries if needed
6. Update tests
7. Regenerate OpenAPI and Kubb if field is exposed to SPA

### New endpoint or contract change
1. Update controller and service together
2. Add or update DTOs
3. Add or update tests
4. Regenerate OpenAPI
5. Coordinate with frontend consumers in the same task

## Generated Code and Contract Changes
- `client/src/kubb/` is generated; never hand-edit it
- If backend DTOs, endpoint signatures, or enums change, regenerate OpenAPI and Kubb in the same task
- If generated schemas reject backend dates or enums, fix the backend DTO or mapper first
- Do not patch generated frontend artifacts to compensate for backend contract bugs unless user explicitly asks

## Available Skills
The following specialized skills are available and should be loaded for relevant tasks:
- **java-coding-standards**: Java naming, immutability, Optional usage, streams, exceptions, and project layout
- **jpa-patterns**: Entity design, relationships, query optimization, transactions, auditing, and pagination
- **springboot-patterns**: REST API design, layered services, data access, caching, and async processing
- **vercel-react-best-practices**: React performance optimization (waterfalls, bundle size, re-renders)

## Quick Checklist Before Finishing a Task
- [ ] Did you keep messages, validation text, and API-facing user copy in Portuguese?
- [ ] Did you preserve the touched file's existing style instead of normalizing unrelated code?
- [ ] Did you avoid editing `client/src/kubb/` manually?
- [ ] Did you run the appropriate build, lint, or targeted test commands for the change?
- [ ] If the SPA depends on this contract, did you regenerate OpenAPI and Kubb?

## Cursor and Copilot Rules
*Checked for additional repository instructions in: `.cursorrules`, `.cursor/rules/`, and `.github/copilot-instructions.md`.*

No Cursor or Copilot rule files are present in this repository today. Agents should strictly follow this `AGENTS.md` file.
