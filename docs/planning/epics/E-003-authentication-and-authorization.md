# Epic: E-003 — Authentication and Authorization
**Goal:** Require authenticated access and enforce role-based authorization across the API and UI.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 2

## Scope
- In scope:
  - JWT-based authentication (login, token issuance, refresh, logout)
  - Password hashing (BCrypt)
  - RBAC permission matrix and enforcement
  - Security hardening baseline (rate limiting, headers, CSRF decisions, audit logging)
  - Privacy hardening for confidential session types
- Out of scope:
  - Building a full parent portal experience (tracked but not required for MVP)

## Stories

### Story: S-020 — User Authentication
**As a** user **I want** to log in securely **so that** I can access the system.

**Acceptance Criteria**
- [ ] Login endpoint authenticates credentials and returns JWT
- [ ] Passwords are stored hashed (BCrypt)
- [ ] Refresh token flow exists (if required for UX/security)
- [ ] Logout invalidates refresh tokens (if implemented)
- [ ] HTTPS/TLS and CORS policies are configured for deployment and local dev

**Test Plan**
- Backend:
  - [ ] Integration tests for login + protected endpoints
- Frontend:
  - [ ] N/A (backend capability)
- Manual:
  - [ ] Verify token works end-to-end with the React app

### Story: S-021 — Role-Based Access Control
**As a** administrator **I want** role-based permissions **so that** users can only perform authorized actions.

**Acceptance Criteria**
- [ ] Permission matrix exists and is documented
- [ ] RBAC enforced via Spring Security (`@PreAuthorize` or equivalent)

**Test Plan**
- Backend:
  - [ ] Security tests per role for key endpoints
- Frontend:
  - [ ] N/A (backend capability)
- Manual:
  - [ ] Verify role restrictions via UI for at least one restricted action

### Story: S-022 — Security Hardening
**As a** security engineer **I want** baseline hardening applied **so that** the system meets security best practices.

**Acceptance Criteria**
- [ ] Rate limiting is in place
- [ ] Security headers configured
- [ ] Audit logging exists for sensitive operations

**Test Plan**
- Backend:
  - [ ] Add tests for hardening behavior where applicable
- Frontend:
  - [ ] N/A
- Manual:
  - [ ] Verify rate limiting behavior and logs

### Story: S-023 — Privacy Hardening for Confidential Session Types
**As a** administrator **I want** confidential session types restricted and audited **so that** mentorship and psychological consulting data is protected.

**Acceptance Criteria**
- [ ] Access restricted to ADMIN and assigned employee (at minimum)
- [ ] Reads of confidential events are audit logged
- [ ] List views optionally redact sensitive fields for non-privileged roles

**Test Plan**
- Backend:
  - [ ] Authorization tests for confidential event access
- Frontend:
  - [ ] Verify list view redaction behavior (if implemented)
- Manual:
  - [ ] Attempt confidential event access with each role and confirm auditing

## Tasks

### Task: T-020 — Implement JWT authentication
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Implement login/token issuance and supporting security configuration.

**Subtasks**
- [ ] ST-050 — Implement JWT token generation (legacy: T-2.1.1)
- [ ] ST-051 — Implement login endpoint (legacy: T-2.1.2)
- [ ] ST-052 — Implement password hashing (BCrypt) (legacy: T-2.1.3)
- [ ] ST-053 — Configure HTTPS/TLS (legacy: T-2.1.4)
- [ ] ST-054 — Configure CORS policy (legacy: T-2.1.5)
- [ ] ST-055 — Implement refresh token mechanism (legacy: T-2.1.6)
- [ ] ST-056 — Implement logout functionality (legacy: T-2.1.7)

**Files likely affected (best guess)**
- server/
- client/ (auth token handling)

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Integration test login + a protected endpoint
- Frontend: Confirm token storage and authenticated requests
- Manual: Log in via UI and verify navigation + API calls succeed

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 2 / User Story 2.1

### Task: T-021 — Implement RBAC
**Type:** backend
**Status:** TODO
**Depends on:** T-020

**Description**
- Define and enforce role permissions across endpoints.

**Subtasks**
- [ ] ST-057 — Define permission matrix (legacy: T-2.2.1)
- [ ] ST-058 — Implement `@PreAuthorize` annotations (legacy: T-2.2.2)
- [ ] ST-059 — Create admin-only endpoints (legacy: T-2.2.3)
- [ ] ST-060 — Implement employee self-service (legacy: T-2.2.4)
- [ ] ST-061 — Implement parent read-only portal (legacy: T-2.2.5)

**Files likely affected (best guess)**
- server/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Add security tests for at least ADMIN vs EMPLOYEE on key endpoints
- Frontend: N/A
- Manual: Attempt forbidden actions and confirm 403

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 2 / User Story 2.2

### Task: T-022 — Security hardening baseline
**Type:** backend
**Status:** TODO
**Depends on:** T-020

**Description**
- Add hardening controls that reduce common attack surfaces.

**Subtasks**
- [ ] ST-062 — Implement rate limiting (legacy: T-2.3.1)
- [ ] ST-063 — Add CSRF protection (legacy: T-2.3.2)
- [ ] ST-064 — Implement audit logging (legacy: T-2.3.3)
- [ ] ST-065 — Add security headers (legacy: T-2.3.4)

**Files likely affected (best guess)**
- server/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Validate headers/rate limiting in integration tests where feasible
- Frontend: N/A
- Manual: Confirm rate limiting and headers in a dev deployment

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 2 / User Story 2.3

### Task: T-023 — Privacy hardening for confidential session types
**Type:** backend
**Status:** TODO
**Depends on:** T-021

**Description**
- Restrict and audit access to confidential events (mentorship/psychological consulting).

**Subtasks**
- [ ] ST-066 — Define confidential event permission matrix (legacy: T-2.4.1)
- [ ] ST-067 — Restrict list/details access to ADMIN and assigned employee (legacy: T-2.4.2)
- [ ] ST-068 — Add audit logging for reads of confidential events (legacy: T-2.4.3)
- [ ] ST-069 — Consider redacting fields in list views for non-privileged roles (legacy: T-2.4.4)

**Files likely affected (best guess)**
- server/
- client/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Authorization tests for confidential event list/detail
- Frontend: Verify redaction rules in list views (if applied)
- Manual: Confirm audit logging records confidential reads

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 2 / User Story 2.4

**Review Notes (append-only)**
- Reviewer notes:
