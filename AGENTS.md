# AGENTS.md

Operational guide for coding agents in the Aprimorar repository.

## Project Architecture
- **Frontend**: React 19 + TypeScript + Vite SPA in `client/`
- **Backend**: Spring Boot 3.5 + Java 21 API in `server/api-aprimorar/`
- **Database**: PostgreSQL (Docker) with Flyway migrations in `server/api-aprimorar/src/main/resources/db/migration/`
- **API Contracts**: OpenAPI generates React hooks/types via Kubb into `client/src/kubb/`
- **Language**: All user-facing strings, validation messages, and API errors MUST be in Portuguese

## Build, Lint, and Test Commands

### Full-Stack & Contracts
```bash
# Database
docker compose up -d db  # Run from server/api-aprimorar/

# Sync after backend changes
cd server/api-aprimorar && ./mvnw -Pgenerate-openapi generate-resources
cd client && npm run sync
```

### Frontend (`client/`)
| Command | Description |
|---------|-------------|
| `npm install` | Install dependencies |
| `npm run dev` | Dev server |
| `npm run build` | Build & type check (tsc -b && vite build) |
| `npm run lint` | ESLint (typescript-eslint + react-hooks); warns on `@typescript-eslint/no-unused-vars` and `@typescript-eslint/no-explicit-any` |
| `npm run preview` | Preview production build |
| `npm run sync` | Regenerate Kubb hooks/schemas/types |

### Backend (`server/api-aprimorar/`)
| Command | Description |
|---------|-------------|
| `./mvnw spring-boot:run` | Run locally |
| `./mvnw package` | Build/package |
| `./mvnw test` | Run all tests |
| `./mvnw verify` | Tests + Jacoco coverage (target/site/jacoco/index.html) |

### Running a Single Test (Backend)
```bash
./mvnw -Dtest=ParentServiceTest test                    # Specific class
./mvnw -Dtest=ParentServiceTest#shouldReturnPagedParents test  # Specific method
./mvnw -Dtest='*ServiceTest' test                      # Wildcard match
```

## Code Style Guidelines

### General Workflow
- Keep diffs tightly scoped; avoid drive-by refactors
- Preserve local style of touched files; do not mass-reformat unrelated lines
- Prefer fixing root causes over unsafe casts or workarounds
- When persistence/API contracts change, update backend, artifacts, and frontend consumers together

### Frontend Guidelines (React/TypeScript)

**Project Structure**
- Route pages: `*Page.tsx`
- Feature UI: `client/src/features/<feature>/components/`
- Shared UI: `client/src/components/ui/`
- Utilities: `client/src/lib/`

**Naming Conventions**
- Components/Pages/Interfaces: `PascalCase` (e.g., `StudentForm.tsx`, `EventCard`)
- Hooks: `useXxx` prefix (e.g., `useStudents`, `useStudentMutations`)
- Variables/Functions/Props: `camelCase` (e.g., `currentPage`, `handleSubmit`)
- CSS Modules: `PascalCase` when pattern exists nearby

**Imports** (order: React/core → third-party → `@/` aliases → relative)
- Use `import type` for type-only imports
- Named exports for components/hooks/helpers; avoid `export default`
- Never hand-edit `client/src/kubb/` (generated)

**Types, Forms, Data Fetching**
- Zod schemas with inferred types at form/API boundaries
- React Hook Form: `zodResolver(schema)` + `mode: "onBlur"`
- TanStack Query with stable query keys; use `enabled` for deferred inputs
- Invalidate relevant queries after mutations

**Component Patterns**
- Explicit prop types near the top of the file
- Early returns for loading, empty, and error states
- Prefer descriptive names (e.g., `studentEvents`, `handleSearchChange` over abbreviations)
- Reuse shared UI primitives before creating new ones

**Error Handling**
- Portuguese UI errors; use shared `getFriendlyErrorMessage` helper
- Surface API failures through page states, not just `console.log`

### Backend Guidelines (Java/Spring)

**Architecture Layers**
- **Controllers**: HTTP concerns only (routing, status, validation, response)
- **Services**: Business rules and transaction boundaries
- **Repositories**: Persistence and queries
- **Mappers**: DTO ↔ Entity conversion; entities never leak to controllers

**Java Conventions**
- Java 21 Records for DTOs with Bean Validation annotations
- Constructor injection (`@RequiredArgsConstructor` or explicit)
- `@Transactional(readOnly = true)` for reads; `@Transactional` for writes
- Helper methods: `findXOrThrow`, `resolveXOrThrow` (private in services)
- Exception classes: domain-specific, ending with `Exception`

**Naming Conventions**
- Classes/Records: `PascalCase` (e.g., `StudentService`, `ParentDto`)
- Methods/Fields: `camelCase` (e.g., `findBySlug`, `marketRepository`)
- Constants: `UPPER_SNAKE_CASE`

**Persistence & Contracts**
- Entities: shared UUID IDs + audit timestamps from base entities
- Repository queries: explicit, intention-revealing
- DTO field types must align with OpenAPI contract
- Be explicit about timezone handling at API boundaries

**Error Handling**
- `GlobalExceptionHandler` with `ProblemDetail` for HTTP error shaping
- Throw domain-specific exceptions instead of returning null
- All validation/business-rule messages in Portuguese

**Logging**
- Use structured logging with parameters: `log.info("action entity={}", entityId)`
- Keep logs business-focused; avoid noisy debug logs in normal paths

### Testing (Backend)
- **Frameworks**: JUnit 5, AssertJ, Mockito
- Use `@Nested` classes for organization
- Deterministic fixtures with fixed UUIDs/timestamps
- Assert both returned DTOs and side effects (repo calls, conflict checks)

## Common Change Patterns

### New persistent field
1. Add Flyway migration
2. Update entity
3. Update request/response DTOs
4. Update mapper
5. Update service rules/repository queries
6. Update tests
7. Regenerate OpenAPI + Kubb if exposed to SPA

### New endpoint/contract change
1. Update controller + service together
2. Add/update DTOs
3. Add/update tests
4. Regenerate OpenAPI
5. Coordinate with frontend consumers

## Available Skills
- **java-coding-standards**: Java naming, immutability, Optional, streams
- **jpa-patterns**: Entity design, relationships, transactions, pagination
- **springboot-patterns**: REST API, layered services, caching, async
- **vercel-react-best-practices**: React performance optimization

## Quick Checklist
- [ ] User-facing strings and validation in Portuguese?
- [ ] `client/src/kubb/` not edited manually?
- [ ] Business logic in service, not controller?
- [ ] Tests updated for changed contracts?
- [ ] OpenAPI + Kubb regenerated if SPA depends on changes?
- [ ] Ran `npm run lint` and `npm run build` (frontend) or targeted tests (backend)?
