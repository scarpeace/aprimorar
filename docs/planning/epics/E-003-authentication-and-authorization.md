# Epic: E-003 - Authentication and Authorization

**Goal:** Require authenticated access and enforce simple, practical role-based permissions.
**Status:** TODO
**Phase:** Phase 2

## Scope

- In scope:
  - login flow and token issuance
  - password hashing and credential storage rules
  - route and endpoint protection
  - baseline RBAC
  - basic security hardening needed for MVP
- Out of scope:
  - parent portal product work
  - advanced enterprise security features

## Workboard

- Current focus: none
- Blocked: none
- Next up: login/auth skeleton before broader RBAC work

## Stories

### Story: S-020 - Login and Session Baseline

**Status:** TODO
**Intent:** Let staff sign in safely and let the app call protected endpoints.
**Links:** T-033, T-034, T-035

**Acceptance Criteria**

- Users can log in with stored credentials.
- Protected endpoints reject unauthenticated requests.

### Story: S-021 - Role-Based Access Control

**Status:** TODO
**Intent:** Keep permissions simple and explicit for admin and employee roles.
**Links:** T-036, T-037

**Acceptance Criteria**

- Core endpoints have a documented permission matrix.
- Forbidden access returns the expected HTTP status.

### Story: S-022 - Security Hardening Baseline

**Status:** TODO
**Intent:** Add the minimum hardening needed so auth does not ship as a thin shell.
**Links:** T-038, T-039

**Acceptance Criteria**

- Passwords are hashed.
- CORS, headers, and audit basics are in place.

## Tasks

### Task: T-033 - Add authentication domain and login endpoint

**Type:** backend
**Status:** TODO
**Depends on:** None

**Outcome**

- Introduce the minimal user/auth pieces needed for login and token issuance.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Log in with a seeded or local admin account

### Task: T-034 - Protect backend routes with auth middleware/config

**Type:** backend
**Status:** TODO
**Depends on:** T-033

**Outcome**

- Require authentication for non-public endpoints and return clear 401 responses.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Call protected endpoints with and without a token

### Task: T-035 - Add frontend auth shell

**Type:** fullstack
**Status:** TODO
**Depends on:** T-033

**Outcome**

- Add a basic login screen, token storage strategy, and protected-route behavior.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Log in and navigate to a protected screen

### Task: T-036 - Define and implement the MVP permission matrix

**Type:** backend
**Status:** TODO
**Depends on:** T-034

**Outcome**

- Document and enforce a practical admin/employee permission model.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Confirm a forbidden action returns 403

### Task: T-037 - Add authorization tests for core entities

**Type:** backend
**Status:** TODO
**Depends on:** T-036

**Outcome**

- Add focused security tests around students, employees, events, and dashboard endpoints.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Spot-check one allowed and one forbidden path per role

### Task: T-038 - Add password hashing and credential safety basics

**Type:** backend
**Status:** TODO
**Depends on:** T-033

**Outcome**

- Store credentials safely and avoid plain-text password handling.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Verify stored credentials are hashed

### Task: T-039 - Add baseline CORS, headers, and audit logging

**Type:** backend
**Status:** TODO
**Depends on:** T-034

**Outcome**

- Add lightweight hardening that matches the chosen auth model.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Inspect response headers and auth-related audit entries in dev

## Archive

- No completed auth tasks yet.

## Review Notes

- 2026-03-07: Split auth work into smaller slices so login can land before broader RBAC and hardening follow-ups.
