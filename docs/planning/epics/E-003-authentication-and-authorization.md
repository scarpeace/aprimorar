# Epic: E-003 — Authentication and Authorization
**Goal:** Require authenticated access and enforce role-based authorization across API and UI.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 2

## Scope
- In scope:
  - JWT authentication, login, refresh, and logout
  - Password hashing
  - RBAC enforcement
  - Baseline hardening: headers, rate limiting, CSRF decisions, audit logging
  - Privacy controls for sensitive event content
- Out of scope:
  - Full parent portal product scope

## Workboard
- Current focus: None
- Blocked: None
- Next up: T-033 JWT authentication baseline

## Stories
### Story: S-020 — User authentication
**Status:** TODO
**Links:** T-033 (TODO)
**Intent:** Enable secure login and token-based access.

### Story: S-021 — Role-based access control
**Status:** TODO
**Links:** T-034 (TODO)
**Intent:** Restrict operations by role and ownership.

### Story: S-022 — Security hardening
**Status:** TODO
**Links:** T-035 (TODO)
**Intent:** Reduce common attack surfaces with baseline controls.

### Story: S-023 — Privacy hardening for sensitive content
**Status:** TODO
**Links:** T-036 (TODO)
**Intent:** Restrict and audit access to sensitive event categories.

## Tasks
### Task: T-033 — Implement JWT authentication
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Implement login, token issuance, refresh, logout, and base security config.

**DoD**
- [ ] Auth flow works for protected endpoints
- [ ] Tests or verification updated
- [ ] Local verification done

**Verification**
- Backend: add integration tests for login and one protected endpoint
- Frontend: smoke test token usage
- Manual: log in via UI and verify authenticated requests

**Notes**
- Risks: token lifecycle and secret management decisions can create contract churn.

### Task: T-034 — Implement RBAC
**Type:** backend
**Status:** TODO
**Depends on:** T-033

**Description**
- Define permission matrix and enforce it across endpoints.

**DoD**
- [ ] Permission matrix defined
- [ ] Authorization enforced on key endpoints
- [ ] Local verification done

**Verification**
- Backend: security tests for ADMIN vs EMPLOYEE on key endpoints
- Frontend: N/A
- Manual: attempt forbidden actions and confirm 403

### Task: T-035 — Security hardening baseline
**Type:** backend
**Status:** TODO
**Depends on:** T-033

**Description**
- Add headers, rate limiting, audit logging, and CSRF/TLS decisions.

**DoD**
- [ ] Baseline controls implemented
- [ ] Verification updated
- [ ] Local verification done

**Verification**
- Backend: verify headers and rate-limiting behavior
- Frontend: N/A
- Manual: confirm controls in dev deployment

### Task: T-036 — Privacy hardening for sensitive content
**Type:** backend
**Status:** TODO
**Depends on:** T-034

**Description**
- Restrict and audit sensitive event content access.

**DoD**
- [ ] Sensitive access rules implemented
- [ ] Authorization/audit verification updated
- [ ] Local verification done

**Verification**
- Backend: authorization tests for sensitive list/detail access
- Frontend: verify redaction behavior if applied
- Manual: confirm audit logging for sensitive reads

## Archive (DONE)
- None.

## Review Notes (append-only)
- 2026-03-08:
  - Quality: Kept epic intentionally concise; details should be added only when implementation decisions are approved.
  - Security: This epic gates meaningful auth-aware hardening elsewhere.
  - Performance: Keep security middleware lightweight on hot paths.
