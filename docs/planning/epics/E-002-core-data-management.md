# Epic: E-002 — Core Data Management
**Goal:** Provide complete, performant CRUD and management for core entities (Students, Employees, Parents, Events).
**Status:** IN_PROGRESS
**Owner:** Gu
**Related milestone/phase:** Phase 1

## Scope
- In scope:
  - Student search/filter and age validation
  - Event listing performance and core filters
  - Event content classification (`content`) in backend/API
  - Parent rules used by student flows
- Out of scope:
  - Authentication/RBAC (E-003)
  - Calendar integration (E-005)
  - Payment gateway integration (E-006)

## Workboard
- Current focus: T-012-C (backend tests + release safety), T-016 (frontend content wiring), T-017 (manual regression)
- Blocked: S-013 frontend parent inline flow depends on E-004
- Next up: finish T-012-C, execute T-017 smoke checks, then close S-014

## Stories
### Story: S-010 — Student Management Enhancements
**Status:** DONE
**Links:** T-010 (DONE), T-015 (DONE)
**Intent:** Keep student search/filter and age validation stable for day-to-day operations.

### Story: S-011 — Employee Management
**Status:** DONE
**Links:** T-013 (DONE)
**Intent:** Preserve stable employee CRUD baseline.

### Story: S-012 — Event/Class Management Improvements
**Status:** DONE
**Links:** T-011 (DONE)
**Intent:** Keep event listing performant and filterable.

### Story: S-013 — Parent/Guardian Management
**Status:** BLOCKED
**Links:** T-014 (DONE)
**Intent:** Backend is ready; UI inline parent create/edit is pending in E-004.

### Story: S-014 — Event Content Classification
**Status:** IN_PROGRESS
**Links:** T-012-A (DONE), T-012-B (DONE), T-012-C (IN_PROGRESS), T-016 (TODO)
**Intent:** Make `content` required and visible across event create/update/list/detail flows.

## Tasks
### Task: T-012-C — Backend tests + release-safety checklist for event `content`
**Type:** backend
**Status:** IN_PROGRESS
**Depends on:** T-012-B

**Description**
- Finalize backend verification for event `content` behavior.
- Keep release checklist explicit because DB column is NOT NULL.

**Subtasks**
- [x] ST-040 — Release safety checklist added and actionable
- [x] ST-041 — Backend tests for required/invalid content and response presence

**DoD (Definition of Done)**
- [x] Coverage includes missing `content`, invalid enum, list/detail response presence
- [x] Release-safety checklist exists in this epic
- [x] `cd server/api-aprimorar && ./mvnw test` passes

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: N/A
- Manual: Smoke test POST/PATCH/GET list/GET detail for event `content`

**Notes**
- Risks: deploy ordering matters because migration already enforces NOT NULL.
- Open questions: None.

### Task: T-016 — (Frontend) Event `content` dropdown and display
**Type:** frontend
**Status:** TODO
**Depends on:** T-012-B

**Description**
- Add required `content` field to event create/edit UI.
- Show human-friendly labels while sending ASCII enum values to API.
- Render `content` on event list/detail.

**Subtasks**
- [ ] ST-032 — Add required content dropdown and display labels

**DoD (Definition of Done)**
- [ ] Event create/edit includes required `content` dropdown
- [ ] Submit payload includes `content` and succeeds
- [ ] Event list/detail displays `content`

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create/edit event and confirm `content` persists/renders

**Notes**
- Risks: minor UI refactor may be needed depending on current form structure.
- Open questions: dropdown label ordering in UI.

### Task: T-017 — Manual regression verification for core list filters
**Type:** manual/qa
**Status:** TODO
**Depends on:** T-010, T-011, T-015

**Description**
- Run a lightweight manual regression for core list filters/pagination and key validations.

**Subtasks**
- [ ] ST-042 — Verify student list `name`/`activity` filters with pagination
- [ ] ST-043 — Verify event filters (`start`, `end`, `studentId`, `employeeId`) with pagination/sorting
- [ ] ST-044 — Verify out-of-range student birthdate returns stable 400 message
- [ ] ST-045 — Smoke test employee create/update/deactivate flows

**DoD (Definition of Done)**
- [ ] Manual checks completed and regressions captured as follow-up tasks

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: Use Swagger/UI in dev with enough data to exercise paging/filtering

**Notes**
- Risks: false confidence if tested with too little seed data.
- Open questions: None.

## Archive (DONE)
### Task: T-010 — Student listing/search and validation
- Status: DONE
- Scope: Added student filters by name/activity and stabilized list endpoints.
- Verification: backend tests + manual API checks.
- Commit: not recorded in this epic history.

### Task: T-011 — Event list performance and filters
- Status: DONE
- Scope: Added composable event filters (`start`, `end`, `studentId`, `employeeId`) and query/perf improvements.
- Verification: backend tests and endpoint smoke checks.
- Commit: not recorded in this epic history.

### Task: T-012 — Add required `content` to events (parent)
- Status: DONE
- Scope: Parent pointer task, completed via T-012-A/B/C slices.
- Verification: delegated to slice tasks.
- Commit: mixed across slice commits.

### Task: T-012-A — Backend model/DTO wiring for event `content`
- Status: DONE
- Scope: Added `EventContent` enum and required create/update validation.
- Verification: backend tests for missing/invalid content.
- Commit: not recorded in this epic history.

### Task: T-012-B — Response contract wiring for event `content`
- Status: DONE
- Scope: Included `content` in event response mapping and mapper tests.
- Verification: mapper/controller test coverage.
- Commit: not recorded in this epic history.

### Task: T-013 — Employee management is complete and stable
- Status: DONE
- Scope: Preserved legacy completion state for employee management.
- Verification: regression covered by T-017 smoke task.
- Commit: legacy/mixed.

### Task: T-014 — Parent management is complete and stable
- Status: DONE
- Scope: Preserved backend completion; frontend inline flow moved to E-004.
- Verification: final UX verification pending E-004.
- Commit: legacy/mixed.

### Task: T-015 — Student age validation (min/max)
- Status: DONE
- Scope: Enforced age range validation and boundary coverage.
- Verification: backend tests + API validation behavior.
- Commit: `c2fd221` (planning status close); implementation commit mixed.

## Review Notes (append-only)
- 2026-03-05:
  - Quality: Epic cleaned to active-vs-archive format to reduce planning drift and status mismatches.
  - Security: Keep API validation errors stable and non-leaky as fields evolve.
  - Performance: Re-check list filter query plans after any future index or filter changes.
