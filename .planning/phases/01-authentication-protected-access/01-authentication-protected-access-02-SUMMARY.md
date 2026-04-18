---
phase: 01-authentication-protected-access
plan: 02
subsystem: auth
tags: [react, kubb, axios, react-query, zod, session-auth]
requires:
  - phase: 01-authentication-protected-access
    provides: session auth endpoints, current-user bootstrap contract, protected backend routes
provides:
  - generated frontend auth hooks and DTOs under the auth Kubb group
  - credential-aware SPA auth client and current-user session wrapper
  - Portuguese login page and reusable auth gate for protected routing work
affects: [protected-routing, login-ux, frontend-auth]
tech-stack:
  added: []
  patterns: [generated auth hooks consumed via handwritten wrapper hook, cookie session auth with XSRF header support]
key-files:
  created:
    - client/src/features/auth/forms/loginSchema.ts
    - client/src/features/auth/hooks/useAuthSession.ts
    - client/src/features/auth/components/AuthGate.tsx
    - client/src/features/auth/pages/LoginPage.tsx
    - client/src/kubb/hooks/auth/useLogin.ts
    - client/src/kubb/hooks/auth/useLogout.ts
    - client/src/kubb/hooks/auth/useMe.ts
  modified:
    - client/src/lib/shared/api.ts
    - client/eslint.config.js
    - client/src/kubb/index.ts
    - server/api-aprimorar/pom.xml
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java
key-decisions:
  - "Use a handwritten useAuthSession wrapper around generated Kubb hooks so login/logout invalidation and bootstrap behavior stay centralized."
  - "Configure the shared Kubb axios client for withCredentials and Spring Security XSRF names instead of storing session state in browser storage."
patterns-established:
  - "Auth pages validate identifier/password with Zod and react-hook-form using Portuguese messages."
  - "Protected-route work can rely on AuthGate plus useAuthSession rather than duplicating current-user query handling."
requirements-completed: [AUTH-01, AUTH-02]
duration: 15min
completed: 2026-04-18
---

# Phase 1 Plan 2: Authentication frontend bootstrap Summary

**Generated auth contracts plus a cookie-based SPA session wrapper, Portuguese login form, and reusable auth gate for protected routing.**

## Performance

- **Duration:** 15 min
- **Started:** 2026-04-18T12:02:00Z
- **Completed:** 2026-04-18T12:17:28Z
- **Tasks:** 2
- **Files modified:** 26

## Accomplishments
- Regenerated auth hooks and DTOs from the backend session API under `client/src/kubb/hooks/auth/` and `client/src/kubb/types/`.
- Updated the SPA client to send credentialed requests with Spring Security-compatible XSRF cookie/header names.
- Added the login schema, `useAuthSession`, `AuthGate`, and a Portuguese `LoginPage` ready for route wiring in the next plan.

## Task Commits

Each task was committed atomically:

1. **Task 1: Regenerate OpenAPI and Kubb auth contracts** - `98805872` (feat)
2. **Task 2: Build the SPA auth client, bootstrap hook, and login page** - `80dd58e3` (feat)

## Files Created/Modified
- `client/src/lib/shared/api.ts` - enables credentialed axios requests and configures the generated Kubb axios client for cookie/XSRF auth.
- `client/src/features/auth/forms/loginSchema.ts` - validates identifier and password with Portuguese messages.
- `client/src/features/auth/hooks/useAuthSession.ts` - centralizes current-user bootstrap plus login/logout invalidation behavior.
- `client/src/features/auth/components/AuthGate.tsx` - renders a loading state while session bootstrap is pending and gates authenticated content.
- `client/src/features/auth/pages/LoginPage.tsx` - provides the Portuguese login form and redirects to the protected app after success.
- `client/src/kubb/hooks/auth/useLogin.ts` - generated login mutation hook.
- `client/src/kubb/hooks/auth/useLogout.ts` - generated logout mutation hook.
- `client/src/kubb/hooks/auth/useMe.ts` - generated current-user bootstrap query hook.
- `client/src/kubb/types/AuthLoginRequestDTO.ts` - generated typed login payload.
- `client/src/kubb/types/AuthCurrentUserResponseDTO.ts` - generated typed current-user payload.
- `client/eslint.config.js` - ignores generated Kubb output during linting.
- `server/api-aprimorar/pom.xml` - fixes Springdoc Maven plugin resolution so OpenAPI generation succeeds.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java` - sets the OpenAPI auth tag so Kubb groups auth hooks under `auth/`.

## Decisions Made
- Wrapped generated auth hooks in `useAuthSession` instead of calling them directly from pages so session bootstrap and cache invalidation stay consistent.
- Configured the generated Kubb axios client globally for `withCredentials`, `XSRF-TOKEN`, and `X-XSRF-TOKEN` to match the backend session/CSRF model.
- Ignored `client/src/kubb/**` in ESLint because Kubb output is generated and should not block frontend lint on handwritten code.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 3 - Blocking] Corrected Springdoc plugin/version and auth tag for contract generation**
- **Found during:** Task 1 (Regenerate OpenAPI and Kubb auth contracts)
- **Issue:** `springdoc-openapi-maven-plugin:2.8.9` could not be resolved from Maven Central, and the generated auth files landed under `auth-controller/` instead of the planned `auth/` group.
- **Fix:** Pinned the Maven plugin to `1.5` and annotated `AuthController` with `@Tag(name = "Auth")` before regenerating OpenAPI and Kubb output.
- **Files modified:** `server/api-aprimorar/pom.xml`, `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java`, generated Kubb auth files
- **Verification:** `./mvnw -Pgenerate-openapi generate-resources` and `npm run sync`
- **Committed in:** `98805872`

**2. [Rule 3 - Blocking] Excluded generated Kubb output from ESLint**
- **Found during:** Task 2 (Build the SPA auth client, bootstrap hook, and login page)
- **Issue:** full frontend lint was flagging generated Kubb files, including `useMe.ts`, which should remain generated and unedited.
- **Fix:** Updated `client/eslint.config.js` to ignore `src/kubb/**` so lint focuses on handwritten SPA code.
- **Files modified:** `client/eslint.config.js`
- **Verification:** `npx eslint eslint.config.js src/lib/shared/api.ts src/features/auth/forms/loginSchema.ts src/features/auth/hooks/useAuthSession.ts src/features/auth/components/AuthGate.tsx src/features/auth/pages/LoginPage.tsx`
- **Committed in:** `80dd58e3`

---

**Total deviations:** 2 auto-fixed (2 blocking)
**Impact on plan:** Both fixes were required to generate the planned auth contract layout and keep verification focused on handwritten frontend auth work without expanding scope.

## Issues Encountered
- Initial execution paused at a human-action checkpoint because Docker/PostgreSQL/backend startup was unavailable locally; after the environment was started, execution resumed normally.
- Full `npm run lint` still fails because of a pre-existing syntax error in `client/src/components/ui/text-input.tsx`.
- Full `npm run build` still fails because of that same pre-existing syntax error plus a TypeScript parse issue in `react-router` typings in this environment.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness
- Plan 01-03 can wire `useAuthSession` and `AuthGate` into app routing and logout UX.
- The login screen and generated auth hooks are ready; remaining auth work is route integration rather than contract/bootstrap setup.

## Deferred Issues
- See `.planning/phases/01-authentication-protected-access/deferred-items.md` for the unrelated frontend lint/build blockers discovered during verification.

## Self-Check: PASSED
