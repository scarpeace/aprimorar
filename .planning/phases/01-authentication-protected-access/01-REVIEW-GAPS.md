---
phase: 01-authentication-protected-access
reviewed: 2026-04-18T18:15:38Z
depth: standard
files_reviewed: 12
files_reviewed_list:
  - client/src/lib/shared/api.ts
  - client/src/main.tsx
  - client/src/features/auth/hooks/useAuthSession.ts
  - client/src/features/auth/pages/LoginPage.tsx
  - client/src/features/auth/components/AuthGate.tsx
  - client/src/components/layout/MainLayout.tsx
  - client/src/lib/shared/queryClient.ts
  - client/src/kubb/hooks/auth/useMe.ts
  - client/src/kubb/hooks/auth/useLogin.ts
  - client/src/kubb/hooks/auth/useLogout.ts
  - server/api-aprimorar/src/main/java/com/aprimorar/api/config/SecurityConfig.java
  - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/auth/AuthController.java
findings:
  critical: 0
  warning: 2
  info: 0
  total: 2
status: issues_found
---

# Phase 1: Gap-Closure Code Review Report

**Reviewed:** 2026-04-18T18:15:38Z
**Depth:** standard
**Files Reviewed:** 12
**Status:** issues_found

## Summary

Reviewed the 01-04 gap-closure changes (`api.ts`, `main.tsx`, `useAuthSession.ts`) plus the directly related auth wiring around login bootstrap, protected routing, logout, and backend CSRF/session endpoints. The refresh/logout direction is correct, but two correctness gaps remain in the client bootstrap flow.

## Warnings

### WR-01: Login still reports success even when `/v1/auth/me` bootstrap fails

**File:** `client/src/features/auth/hooks/useAuthSession.ts:29-38`
**Issue:** `refetchCurrentUser()` returns `currentUserQuery.refetch()`, but React Query's `refetch()` resolves with an error result by default instead of throwing. That means `login.onSuccess` can still show `"Login realizado com sucesso"` even if the follow-up `/v1/auth/me` request fails (for example, cookie/session/bootstrap issues). This does not fully satisfy the plan's requirement that login success be confirmed by a real session bootstrap.
**Fix:** Make the post-login bootstrap fail closed by checking the refetch result or by using a throwing fetch path.

```ts
const refetchCurrentUser = async () => {
  const result = await currentUserQuery.refetch({ throwOnError: true });

  if (!result.data) {
    throw new Error("Não foi possível confirmar a sessão autenticada.");
  }

  return result.data;
};
```

### WR-02: Shared auth bootstrap does not actually override Kubb's hardcoded auth base URL

**File:** `client/src/lib/shared/api.ts:18`
**Issue:** The new startup import correctly runs early, but the bootstrap relies on `axiosInstance.defaults`. The generated auth hooks still pass a request-level `baseURL: "http://localhost:8080"` (`client/src/kubb/hooks/auth/useMe.ts:40`, `useLogin.ts:47`, `useLogout.ts:39`), and request config wins over axios defaults. As a result, `VITE_API_URL` is not reliably applied to auth/bootstrap requests, so refresh persistence and logout can still break outside the local default backend.
**Fix:** Move base URL control into generation/runtime config that the generated requests actually use, or explicitly override the client config when calling the generated hooks.

```ts
// Preferred direction: regenerate Kubb hooks without a hardcoded baseURL
// and source it from runtime config instead.

useMe({ client: { baseURL: apiBaseUrl } });
useLogin({ client: { baseURL: apiBaseUrl } });
useLogout({ client: { baseURL: apiBaseUrl } });
```

---

_Reviewed: 2026-04-18T18:15:38Z_
_Reviewer: the agent (gsd-code-reviewer)_
_Depth: standard_
