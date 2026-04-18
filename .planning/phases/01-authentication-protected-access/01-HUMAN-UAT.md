---
status: diagnosed
phase: 01-authentication-protected-access
source: [01-VERIFICATION.md]
started: 2026-04-18T12:15:00Z
updated: 2026-04-18T17:24:00Z
---

## Current Test

[testing complete]

## Tests

### 1. Fresh-browser login handshake
expected: successful redirect to `/` with no CSRF/cookie issues
result: pass

### 2. Refresh persistence on protected page
expected: session remains active and protected UI stays accessible
result: issue
reported: "when refreshing the UI i'm being redirected to the login page."
severity: major

### 3. Logout teardown from protected navigation
expected: redirect to `/login` and no stale protected data shown
result: issue
reported: "when clicking the logout button i'm getting a \"Acesso Negado\" toast. the user is not being logged out"
severity: major

## Summary

total: 3
passed: 1
issues: 2
pending: 0
skipped: 0
blocked: 0

## Gaps

- truth: "session remains active and protected UI stays accessible"
  status: failed
  reason: "User reported: when refreshing the UI i'm being redirected to the login page."
  severity: major
  test: 2
  root_cause: "The SPA never imports `client/src/lib/shared/api.ts`, so the generated Kubb axios client never receives the required credential and XSRF defaults. Login can look successful from the response body, but refresh falls back to a real `/v1/auth/me` call on an unconfigured client and `AuthGate` redirects to `/login`."
  artifacts:
    - path: "client/src/lib/shared/api.ts"
      issue: "Contains the Kubb axios credential/XSRF bootstrap, but its side effects are never imported into the running app."
    - path: "client/src/main.tsx"
      issue: "App bootstrap does not import the shared API bootstrap module before generated hooks run."
    - path: "client/src/features/auth/hooks/useAuthSession.ts"
      issue: "Current login success path writes the returned user into cache, masking the missing persisted session until refresh."
  missing:
    - "Import the shared API bootstrap during app startup so Kubb axios gets `withCredentials` and XSRF defaults."
    - "Make authenticated route state depend on a real session bootstrap from `/v1/auth/me` instead of only the login response cache."
  debug_session: ".planning/debug/phase-1-uat-refresh-session.md"

- truth: "redirect to `/login` and no stale protected data shown"
  status: failed
  reason: "User reported: when clicking the logout button i'm getting a \"Acesso Negado\" toast. the user is not being logged out"
  severity: major
  test: 3
  root_cause: "`POST /v1/auth/logout` is still CSRF-protected, but the SPA client is not configured to send the XSRF header on cross-origin requests from `localhost:5173` to `localhost:8080`. Spring blocks logout with 403 `Acesso negado` before `AuthController.logout()` runs."
  artifacts:
    - path: "client/src/lib/shared/api.ts"
      issue: "Axios client sets cookie names and `withCredentials`, but not cross-origin XSRF header forwarding."
    - path: "server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java"
      issue: "Logout remains CSRF-protected and maps CSRF failures to `Acesso negado`."
    - path: "client/src/components/layout/MainLayout.tsx"
      issue: "Logout path surfaces the backend CSRF denial toast when the request is rejected."
  missing:
    - "Send the XSRF header for cross-origin cookie-based SPA requests, including logout."
    - "Re-test logout against the protected backend endpoint after the XSRF client config is corrected."
  debug_session: ".planning/debug/logout-access-negado-toast.md"
