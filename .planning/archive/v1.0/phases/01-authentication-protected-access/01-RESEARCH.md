# Phase 1: Authentication & Protected Access — Research

**Date:** 2026-04-17
**Status:** Complete

## Research Question

How should this brownfield React SPA + Spring Boot app add internal-staff authentication, persistent sessions, logout, and protected route access with minimal disruption to existing student/parent/employee/event/dashboard flows?

## Current State

- Backend has no Spring Security dependency or `SecurityFilterChain`.
- Frontend routes under `client/src/App.tsx` are all public today.
- Axios client does not send credentials and has no auth bootstrap/interceptors.
- Existing brownfield modules already work and should be protected, not rewritten.
- Existing employee records provide internal staff identity data (`tb_employees` has unique email + duty), but they do not contain login credentials.

## Recommendation

Use **Spring Security with server-managed HTTP session authentication** and a **dedicated internal user table linked to employees**.

### Why this is the best fit

1. **Matches the requirement scope**: internal staff only; no need for multi-client token ecosystem.
2. **Immediate logout is simple**: invalidating the server session satisfies AUTH-03 cleanly.
3. **Browser refresh persistence is built in**: session cookie + `/v1/auth/me` bootstrap satisfies AUTH-02.
4. **Lower risk than rolling JWT refresh flows**: fewer moving parts, fewer token revocation edge cases.
5. **Protects brownfield endpoints centrally**: secure existing `/v1/**` routes without touching every controller method.

## Recommended Architecture

### Backend

Add:

- `spring-boot-starter-security`
- Security config with `SecurityFilterChain`
- Stateful session auth
- `CookieCsrfTokenRepository.withHttpOnlyFalse()` for SPA-compatible CSRF protection
- `BCryptPasswordEncoder`
- `UserDetailsService` backed by a new auth table
- JSON auth endpoints:
  - `POST /v1/auth/login`
  - `POST /v1/auth/logout`
  - `GET /v1/auth/me`
- Route protection for all existing app APIs except auth/bootstrap/OpenAPI health docs

### Frontend

Add:

- Login page/feature
- Auth bootstrap query calling `GET /v1/auth/me`
- Protected route/layout wrapper around existing app routes
- Logout action in the main layout
- Axios credentials support (`withCredentials: true`)
- CSRF header/cookie wiring if Spring Security CSRF is enabled via cookie repository

## Data Model Recommendation

Create a dedicated table, e.g. `tb_internal_users`, instead of adding password fields directly to `tb_employees`.

### Suggested fields

- `id UUID PK`
- `employee_id UUID UNIQUE NULL/NOT NULL` (recommended: nullable only if bootstrap seed needs flexibility; otherwise required)
- `username VARCHAR(...) UNIQUE NOT NULL`
- `password_hash VARCHAR(...) NOT NULL`
- `active BOOLEAN NOT NULL DEFAULT true`
- `last_login_at TIMESTAMP WITH TIME ZONE NULL`
- audit columns consistent with `BaseEntity`

### Why separate from `tb_employees`

- Login credentials and operational employee data have different lifecycles.
- Future v2 portals can add other user/account types without reshaping the employee aggregate.
- Auth changes stay isolated from brownfield employee CRUD behavior.

## Seeding / Bootstrap Strategy

The phase needs at least one known internal user for local development and verification.

Recommended bootstrap:

- Add a Flyway migration for `tb_internal_users`
- Add a dev seed row tied to the existing ADM employee (`b71fa3e6-31f0-4ef5-a650-1bccae83302e`)
- Seed a deterministic username and BCrypt hash for a known dev password
- Keep the password only in dev docs/research/plan text, not in production config

This avoids blocking execution on an admin-creation UI that does not yet exist.

## API Shape Recommendation

### `POST /v1/auth/login`

Request:

```json
{
  "identifier": "beatriz.santos@731aprimorar.dev",
  "password": "admin123"
}
```

Behavior:

- Accept email **or** username in `identifier`
- Authenticate against `tb_internal_users`
- Create authenticated session
- Return current user payload, not raw token

Suggested response:

```json
{
  "id": "...",
  "username": "beatriz.santos",
  "displayName": "Beatriz Santos",
  "email": "beatriz.santos@731aprimorar.dev",
  "employeeId": "...",
  "duty": "ADM"
}
```

### `GET /v1/auth/me`

- Returns `200` + current user DTO when session is valid
- Returns `401` when not authenticated
- Used by the SPA on load/refresh

### `POST /v1/auth/logout`

- Invalidates session
- Clears security context
- Returns `204`

## Security Notes

### CSRF

Because this is a session/cookie-based browser app, **do not disable CSRF globally** for authenticated write routes.

Recommended pattern:

- Use Spring Security cookie CSRF repository
- Let frontend send the expected header on mutations
- Exempt only truly necessary endpoints if needed (for example login/logout can still be handled explicitly, but keep the surface small)

### CORS / credentials

Frontend dev runs on a different port, so authenticated XHR requires:

- backend CORS allowlist for the Vite origin
- `allowCredentials(true)`
- axios `withCredentials: true`

### Session policy

- Use session-based authentication, **not** `STATELESS`
- Invalidate session on logout
- Return `401` for anonymous API access to protected endpoints

### Password storage

- Store only BCrypt hashes
- Never store plaintext or reversible encryption

## Frontend Routing Recommendation

Use a route wrapper pattern:

- Public routes: `/login`
- Protected branch: everything inside `<MainLayout />`
- Auth bootstrap state decides between loading spinner, redirect to `/login`, or rendering app routes

This is cleaner than sprinkling auth checks inside each feature page.

## Existing Files / Patterns To Reuse

- `client/src/App.tsx` — current route tree to split into public + protected branches
- `client/src/components/layout/MainLayout.tsx` — best place for logout affordance/user identity display
- `client/src/lib/shared/api.ts` — central axios setup for credentials + response handling
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java` — extend for auth/security errors with Portuguese messages
- `server/api-aprimorar/src/main/resources/db/migration/V1__init.sql` — schema style reference
- `server/api-aprimorar/src/main/resources/data.sql` — dev seed location

## Architectural Responsibility Map

| Concern | Correct Tier |
|---|---|
| Password hashing, login verification, session invalidation | backend security/service layer |
| Route visibility and redirect to login | frontend router/auth shell |
| Current-user bootstrap on refresh | frontend auth query + backend `/v1/auth/me` |
| Persistent credential storage | browser cookie managed by backend session |
| Protection of `/v1/students`, `/v1/parents`, `/v1/employees`, `/v1/events`, `/v1/dashboard/**` | backend security filter chain |
| User-facing login/logout messages | frontend Portuguese UI |

## Don’t Hand-Roll

- Password hashing algorithm
- Session cookie internals
- Ad hoc route guards inside every page
- Manual edits inside `client/src/kubb/`

Use Spring Security, generated contract sync, and centralized router/auth wrappers.

## Common Pitfalls

1. **Protecting only the UI, not the API** — AUTH-04 requires backend enforcement too.
2. **Using JWT because it feels modern** — unnecessary here and complicates logout/revocation.
3. **Disabling CSRF for convenience** — wrong tradeoff for cookie auth.
4. **Forgetting axios credentials** — login appears to work, refresh does not.
5. **Leaving OpenAPI/codegen unsynced** — frontend auth hooks/types drift from backend.
6. **Adding auth fields directly to employee CRUD without separation** — couples account lifecycle to HR-style record edits.

## Verification Strategy

Minimum expected verification after implementation:

- Backend focused auth tests (`./mvnw -Dtest='*Auth*Test,*Security*Test' test` or exact created classes)
- Backend broader verification (`./mvnw test`)
- Contract regen if DTOs/endpoints are added:
  - `./mvnw -Pgenerate-openapi generate-resources`
  - `npm run sync`
- Frontend checks:
  - `npm run lint`
  - `npm run build`

## Validation Architecture

The phase should validate in layers:

1. **Auth service tests** — identifier lookup, password validation, inactive-user rejection.
2. **Security/HTTP tests** — anonymous access returns `401`, login succeeds, logout invalidates session, `/v1/auth/me` reflects session state.
3. **Frontend build/lint** — login route, protected route wrapper, and logout wiring compile cleanly.
4. **Manual browser check** — login, refresh persistence, logout, and direct protected-route denial.

## Final Research Decision

Plan Phase 1 around:

- **Spring Security session-based auth**
- **Dedicated internal-user table linked to employee records**
- **JSON auth endpoints (`login`, `logout`, `me`)**
- **Protected backend `/v1/**` routes plus frontend route guard shell**
- **OpenAPI → Kubb sync as part of the implementation path**
