---
phase: 1
slug: authentication-protected-access
status: draft
nyquist_compliant: true
wave_0_complete: true
created: 2026-04-17
---

# Phase 1 — Validation Strategy

> Per-phase validation contract for feedback sampling during execution.

---

## Test Infrastructure

| Property | Value |
|----------|-------|
| **Framework** | JUnit 5 + Mockito + AssertJ; frontend uses lint/build only |
| **Config file** | `server/api-aprimorar/pom.xml`; no dedicated frontend test runner |
| **Quick run command** | `cd server/api-aprimorar && ./mvnw -Dtest='*Auth*Test,*Security*Test' test` |
| **Full suite command** | `cd server/api-aprimorar && ./mvnw test && cd ../../client && npm run lint && npm run build` |
| **Estimated runtime** | ~45 seconds |

---

## Sampling Rate

- **After every task commit:** Run `cd server/api-aprimorar && ./mvnw -Dtest='*Auth*Test,*Security*Test' test`
- **After every plan wave:** Run `cd server/api-aprimorar && ./mvnw test && cd ../../client && npm run lint && npm run build`
- **Before `/gsd-verify-work`:** Full suite must be green
- **Max feedback latency:** 45 seconds

---

## Per-Task Verification Map

| Task ID | Plan | Wave | Requirement | Threat Ref | Secure Behavior | Test Type | Automated Command | File Exists | Status |
|---------|------|------|-------------|------------|-----------------|-----------|-------------------|-------------|--------|
| 1-01-01 | 01 | 1 | AUTH-01, AUTH-02 | T-01-01 | Valid identifier+password creates authenticated session and `/v1/auth/me` resolves current user | unit/http | `cd server/api-aprimorar && ./mvnw -Dtest='*Auth*Test,*Security*Test' test` | ✅ | ⬜ pending |
| 1-02-01 | 02 | 2 | AUTH-01, AUTH-03 | T-01-02 | SPA login/logout flow respects server session and shows Portuguese errors | build | `cd client && npm run lint && npm run build` | ✅ | ⬜ pending |
| 1-03-01 | 03 | 2 | AUTH-04 | T-01-03 | Protected API/routes reject anonymous access and redirect to `/login` in SPA | full | `cd server/api-aprimorar && ./mvnw test && cd ../../client && npm run lint && npm run build` | ✅ | ⬜ pending |

*Status: ⬜ pending · ✅ green · ❌ red · ⚠️ flaky*

---

## Wave 0 Requirements

Existing infrastructure covers all phase requirements.

---

## Manual-Only Verifications

| Behavior | Requirement | Why Manual | Test Instructions |
|----------|-------------|------------|-------------------|
| Login survives refresh and protected navigation | AUTH-02, AUTH-04 | Requires browser cookie/session behavior across navigation | Start backend + frontend, log in, refresh `/students`, confirm app stays authenticated and content remains accessible |
| Logout blocks direct protected route reopen | AUTH-03, AUTH-04 | Requires browser redirect and session teardown confirmation | Log out, then navigate directly to `/`, `/students`, `/parents`, `/employees`, `/events`; expect redirect/login denial |

---

## Validation Sign-Off

- [x] All tasks have `<automated>` verify or Wave 0 dependencies
- [x] Sampling continuity: no 3 consecutive tasks without automated verify
- [x] Wave 0 covers all MISSING references
- [x] No watch-mode flags
- [x] Feedback latency < 45s
- [x] `nyquist_compliant: true` set in frontmatter

**Approval:** pending
