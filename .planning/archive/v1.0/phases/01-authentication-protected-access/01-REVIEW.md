---
phase: 01-authentication-protected-access
reviewed: 2026-04-18T12:28:21Z
depth: standard
files_reviewed: 25
files_reviewed_list:
  - .planning/phases/01-authentication-protected-access/01-01-PLAN.md
  - .planning/phases/01-authentication-protected-access/01-authentication-protected-access-01-SUMMARY.md
  - .planning/phases/01-authentication-protected-access/01-authentication-protected-access-02-SUMMARY.md
  - .planning/phases/01-authentication-protected-access/01-authentication-protected-access-03-SUMMARY.md
  - server/api-aprimorar/pom.xml
  - server/api-aprimorar/src/main/resources/db/migration/V2__create_internal_users.sql
  - server/api-aprimorar/src/main/resources/data.sql
  - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/InternalUser.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/repository/InternalUserRepository.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/dto/AuthLoginRequestDTO.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/dto/AuthCurrentUserResponseDTO.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthService.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/InternalUserDetailsService.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/config/WebCorsConfig.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/exception/GlobalExceptionHandler.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/exception/ErrorCode.java
  - client/src/lib/shared/api.ts
  - client/eslint.config.js
  - client/src/features/auth/forms/loginSchema.ts
  - client/src/features/auth/hooks/useAuthSession.ts
  - client/src/features/auth/components/AuthGate.tsx
  - client/src/features/auth/pages/LoginPage.tsx
  - client/src/App.tsx
  - client/src/components/layout/MainLayout.tsx
  - client/src/lib/shared/queryClient.ts
findings:
  critical: 0
  warning: 2
  info: 1
  total: 3
status: issues_found
---

# Phase 1: Code Review Report

**Reviewed:** 2026-04-18T12:28:21Z
**Depth:** standard
**Files Reviewed:** 25
**Status:** issues_found

## Summary

Reviewed the Phase 1 authentication/protected-access backend and SPA wiring, using the phase plan/summaries for intent. The main risks are in session lifecycle handling: the backend CSRF setup is incomplete for an SPA login flow, and the frontend can keep treating stale cached auth data as a valid session after the server has already expired it.

## Warnings

### WR-01: SPA login flow does not guarantee an initial CSRF token cookie

**File:** `server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java:45`
**Issue:** The app enables `CookieCsrfTokenRepository.withHttpOnlyFalse()`, and the frontend is configured to send `XSRF-TOKEN` / `X-XSRF-TOKEN`, but no code forces Spring Security to render a CSRF token cookie for the SPA before the first `POST /v1/auth/login`. In this setup, a fresh browser session can hit `/login` without ever receiving `XSRF-TOKEN`, causing the first login attempt to fail with `403` instead of authenticating.
**Fix:** Force token issuance for SPA requests (for example with a `CsrfTokenRequestHandler`/filter that loads the token on safe requests, or a dedicated `/csrf` bootstrap endpoint), or explicitly exempt `/v1/auth/login` from CSRF if that is the intended flow.

```java
http.csrf(csrf -> csrf
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
);
```

### WR-02: Expired sessions can still look authenticated because auth state is derived from cached data only

**File:** `client/src/features/auth/hooks/useAuthSession.ts:15-27,54-58`
**Issue:** `isAuthenticated` is computed as `Boolean(currentUserQuery.data)`. React Query keeps the last successful `data` during failed refetches, so if `/v1/auth/me` later returns `401` (session expired, revoked, or server restarted), the hook can still expose the stale user object as authenticated. Combined with protected-query caching, this can leave private screens visible until a hard refresh or manual logout.
**Fix:** Clear the current-user query and protected caches whenever `/v1/auth/me` returns `401`, and derive authenticated state from a successful query state instead of cached data alone.

```ts
const currentUserQuery = useMe({
  query: {
    queryKey: currentUserQueryKey,
    retry: (count, error) => !isUnauthorizedError(error) && count < 1,
  },
})

useEffect(() => {
  if (isUnauthorizedError(currentUserQuery.error)) {
    queryClient.removeQueries({ queryKey: currentUserQueryKey })
    void clearProtectedQueryCache(queryClient)
  }
}, [currentUserQuery.error, queryClient])

const isAuthenticated = currentUserQuery.status === "success" && Boolean(currentUserQuery.data)
```

## Info

### IN-01: Authentication logic is duplicated across two backend paths

**File:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthService.java:29-53`
**Issue:** `AuthService.login(...)` reimplements password/active-user checks, but production login goes through `AuthenticationManager` in `AuthController` and then calls `registerAuthenticatedSession(...)`. That split makes it easier for controller behavior and tested service behavior to drift apart over time.
**Fix:** Keep a single authoritative login path: either delegate the controller to one service method that performs the full flow, or remove the unused duplicate method and test the path that production actually executes.

---

_Reviewed: 2026-04-18T12:28:21Z_
_Reviewer: the agent (gsd-code-reviewer)_
_Depth: standard_
