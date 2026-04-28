---
phase: 01-authentication-protected-access
plan: 01
subsystem: auth
tags: [spring-security, session-auth, csrf, postgres, testing]
requires: []
provides:
  - internal user persistence linked to employees
  - session-based login, current-user, and logout endpoints
  - centralized protection for protected /v1 backend routes
affects: [frontend-auth, openapi, protected-routing]
tech-stack:
  added: [spring-boot-starter-security, spring-security-test]
  patterns: [stateful session auth, cookie csrf tokens, employee-linked internal users]
key-files:
  created:
    - server/api-aprimorar/src/main/resources/db/migration/V2__create_internal_users.sql
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/InternalUser.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthService.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java
  modified:
    - server/api-aprimorar/pom.xml
    - server/api-aprimorar/src/main/resources/data.sql
    - server/api-aprimorar/src/main/java/com/aprimorar/api/config/WebCorsConfig.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java
    - server/api-aprimorar/src/test/java/com/aprimorar/api/domain/auth/AuthServiceTest.java
    - server/api-aprimorar/src/test/java/com/aprimorar/api/domain/auth/AuthControllerSecurityTest.java
key-decisions:
  - "Use Spring Security server-managed sessions instead of JWT to keep logout immediate and browser refresh persistence simple."
  - "Store staff credentials in tb_internal_users linked to employees so authentication lifecycle stays isolated from employee CRUD."
patterns-established:
  - "Protected /v1 APIs use a single SecurityFilterChain with explicit auth/docs allowlist."
  - "Browser session auth keeps CSRF enabled through CookieCsrfTokenRepository.withHttpOnlyFalse()."
requirements-completed: [AUTH-01, AUTH-02, AUTH-03, AUTH-04]
duration: 10min
completed: 2026-04-18
---

# Phase 1 Plan 1: Authentication backend foundation Summary

**Spring Security session auth with employee-linked internal users, JSON auth endpoints, and protected `/v1/**` routes.**

## Performance

- **Duration:** 10 min
- **Started:** 2026-04-18T08:42:05-03:00
- **Completed:** 2026-04-18T11:52:25Z
- **Tasks:** 3
- **Files modified:** 16

## Accomplishments
- Added `tb_internal_users` persistence, seed credentials, and backend auth DTO contracts.
- Implemented login, current-user, and logout endpoints backed by Spring Security sessions.
- Enforced protected `/v1/**` access with CSRF, credentialed CORS, and Portuguese JSON auth errors.

## Task Commits

Each task was committed atomically:

1. **Task 1: Create auth test scaffolds first** - `9ab35699` (test)
2. **Task 2: Add internal-user persistence and auth contracts** - `67dd105d` (test), `670210ce` (feat)
3. **Task 3: Implement secured session endpoints and route protection** - `610bd21a` (test), `641764bb` (feat)

## Files Created/Modified
- `server/api-aprimorar/pom.xml` - adds Spring Security runtime and test dependencies.
- `server/api-aprimorar/src/main/resources/db/migration/V2__create_internal_users.sql` - creates credential storage linked to employees.
- `server/api-aprimorar/src/main/resources/data.sql` - seeds the deterministic `beatriz.santos` dev account.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/InternalUser.java` - models internal staff credentials and login timestamp.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/repository/InternalUserRepository.java` - resolves accounts by username or employee email.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthService.java` - validates credentials, records login time, and maps current-user DTOs.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/InternalUserDetailsService.java` - bridges internal users into Spring Security authentication.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java` - exposes `/v1/auth/login`, `/me`, and `/logout`.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java` - centralizes session auth, CSRF, and protected-route rules.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/config/WebCorsConfig.java` - enables credentialed frontend requests from Vite.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java` - returns stable Portuguese JSON for auth failures.
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/auth/AuthServiceTest.java` - covers identifier login, inactive rejection, and DTO mapping.
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/auth/AuthControllerSecurityTest.java` - covers session login/me/logout and anonymous rejection.

## Decisions Made
- Used Spring Security session authentication with server-side invalidation to satisfy immediate logout and refresh persistence requirements.
- Kept `/v1/auth/me` publicly reachable at the filter level and converted missing-auth cases into explicit `401` JSON responses inside the auth flow.
- Added a dedicated `UNAUTHORIZED` error code so security failures do not reuse unrelated validation/conflict codes.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 2 - Missing Critical] Added explicit unauthorized error code and JSON security handlers**
- **Found during:** Task 3 (Implement secured session endpoints and route protection)
- **Issue:** Default Spring Security failures would have mixed HTML/default responses with existing API problem payloads.
- **Fix:** Added `ErrorCode.UNAUTHORIZED`, centralized `401` handling in `GlobalExceptionHandler`, and JSON `AuthenticationEntryPoint` / `AccessDeniedHandler` wiring in `SecurityConfig`.
- **Files modified:** `server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/ErrorCode.java`, `server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java`
- **Verification:** `cd server/api-aprimorar && ./mvnw -Dtest=AuthServiceTest,AuthControllerSecurityTest test`
- **Committed in:** `641764bb`

**2. [Rule 2 - Missing Critical] Rotated the session id after login**
- **Found during:** Task 3 (Implement secured session endpoints and route protection)
- **Issue:** Manual session creation inside the login controller needed fixation hardening.
- **Fix:** Called `changeSessionId()` before persisting the authenticated security context into the HTTP session.
- **Files modified:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java`
- **Verification:** `cd server/api-aprimorar && ./mvnw -Dtest=AuthServiceTest,AuthControllerSecurityTest test`
- **Committed in:** `641764bb`

---

**Total deviations:** 2 auto-fixed (2 missing critical)
**Impact on plan:** Both changes tightened auth correctness and response stability without expanding scope beyond the planned backend authentication foundation.

## Issues Encountered
- Docker-based live verification could not be completed because the local Docker daemon was unavailable (`Cannot connect to the Docker daemon`). Focused and full Maven test suites still passed.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness
- Backend auth endpoints and route protection are ready for OpenAPI regeneration and frontend login/bootstrap wiring in Plan 01-02.
- Frontend work should consume `/v1/auth/login`, `/v1/auth/me`, and `/v1/auth/logout` with credentials enabled.

## Self-Check: PASSED
