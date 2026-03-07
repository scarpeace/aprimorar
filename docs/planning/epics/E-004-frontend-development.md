# Epic: E-004 - Frontend Operations UI

**Goal:** Deliver and polish the operational frontend used for daily staff workflows.
**Status:** IN_PROGRESS
**Phase:** Phase 3

## Scope

- In scope:
  - dashboard and navigation polish
  - student, employee, and event workflows
  - parent flows needed by student management
  - shared UI conventions and beginner-friendly structure
  - PT-BR UX consistency
- Out of scope:
  - calendar-style scheduling views
  - payment gateway UX

## Workboard

- Current focus: parent inline flow and dashboard integration readiness
- Blocked: dashboard summary depends on E-002 `T-031`; inline parent UX depends on E-002 `T-029`
- Next up: close remaining UTF-8/error-state sweep and manual QA

## Stories

### Story: S-030 - Core Operational Screens

**Status:** DONE
**Intent:** Provide the baseline navigation and management screens for daily use.
**Links:** T-030, T-031, T-032

**Acceptance Criteria**

- Core modules are reachable and usable on desktop and mobile.
- Student and event CRUD flows work without obvious dead ends.

### Story: S-031 - Dashboard Correctness and Simplicity

**Status:** TODO
**Intent:** Keep the dashboard accurate and simple by consuming one backend summary contract.
**Links:** T-056, T-057

**Acceptance Criteria**

- Dashboard uses backend summary data instead of client aggregation.
- KPI labels and quick actions match MVP priorities.

### Story: S-032 - Parent Flow in Student UI

**Status:** TODO
**Intent:** Let staff manage parent assignment without leaving the student workflow.
**Links:** T-058, T-059

**Acceptance Criteria**

- Student create/edit supports selecting an existing parent.
- Inline parent create or edit flow works without confusing context switching.

### Story: S-033 - UX Copy and Error-State Polish

**Status:** IN_PROGRESS
**Intent:** Make user-facing PT-BR copy clear, accented, and consistent.
**Links:** T-052, T-053, T-054, T-055

**Acceptance Criteria**

- Core validation, empty states, and common error states use clear PT-BR copy.
- Lint and build stay green after copy cleanup.

## Tasks

### Task: T-055 - Sweep core UI error, toast, and empty-state strings

**Type:** frontend
**Status:** TODO
**Depends on:** T-052

**Outcome**

- Finish the remaining PT-BR/UTF-8 copy cleanup across students, events, employees, and parents.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Trigger validation, API error, and empty states on core screens

### Task: T-056 - Switch dashboard to backend summary endpoint

**Type:** frontend
**Status:** TODO
**Depends on:** T-031

**Outcome**

- Replace client-side dashboard aggregation with the backend summary response from E-002.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Compare dashboard totals to API response and upcoming list

### Task: T-057 - Polish dashboard quick actions and loading states

**Type:** frontend
**Status:** TODO
**Depends on:** T-056

**Outcome**

- Add clear create shortcuts and simple loading/error handling aligned with the summary endpoint.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Click each quick action and test loading/error fallback behavior

### Task: T-058 - Add existing-parent selection to student create/edit

**Type:** frontend
**Status:** TODO
**Depends on:** T-029

**Outcome**

- Make parent selection obvious and stable in student forms.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Create and edit a student using an existing parent

### Task: T-059 - Add inline parent create/edit flow to student forms

**Type:** frontend
**Status:** TODO
**Depends on:** T-058

**Outcome**

- Let staff create or update parent info without leaving the student flow.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Create a new parent during student creation and verify selection persists

### Task: T-060 - Run a focused manual QA pass on core screens

**Type:** frontend
**Status:** TODO
**Depends on:** T-055

**Outcome**

- Validate key students, employees, events, and dashboard flows after the remaining polish tasks land.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Smoke-test create, edit, delete/archive, filters, dashboard, and common error states

## Archive

### Task: T-030 - Dashboard and navigation baseline

- Status: DONE
- Scope: Base routes and navigation shell were delivered.
- Verification: Frontend lint/build and manual route checks.

### Task: T-031 - Student management UI baseline

- Status: DONE
- Scope: Student list, form reuse, detail/edit routing, and shared list patterns were delivered.
- Verification: Frontend lint/build and manual create/edit/delete checks.

### Task: T-032 - Event management UI baseline

- Status: DONE
- Scope: Shared event form, edit flows, and event list/detail polish were delivered.
- Verification: Frontend lint/build and manual event flow checks.

### Task: T-052 - T-054 UTF-8 schema cleanup

- Status: DONE
- Scope: Event, student, employee, and parent schema messages were updated to accented PT-BR.
- Verification: Frontend lint/build.

## Review Notes

- 2026-03-07: Preserved completed frontend baseline work and shifted the active plan toward dashboard correctness, parent inline management, and final UX cleanup.
