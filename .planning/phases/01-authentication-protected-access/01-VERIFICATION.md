---
phase: 01-authentication-protected-access
verified: 2026-04-18T18:18:59Z
status: human_needed
score: 7/7 must-haves verified
overrides_applied: 0
re_verification:
  previous_status: human_needed
  previous_score: 5/5
  gaps_closed:
    - "SPA startup now imports the shared auth client bootstrap before auth hooks run."
    - "Auth state now depends on a real /v1/auth/me refetch after login instead of optimistic cache-only data."
    - "Shared axios/Kubb config now enables cross-origin XSRF forwarding for logout and other cookie-authenticated mutations."
  gaps_remaining: []
  regressions: []
human_verification:
  - test: "Login in a clean browser session with the seeded internal user"
    expected: "Login succeeds, redirects to /, and no CSRF/cookie error appears."
    why_human: "Needs a real browser to validate cookie + XSRF behavior across origins."
  - test: "Refresh a protected route such as /students after login"
    expected: "The session stays active and the app remains on the protected screen instead of redirecting to /login."
    why_human: "Browser refresh persistence depends on real cookie resend behavior, not just static code review or MockMvc."
  - test: "Click Sair from a protected screen, then revisit /students or use the browser back button"
    expected: "Logout completes without the Acesso negado toast and protected pages redirect back to /login with no stale private data shown."
    why_human: "Requires end-to-end browser validation of CSRF, navigation history, and visible cache teardown."
---

# Phase 1: Authentication & Protected Access Verification Report

**Phase Goal:** Internal staff can securely access the application, while anonymous users are kept out of protected workflows.
**Verified:** 2026-04-18T18:18:59Z
**Status:** human_needed
**Re-verification:** Yes — after UAT gap-closure plan 01-04

## Goal Achievement

### Observable Truths

| # | Truth | Status | Evidence |
| --- | --- | --- | --- |
| 1 | Internal user can sign in with email or username plus password and reach the protected app. | ✓ VERIFIED | `StaffAccountRepository.findByUsernameOrEmployeeEmail(...)` accepts username or employee email; `LoginPage.tsx` submits `identifier` + `password` through generated `useLogin`; `AuthController.login` authenticates and stores the `SPRING_SECURITY_CONTEXT`; `AuthServiceTest` and `AuthControllerSecurityTest` pass for username/email login. |
| 2 | Authenticated user stays signed in after browser refresh until logout or session expiry. | ✓ VERIFIED | `main.tsx` now imports `@/lib/shared/api` before render, `api.ts` configures `withCredentials`, `withXSRFToken`, and Spring CSRF names for both shared axios and Kubb axios, and `useAuthSession.ts` derives auth state from `useMe`/`/v1/auth/me` instead of optimistic cache data. |
| 3 | Authenticated user can log out from the app and protected pages stop being accessible immediately. | ✓ VERIFIED | `MainLayout.tsx` exposes a visible `Sair` button that calls `logout.mutateAsync()`, then `clearProtectedQueryCache(queryClient)`, then redirects to `/login`; `AuthController.logout` uses `SecurityContextLogoutHandler`; `AuthControllerSecurityTest` verifies `/v1/auth/me` becomes `401` after logout. |
| 4 | Unauthenticated visitor cannot open dashboard, student, parent, employee, event, or finance workflows directly. | ✓ VERIFIED | `SecurityConfig.java` requires authentication for `/v1/**`; `AuthControllerSecurityTest` confirms anonymous `GET /v1/students` returns `401`; `App.tsx` wraps all business routes in `AuthGate`; unauthenticated unknown SPA paths (including `/finance`) fall through to the catch-all redirect to `/login`. |
| 5 | The frontend uses generated auth contracts instead of handwritten request shapes. | ✓ VERIFIED | `useAuthSession.ts` imports generated `useLogin`, `useLogout`, `useMe`, and `meQueryKey` from `@/kubb`; generated auth hooks exist under `client/src/kubb/hooks/auth/`; generated DTO types exist in `client/src/kubb/types/AuthLoginRequestDTO.ts` and `AuthCurrentUserResponseDTO.ts`. |
| 6 | The SPA boots the shared auth client before any auth hook runs, so cross-origin cookie/XSRF settings actually apply at runtime. | ✓ VERIFIED | `client/src/main.tsx` imports `@/lib/shared/api`; `client/src/lib/shared/api.ts` executes `Object.assign(axiosInstance.defaults, sharedApiConfig)` with `withCredentials: true` and `withXSRFToken: true`. |
| 7 | Login success is confirmed by a real `/v1/auth/me` bootstrap, not only by cached mutation data. | ✓ VERIFIED | `useAuthSession.ts` calls `refetchCurrentUser()` on login success and does not write login response data directly into the current-user cache; protected routing uses `currentUserQuery.data` / `isSuccess`. |

**Score:** 7/7 truths verified

### Required Artifacts

| Artifact | Expected | Status | Details |
| --- | --- | --- | --- |
| `server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java` | Session security, CSRF, route protection | ✓ VERIFIED | Substantive 171-line config with `SecurityFilterChain`, `CookieCsrfTokenRepository.withHttpOnlyFalse()`, SPA CSRF handler, credentialed exception handlers, and `/v1/**` auth rule. |
| `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java` | Login/logout/me endpoints | ✓ VERIFIED | Exposes `POST /v1/auth/login`, `GET /v1/auth/me`, and `POST /v1/auth/logout`; rotates existing session id and persists the Spring security context in session. |
| `server/api-aprimorar/src/main/resources/db/migration/V2__create_internal_users.sql` | Internal staff credential storage | ✓ VERIFIED | Creates `tb_internal_users` linked to employees, with unique username and employee constraints. |
| `client/src/features/auth/pages/LoginPage.tsx` | Portuguese login screen | ✓ VERIFIED | Renders `Entrar`, identifier/password inputs, Portuguese guidance/errors, and redirects authenticated users away from `/login`. |
| `client/src/features/auth/hooks/useAuthSession.ts` | Current-user bootstrap and auth mutations | ✓ VERIFIED | Wraps generated `useLogin`, `useLogout`, and `useMe`; plan pattern expected `useGet`, but actual generated query hook is `useMe` and is wired correctly. |
| `client/src/lib/shared/api.ts` | Credentialed axios + Kubb bootstrap | ✓ VERIFIED | Sets `withCredentials`, `withXSRFToken`, `xsrfCookieName`, and `xsrfHeaderName`; applies the same defaults to the generated Kubb axios instance. |
| `client/src/main.tsx` | Startup-time auth client bootstrap | ✓ VERIFIED | Imports `@/lib/shared/api` before rendering `<App />`, ensuring auth client side effects always run. |
| `client/src/App.tsx` | Public login route plus protected route branch | ✓ VERIFIED | Defines `/login`, wraps the app shell in `AuthGate`, and redirects unmatched paths based on auth state. |
| `client/src/components/layout/MainLayout.tsx` | In-app logout affordance and user context | ✓ VERIFIED | Shows current user name/duty/email and a visible `Sair` button wired to logout and redirect. |
| `client/src/lib/shared/queryClient.ts` | Protected query cleanup support | ✓ VERIFIED | Cancels/removes cached protected queries for dashboard, students, parents, employees, events, and `/v1/auth/me`. |
| `client/src/kubb/hooks/auth/useLogin.ts` + `useLogout.ts` + `useMe.ts` | Generated auth contract hooks | ✓ VERIFIED | Generated hooks exist and target `/v1/auth/login`, `/v1/auth/logout`, and `/v1/auth/me`. |

### Key Link Verification

| From | To | Via | Status | Details |
| --- | --- | --- | --- | --- |
| `AuthController.java` | `AuthService.java` | login/logout/me methods | ✓ VERIFIED | `AuthController` calls `authService.registerAuthenticatedSession(...)` and `authService.getCurrentUser(...)`. |
| `SecurityConfig.java` | `StudentController.java` | authenticated `/v1/**` matcher | ✓ VERIFIED | Manual verification: `SecurityConfig` protects `/v1/**`, and `StudentController` is mounted at `/v1/students`; gsd-tools false-negative came from looking for a direct class reference. |
| `useAuthSession.ts` | `client/src/kubb/hooks/auth/useMe.ts` | current-user bootstrap query | ✓ VERIFIED | Imports and calls generated `useMe` with stable `meQueryKey()`. |
| `LoginPage.tsx` | `client/src/kubb/hooks/auth/useLogin.ts` | submit handler | ✓ VERIFIED | `handleSubmit` calls `login.mutateAsync({ data })`; `login` comes from the generated-hook wrapper. |
| `App.tsx` | `AuthGate.tsx` | protected route element | ✓ VERIFIED | Protected branch uses `<AuthGate ...><MainLayout /></AuthGate>`. |
| `MainLayout.tsx` | `useAuthSession.ts` | logout action | ✓ VERIFIED | `handleLogout` calls `logout.mutateAsync()` from `useAuthSession`. |
| `MainLayout.tsx` | `queryClient.ts` | protected cache cleanup on logout | ✓ VERIFIED | `handleLogout` calls `clearProtectedQueryCache(queryClient)` before navigation. |
| `main.tsx` | `api.ts` | side-effect import before React app render | ✓ VERIFIED | `main.tsx` imports `@/lib/shared/api`; gsd-tools false-negative was caused by quote-pattern matching, not missing wiring. |

### Data-Flow Trace (Level 4)

| Artifact | Data Variable | Source | Produces Real Data | Status |
| --- | --- | --- | --- | --- |
| `client/src/features/auth/hooks/useAuthSession.ts` | `currentUserQuery.data` | Generated `useMe` → `GET /v1/auth/me` → `AuthController.me` → `AuthService.getCurrentUser(...)` → `StaffAccountRepository.findByUsernameOrEmployeeEmail(...)` | Yes — repository query joins real employee data | ✓ FLOWING |
| `client/src/features/auth/hooks/useAuthSession.ts` | `refetchCurrentUser()` | TanStack Query invalidation/refetch of `meQueryKey()` after login | Yes — post-login auth state depends on fresh `/v1/auth/me` output | ✓ FLOWING |
| `client/src/features/auth/pages/LoginPage.tsx` | form `identifier/password` | `react-hook-form` + `loginSchema` → generated `useLogin` → `POST /v1/auth/login` → `AuthenticationManager`/`StaffAccountDetailsService` | Yes — backend establishes a real authenticated session | ✓ FLOWING |
| `client/src/components/layout/MainLayout.tsx` | `currentUser` | `useAuthSession().currentUser` → `/v1/auth/me` response | Yes — renders real `displayName`, `duty`, and `email` | ✓ FLOWING |
| `client/src/App.tsx` | `isAuthenticated` | `useAuthSession()` derived from `currentUserQuery.isSuccess && Boolean(currentUserQuery.data)` | Yes — protected routing depends on real current-user query state | ✓ FLOWING |

### Behavioral Spot-Checks

| Behavior | Command | Result | Status |
| --- | --- | --- | --- |
| Backend auth/session behavior | `./mvnw -Dtest=AuthServiceTest,AuthControllerSecurityTest test` | `BUILD SUCCESS`; 11 tests passed | ✓ PASS |
| Handwritten auth frontend files lint cleanly | `npx eslint "src/lib/shared/api.ts" "src/main.tsx" "src/features/auth/hooks/useAuthSession.ts" "src/features/auth/components/AuthGate.tsx" "src/features/auth/pages/LoginPage.tsx" "src/App.tsx" "src/components/layout/MainLayout.tsx" "src/lib/shared/queryClient.ts"` | Exit 0, no lint output | ✓ PASS |

### Requirements Coverage

| Requirement | Source Plan | Description | Status | Evidence |
| --- | --- | --- | --- | --- |
| AUTH-01 | 01-01, 01-02 | Internal user can sign in with email or username and password | ✓ SATISFIED | Repository lookup accepts username/email, login DTO/schema capture `identifier` + `password`, login page submits to generated `useLogin`, and backend auth tests pass for both identifiers. |
| AUTH-02 | 01-01, 01-02, 01-04 | Authenticated user session persists across browser refresh until logout or session expiry | ✓ SATISFIED | Server session is stored in `SPRING_SECURITY_CONTEXT`; `main.tsx` now boots shared auth client config before app render; `useAuthSession.ts` refetches `/v1/auth/me` and uses that as the auth source of truth. |
| AUTH-03 | 01-01, 01-03, 01-04 | Authenticated user can log out securely from the app | ✓ SATISFIED | `Sair` calls generated logout, shared auth client forwards XSRF token, server invalidates session, and protected query cache is cleared before redirect. |
| AUTH-04 | 01-01, 01-03 | Unauthenticated users cannot access protected student, parent, employee, event, finance, or dashboard workflows | ✓ SATISFIED | Backend protects `/v1/**`, SPA business routes sit behind `AuthGate`, anonymous `/v1/students` returns `401`, and unauthenticated unknown routes fall back to `/login`. |

No orphaned Phase 1 requirement IDs were found in `REQUIREMENTS.md`; all four phase requirements are declared by plan frontmatter and accounted for above.

### Anti-Patterns Found

| File | Line | Pattern | Severity | Impact |
| --- | --- | --- | --- | --- |
| `client/src/lib/shared/api.ts` | 30 | `//TODO implementar o logging mais pra frente` | ℹ️ Info | Logging cleanup remains, but auth/bootstrap behavior is implemented and wired. |
| `client/src/lib/shared/api-errors.ts` | 6 | `//TODO implementar o logging mais pra frente` | ℹ️ Info | Error logging is still ad hoc, but user-facing auth errors still resolve to Portuguese messages. |

### Human Verification Required

### 1. Fresh-browser login handshake

**Test:** Open `/login` in a clean browser session and sign in with the seeded user (`beatriz.santos` / `admin123`).
**Expected:** Login succeeds, redirects to `/`, and no CSRF/cookie error appears.
**Why human:** This requires a real browser to validate cross-origin cookie and XSRF behavior.

### 2. Refresh persistence on a protected route

**Test:** After logging in, open `/students` and refresh the page.
**Expected:** The session remains active and the app stays on the protected route instead of redirecting to `/login`.
**Why human:** Real refresh persistence depends on browser cookie resend behavior that static review and MockMvc do not fully reproduce.

### 3. Logout teardown from protected navigation

**Test:** From `/students` or another protected page, click `Sair`, then revisit `/students` directly or use the browser back button.
**Expected:** Logout completes without the `Acesso negado` toast and protected screens redirect to `/login` without stale private data remaining visible.
**Why human:** Needs end-to-end validation of CSRF handling, navigation history, and visible cache teardown.

### Gaps Summary

No code-level implementation gaps remain against Phase 1 roadmap success criteria or plan must-haves. Plan 01-04 closes the previously diagnosed refresh/logout issues in code by importing the shared API bootstrap at startup, enabling cross-origin XSRF forwarding, and making auth state depend on a real `/v1/auth/me` refetch. Phase status remains `human_needed` because those browser-level behaviors still need live re-testing.

---

_Verified: 2026-04-18T18:18:59Z_
_Verifier: the agent (gsd-verifier)_
