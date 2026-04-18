---
status: investigating
trigger: "Diagnose the Phase 1 UAT gap in the current working tree (including uncommitted changes). Do not fix code. Find the root cause only.\n\nGap truth: redirect to `/login` and no stale protected data shown\nExpected: clicking logout from a protected page should log the user out, clear protected state, and redirect to /login.\nActual: user reported \"when clicking the logout button i'm getting a \"Acesso Negado\" toast. the user is not being logged out\"\nErrors: toast message \"Acesso Negado\"\nReproduction: Test 3 in /home/scarpellini/Documents/Projetos/aprimorar/.planning/phases/01-authentication-protected-access/01-HUMAN-UAT.md\nTimeline: discovered during UAT\nGoal: find_root_cause_only\n\nFocus area hints:\n- client/src/features/auth/hooks/useAuthSession.ts\n- client/src/components/layout/MainLayout.tsx\n- client/src/lib/shared/api.ts\n- server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java\n- server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java\n\nFiles to read:\n- /home/scarpellini/Documents/Projetos/aprimorar/.planning/phases/01-authentication-protected-access/01-HUMAN-UAT.md\n- /home/scarpellini/Documents/Projetos/aprimorar/.planning/STATE.md\n\nReturn exactly:\n## ROOT CAUSE FOUND\n\n**Debug Session:** <path or none>\n\n**Root Cause:** <specific cause with evidence>\n\n**Evidence Summary:**\n- ...\n- ...\n- ...\n\n**Files Involved:**\n- <file>: <issue>\n\n**Suggested Fix Direction:** <brief hint>\n\nIf inconclusive, return ## INVESTIGATION INCONCLUSIVE with remaining possibilities."
created: 2026-04-18T00:00:00Z
updated: 2026-04-18T00:15:00Z
---

## Current Focus

hypothesis: logout fails because the SPA sends a cross-origin POST to /v1/auth/logout without the X-XSRF-TOKEN header, so Spring Security rejects it with the access-denied handler before the controller can invalidate the session
test: compare axios XSRF behavior for cross-origin requests against the shared API config and the server CSRF rules for /v1/auth/logout
expecting: client config will omit withXSRFToken while backend requires CSRF on logout and maps that failure to "Acesso negado"
next_action: return root-cause diagnosis with client/server evidence

## Symptoms

expected: clicking logout from a protected page should log the user out, clear protected state, and redirect to /login.
actual: clicking logout shows an "Acesso Negado" toast and the user is not logged out.
errors: toast message "Acesso Negado"
reproduction: Test 3 in .planning/phases/01-authentication-protected-access/01-HUMAN-UAT.md
started: discovered during UAT

## Eliminated

## Evidence

- timestamp: 2026-04-18T00:05:00Z
  checked: .planning/phases/01-authentication-protected-access/01-HUMAN-UAT.md
  found: Test 3 reports logout from a protected page shows an "Acesso Negado" toast instead of redirecting to /login.
  implication: the failure happens during the logout request path, not after navigation.

- timestamp: 2026-04-18T00:08:00Z
  checked: client/src/components/layout/MainLayout.tsx and client/src/lib/shared/api-errors.ts
  found: handleLogout only shows a toast when logout.mutateAsync throws, and the toast text comes from error.response.data.message.
  implication: the reported toast is the backend response message from the failed logout POST.

- timestamp: 2026-04-18T00:10:00Z
  checked: server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java
  found: POST /v1/auth/logout is permitAll but still covered by CSRF because only POST /v1/auth/login is excluded; the accessDeniedHandler returns 403 with message "Acesso negado".
  implication: a missing/invalid CSRF token on logout produces exactly the user-reported behavior before AuthController.logout runs.

- timestamp: 2026-04-18T00:12:00Z
  checked: client/src/lib/shared/api.ts and client/node_modules/axios/lib/helpers/resolveConfig.js
  found: the shared axios config sets withCredentials, xsrfCookieName, and xsrfHeaderName, but never sets withXSRFToken; axios only copies the XSRF cookie into the header when withXSRFToken is truthy or the request URL is same-origin.
  implication: requests from the SPA origin (localhost:5173) to the API origin (localhost:8080) will not send X-XSRF-TOKEN automatically, so cross-origin logout POSTs are rejected by Spring Security.

- timestamp: 2026-04-18T00:14:00Z
  checked: server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java
  found: AuthController.logout simply calls SecurityContextLogoutHandler and returns 204; there is no controller-side branch that emits "Acesso negado".
  implication: the denial occurs in the security filter chain, confirming CSRF rejection as the root cause.

## Resolution

root_cause: The SPA's shared axios client is configured for cookies but not for cross-origin XSRF header injection. Because the frontend calls http://localhost:8080 from a different origin than Vite, axios does not send X-XSRF-TOKEN on POST /v1/auth/logout. Spring Security still enforces CSRF on logout and answers through the accessDeniedHandler with 403 "Acesso negado", so the logout controller never runs and the session remains active.
fix:
verification:
files_changed: []
