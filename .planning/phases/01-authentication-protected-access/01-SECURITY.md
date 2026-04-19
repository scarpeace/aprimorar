---
phase: 01
slug: authentication-protected-access
status: verified
threats_open: 0
asvs_level: 1
created: 2026-04-19
---

# Phase 01 — Security

> Per-phase security contract: threat register, accepted risks, and audit trail.

---

## Trust Boundaries

| Boundary | Description | Data Crossing |
|----------|-------------|---------------|
| browser -> `/v1/auth/login` | Untrusted credential input enters the authentication flow. | username/email, password |
| browser cookie/session -> protected `/v1/**` endpoints | Browser-held session state is presented to the API for protected business access. | `JSESSIONID`, auth context |
| frontend dev origin -> backend API | Cross-origin credentialed SPA requests depend on correct CORS and CSRF handling. | cookies, XSRF token, JSON payloads |
| generated Kubb hooks -> handwritten auth wrapper | Generated contract data feeds handwritten SPA auth state and route gating. | current-user DTO, auth mutation results |
| query cache -> post-logout UI | Cached protected data can survive after auth teardown if not cleared. | dashboard/student/parent/employee/event query data |

---

## Threat Register

| Threat ID | Category | Component | Disposition | Mitigation | Status |
|-----------|----------|-----------|-------------|------------|--------|
| T-01-01 | S | `POST /v1/auth/login` | mitigate | Spring Security + BCrypt auth flow using `StaffAccountDetailsService` and `StaffAccountRepository`; inactive users rejected. Evidence: `SecurityConfig.java:96-100`, `StaffAccountDetailsService.java:22-29`, `StaffAccountRepository.java:13-23` | closed |
| T-01-02 | T | session-backed write endpoints | mitigate | CSRF remains enabled with `CookieCsrfTokenRepository.withHttpOnlyFalse()` and SPA-aware token handling. Evidence: `SecurityConfig.java:57-61` | closed |
| T-01-03 | I | protected `/v1/**` controllers | mitigate | Security filter chain requires authentication for protected API routes. Evidence: `SecurityConfig.java:70-82` | closed |
| T-01-04 | E | CORS/session configuration | mitigate | CORS restricted to `http://localhost:5173`, credentials explicitly enabled, unauthorized/denied responses normalized as JSON. Evidence: `WebCorsConfig.java:12-17`, `SecurityConfig.java:66-69`, `SecurityConfig.java:109-129` | closed |
| T-01-05 | T | `client/src/lib/shared/api.ts` | mitigate | Shared axios/Kubb client sends credentialed requests only to configured backend origin with Spring-compatible XSRF names. Evidence: `client/src/lib/shared/api.ts:5-18` | closed |
| T-01-06 | I | `client/src/features/auth/pages/LoginPage.tsx` | mitigate | Login submits directly to backend session endpoint and does not persist credentials in browser storage. Evidence: `LoginPage.tsx:40-55` | closed |
| T-01-07 | R | `client/src/features/auth/hooks/useAuthSession.ts` | mitigate | Login/logout/current-user lifecycle is centralized in one auth wrapper. Evidence: `useAuthSession.ts:12-61` | closed |
| T-01-08 | E | `client/src/App.tsx` | mitigate | Protected SPA routes are wrapped by the shared auth gate so anonymous users are redirected to `/login`. Evidence: `App.tsx:49-84` | closed |
| T-01-09 | I | `client/src/components/layout/MainLayout.tsx` | mitigate | Logout triggers server-side teardown and redirects away from protected UI. Evidence: `MainLayout.tsx:46-53`, `MainLayout.tsx:88-97` | closed |
| T-01-10 | T | `client/src/lib/shared/queryClient.ts` | mitigate | Protected query cache is cleared on logout to prevent stale private data exposure. Evidence: `queryClient.ts:3-26`, `MainLayout.tsx:48-50` | closed |
| T-01-11 | T | `client/src/lib/shared/api.ts` | mitigate | Shared auth client forces XSRF header forwarding for cross-origin cookie-authenticated mutations. Evidence: `client/src/lib/shared/api.ts:7-18` | closed |
| T-01-12 | E | `client/src/main.tsx` | mitigate | Shared API bootstrap is imported before React renders, so auth requests never run on an unconfigured client. Evidence: `client/src/main.tsx:5-17` | closed |
| T-01-13 | S | `client/src/features/auth/hooks/useAuthSession.ts` | mitigate | Authenticated SPA state is derived from real `/v1/auth/me` bootstrap rather than optimistic cached login data. Evidence: `useAuthSession.ts:15-18`, `useAuthSession.ts:29-39`, `useAuthSession.ts:53-57` | closed |

*Status: open · closed*
*Disposition: mitigate (implementation required) · accept (documented risk) · transfer (third-party)*

---

## Accepted Risks Log

No accepted risks.

---

## Security Audit Trail

| Audit Date | Threats Total | Closed | Open | Run By |
|------------|---------------|--------|------|--------|
| 2026-04-19 | 13 | 13 | 0 | the agent (gsd-security-auditor + orchestrator) |

---

## Sign-Off

- [x] All threats have a disposition (mitigate / accept / transfer)
- [x] Accepted risks documented in Accepted Risks Log
- [x] `threats_open: 0` confirmed
- [x] `status: verified` set in frontmatter

**Approval:** verified 2026-04-19
