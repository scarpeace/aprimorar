---
status: diagnosed
trigger: "Diagnose the Phase 1 UAT gap in the current working tree (including uncommitted changes). Do not fix code. Find the root cause only.\n\nGap truth: session remains active and protected UI stays accessible\nExpected: after login, refreshing a protected page should keep the session active and the protected UI accessible.\nActual: user reported \"when refreshing the UI i'm being redirected to the login page.\"\nErrors: none reported beyond redirect behavior.\nReproduction: Test 2 in /home/scarpellini/Documents/Projetos/aprimorar/.planning/phases/01-authentication-protected-access/01-HUMAN-UAT.md\nTimeline: discovered during UAT\nGoal: find_root_cause_only"
created: 2026-04-18T00:00:00Z
updated: 2026-04-18T00:30:00Z
---

## Current Focus

hypothesis: the SPA never applies its shared axios credential config, so generated auth requests run without withCredentials; login looks successful only because currentUser is written into React Query from the login response, but refresh reboots from /v1/auth/me with no session cookie and redirects to /login
test: compare generated auth hooks with shared axios bootstrap imports and inspect the uncommitted login-session changes in useAuthSession
expecting: no runtime import of client/src/lib/shared/api.ts, generated hooks using kubb axios client directly, and login flow masking the missing persisted session until refresh
next_action: return root-cause diagnosis with frontend file evidence

## Symptoms

expected: after login, refreshing a protected page should keep the session active and the protected UI accessible.
actual: user reported "when refreshing the UI i'm being redirected to the login page."
errors: none reported beyond redirect behavior.
reproduction: Test 2 in /home/scarpellini/Documents/Projetos/aprimorar/.planning/phases/01-authentication-protected-access/01-HUMAN-UAT.md
started: discovered during UAT

## Eliminated

## Evidence

- timestamp: 2026-04-18T00:10:00Z
  checked: client/src/lib/shared/api.ts and imports across client/src
  found: api.ts is the only place that sets axiosInstance.defaults with withCredentials/XSRF config, and grep found no runtime import of that module from main.tsx, App.tsx, or auth/session code.
  implication: generated Kubb hooks keep using the default axios client instead of the credentialed one, so browser auth requests do not reliably persist/send the server session cookie.

- timestamp: 2026-04-18T00:15:00Z
  checked: client/src/kubb/hooks/auth/useMe.ts
  found: useMe imports fetch from @kubb/plugin-client/clients/axios directly and never references client/src/lib/shared/api.ts.
  implication: session bootstrap on refresh depends on the unconfigured generated axios client, so /v1/auth/me can run without credentials and come back unauthorized.

- timestamp: 2026-04-18T00:20:00Z
  checked: git diff for client/src/features/auth/hooks/useAuthSession.ts and client/src/features/auth/pages/LoginPage.tsx
  found: current working tree changed login success from invalidate+refetch /v1/auth/me to queryClient.setQueryData(authenticatedUser) and removed explicit navigate-to-root after login.
  implication: the UI now trusts the login response body as a local auth source of truth, masking the missing persisted session until the page refreshes and the app has to bootstrap from /v1/auth/me again.

- timestamp: 2026-04-18T00:25:00Z
  checked: server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java and SecurityConfig.java
  found: backend is designed for server-managed sessions (/login stores SecurityContext in HttpSession and /me reads Authentication from the restored context).
  implication: if refresh loses auth, the failure is upstream of controller logic—at the browser/client credential boundary rather than the session creation code itself.

## Resolution

root_cause: "The frontend never imports client/src/lib/shared/api.ts, so the Kubb axios client never receives withCredentials/XSRF defaults. Login still appears to work because the current working tree writes the login response directly into the auth query cache, but the browser session is not actually re-bootstrapable; after refresh, /v1/auth/me runs through the unconfigured client, lacks the authenticated session context, returns unauthorized, and AuthGate redirects to /login." 
fix: ""
verification: ""
files_changed:
  - client/src/lib/shared/api.ts
  - client/src/main.tsx
  - client/src/kubb/hooks/auth/useMe.ts
  - client/src/features/auth/hooks/useAuthSession.ts
  - client/src/features/auth/pages/LoginPage.tsx
