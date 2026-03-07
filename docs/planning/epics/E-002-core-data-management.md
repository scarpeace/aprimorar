# Epic: E-002 - Core Data and Operations Baseline

**Goal:** Keep the core data model and API reliable for daily operations and finance workflows.
**Status:** IN_PROGRESS
**Phase:** Phase 1

## Scope

- In scope:
  - dashboard/backend data needed for operations and finance
  - event response improvements for frontend/reporting
  - parent flow support for student management
  - backend consistency fixes around queries and validation
  - risky schema follow-ups that block real-world usage
- Out of scope:
  - auth and RBAC
  - calendar sync and calendar views
  - payment gateway work

## Workboard

- Current focus: dashboard summary and event response contract improvements
- Blocked: none
- Next up: uniqueness cleanup and query consistency follow-ups

## Stories

### Story: S-010 - Student and Parent Operations

**Status:** DONE
**Intent:** Keep student management practical for staff, including parent assignment flows.
**Links:** T-029, T-030

**Acceptance Criteria**

- Student flows support parent selection without extra manual work.
- Parent-related API behavior is clear and safe for create/edit flows.

### Story: S-011 - Dashboard Data Contract

**Status:** TODO
**Intent:** Move dashboard aggregation out of the frontend and into a simple backend summary endpoint.
**Links:** T-031, T-032

**Acceptance Criteria**

- Backend exposes one summary endpoint for monthly KPIs and upcoming events.
- Summary logic matches MVP finance rules and Sao Paulo time windows.

### Story: S-012 - Event Response Quality

**Status:** TODO
**Intent:** Return event data in a format the frontend and finance views can use directly.
**Links:** T-033, T-034

**Acceptance Criteria**

- Event responses include formatted date/time fields and `profit`.
- Existing date-time fields remain available for compatibility.

### Story: S-013 - Backend Consistency and Safety

**Status:** TODO
**Intent:** Reduce avoidable backend surprises in queries, validation, and schema rules.
**Links:** T-035, T-036, T-037

**Acceptance Criteria**

- Event listing avoids obvious N+1 behavior.
- Query parameter limits are consistent.
- Real-world uniqueness constraints are less risky.

## Tasks

### Task: T-029 - Confirm parent endpoints and contracts for inline student flows

**Type:** backend
**Status:** DONE
**Depends on:** None

**Outcome**

- Validate or adjust parent create/update/list contracts so E-004 can build inline parent create/edit safely.

**Definition of Done**

- [x] Code or docs updated
- [x] Verification completed
- [x] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Create or update a student using both existing and newly created parent data

### Task: T-030 - Decide and document parent management path in student edit flows

**Type:** docs
**Status:** DONE
**Depends on:** T-029

**Outcome**

- Document the exact backend/frontend flow for parent selection, inline creation, and reassignment.

**Definition of Done**

- [x] Code or docs updated
- [x] Verification completed
- [x] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Confirm flow is clear enough to implement without guesswork

### Task: T-031 - Add dashboard summary endpoint

**Type:** backend
**Status:** TODO
**Depends on:** None

**Outcome**

- Add `GET /v1/dashboard/summary` with active counts, current-month revenue/cost/profit, and upcoming events.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Validate response against seed data and MVP finance rules

### Task: T-032 - Add repository/service coverage for dashboard windows

**Type:** backend
**Status:** TODO
**Depends on:** T-031

**Outcome**

- Add tests for month windowing, upcoming windowing, and null-safe profit totals.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Inspect boundary dates around month start/end in `America/Sao_Paulo`

### Task: T-033 - Add formatted event fields and profit to event responses

**Type:** backend
**Status:** TODO
**Depends on:** None

**Outcome**

- Return `startDate`, `startTime`, `endDate`, `endTime`, and `profit` on event responses while keeping existing fields.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Inspect event list/detail responses in Swagger

### Task: T-034 - Document event response contract usage for frontend/reporting

**Type:** docs
**Status:** TODO
**Depends on:** T-033

**Outcome**

- Reflect the new event response fields in canonical docs so client work can consume them directly.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: N/A
- Manual: Confirm docs match the implemented response

### Task: T-035 - Fix or rule out N+1 behavior in event listing

**Type:** backend
**Status:** TODO
**Depends on:** None

**Outcome**

- Use a simple repository strategy that avoids avoidable extra queries in event lists.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Inspect SQL/log behavior on paginated event list requests

### Task: T-036 - Standardize list query parameter validation

**Type:** backend
**Status:** TODO
**Depends on:** None

**Outcome**

- Make pagination parameter rules consistent across controllers, especially `size` limits.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Try oversized page sizes on affected endpoints

### Task: T-037 - Relax risky uniqueness constraints

**Type:** backend
**Status:** TODO
**Depends on:** None

**Outcome**

- Remove or reduce uniqueness constraints on fields like `name` where they do not match real-world usage.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Create realistic duplicate-name records and confirm expected behavior

## Archive

### Task: T-010 - T-028 completed baseline work

- Status: DONE
- Scope: Student filtering, event filters, event `content`, student simplification, and student archive semantics were completed earlier.
- Verification: Prior backend/frontend test passes were recorded in the earlier epic history.

## Review Notes

- 2026-03-07: Reframed the epic around the remaining MVP ops/finance gaps instead of preserving a long historical task ledger.
- 2026-03-07: Locked the student/parent flow: create requires either existing-parent selection or inline parent creation; edit supports current-parent updates or reassignment to another existing parent.
