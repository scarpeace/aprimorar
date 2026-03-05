# Epic: E-003 — Authentication and Authorization
**Goal:** Require authenticated access and enforce role-based authorization across API and UI.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 2

## Scope
- In scope:
  - JWT authentication (login, token issuance, refresh, logout)
  - Password hashing (BCrypt)
  - RBAC enforcement
  - Security hardening baseline (rate limiting, headers, audit)
  - Privacy hardening for sensitive event content
- Out of scope:
  - Full parent portal product scope

## Workboard
- Current focus: None started
- Blocked: None
- Next up: T-020 (JWT authentication baseline)

## Stories
### Story: S-020 — User Authentication
**Status:** TODO
**Links:** T-033 (TODO)
**Intent:** Enable secure login and token-based access.

### Story: S-021 — Role-Based Access Control
**Status:** TODO
**Links:** T-034 (TODO)
**Intent:** Ensure users can only perform authorized actions.

### Story: S-022 — Security Hardening
**Status:** TODO
**Links:** T-035 (TODO)
**Intent:** Reduce common attack surfaces with baseline controls.

### Story: S-023 — Privacy Hardening for Sensitive Content
**Status:** TODO
**Links:** T-036 (TODO)
**Intent:** Restrict/audit access to sensitive event categories.

## Tasks
### Task: T-033 — Implement JWT authentication
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Implement login, JWT issuance, refresh, logout, and base security config.

**Subtasks**
- [ ] ST-050 — Implement JWT token generation
- [ ] ST-051 — Implement login endpoint
- [ ] ST-052 — Implement password hashing (BCrypt)
- [ ] ST-053 — Configure HTTPS/TLS
- [ ] ST-054 — Configure CORS policy
- [ ] ST-055 — Implement refresh token mechanism
- [ ] ST-056 — Implement logout functionality

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: integration tests for login + protected endpoint
- Frontend: token usage smoke test
- Manual: log in via UI and verify authenticated requests

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 2 / User Story 2.1

### Task: T-034 — Implement RBAC
**Type:** backend
**Status:** TODO
**Depends on:** T-033

**Description**
- Define and enforce role permissions across endpoints.

**Subtasks**
- [ ] ST-057 — Define permission matrix
- [ ] ST-058 — Implement `@PreAuthorize` rules
- [ ] ST-059 — Create admin-only endpoints
- [ ] ST-060 — Implement employee self-service
- [ ] ST-061 — Implement parent read-only portal

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: security tests for ADMIN vs EMPLOYEE on key endpoints
- Frontend: N/A
- Manual: attempt forbidden actions and confirm 403

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 2 / User Story 2.2

### Task: T-035 — Security hardening baseline
**Type:** backend
**Status:** TODO
**Depends on:** T-033

**Description**
- Add hardening controls (rate limiting, headers, CSRF decisions, audit logging).

**Subtasks**
- [ ] ST-062 — Implement rate limiting
- [ ] ST-063 — Add CSRF protection
- [ ] ST-064 — Implement audit logging
- [ ] ST-065 — Add security headers

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: verify headers/rate limiting behavior
- Frontend: N/A
- Manual: confirm controls in dev deployment

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 2 / User Story 2.3

### Task: T-036 — Privacy hardening for sensitive content
**Type:** backend
**Status:** TODO
**Depends on:** T-034

**Description**
- Restrict and audit access to sensitive event content.

**Subtasks**
- [ ] ST-066 — Define sensitive-content permission matrix
- [ ] ST-067 — Restrict list/detail access to ADMIN and assigned employee
- [ ] ST-068 — Add audit logs for sensitive reads
- [ ] ST-069 — Evaluate redaction in list views for non-privileged roles

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: authorization tests for sensitive list/detail
- Frontend: verify redaction behavior (if applied)
- Manual: confirm audit logging for sensitive reads

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 2 / User Story 2.4

## Archive (DONE)
- No DONE tasks archived yet.

## Review Notes (append-only)
- Reviewer notes:
