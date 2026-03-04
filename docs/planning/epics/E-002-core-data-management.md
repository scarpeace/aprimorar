# Epic: E-002 — Core Data Management
**Goal:** Provide complete, performant CRUD and management for core entities (Students, Employees, Parents, Events).
**Status:** IN_PROGRESS
**Owner:** Gu
**Related milestone/phase:** Phase 1

## Scope
- In scope:
  - Student management enhancements (search/filter, validation, import/export)
  - Event listing performance and filters
  - Parent management rules used by Student flows
  - Event session type classification (backend + API + migrations)
- Out of scope:
  - Authentication/RBAC (E-003)
  - Calendar integration (E-005)
  - Payment gateway integration (E-006)

## Stories

### Story: S-010 — Student Management Enhancements
**As a** administrator **I want** improved student search and validation **so that** I can manage student records efficiently at scale.

**Status:** IN_PROGRESS
**Links:** T-010 (DONE), T-015 (TODO)

**Acceptance Criteria**
- [x] AC-010-01 Student list supports searching/filtering by name
- [x] AC-010-02 Student list supports filtering by activity type
- [ ] AC-010-03 Student age is validated on create/update using a defined min/max range (ST-023)
- [ ] AC-010-04 Validation errors return 400 with a clear, stable message indicating the allowed range
- [ ] AC-010-05 Boundary cases are covered by automated tests (exact min/max, just outside min/max)

**Test Plan**
- Backend:
  - [ ] Add DTO/service tests for student age boundaries (min/max) and error messages
- Frontend:
  - [x] N/A (backend capability)
- Manual:
  - [ ] Verify student list filters work with pagination
  - [ ] Try creating/updating a student with an out-of-range birthdate and confirm a 400 response

**Review Notes (append-only)**
- 2026-03-04
  - Quality: Story is partially complete; search/filter is done via T-010, but age validation + boundary tests are still outstanding via T-015.
  - Security: Ensure validation error messages are stable and do not leak internal details; enforce server-side regardless of client inputs.
  - Performance: Student name search should remain index-friendly (avoid leading-wildcard patterns where possible); confirm pagination + filters compose without full table scans.

### Story: S-011 — Employee Management
**As a** administrator **I want** employee records managed **so that** I can track teachers and staff.

**Links:** T-013 (DONE) — ST-033

**Acceptance Criteria**
- [ ] AC-011-01 Employee CRUD works and remains stable

**Test Plan**
- Backend:
  - [ ] N/A (existing behavior)
- Frontend:
  - [ ] N/A (backend capability)
- Manual:
  - [ ] Smoke test employee create/update/deactivate

### Story: S-012 — Event/Class Management Improvements
**As a** employee **I want** event listing to be performant and filterable **so that** scheduling and follow-ups are efficient.

**Links:** T-011 (TODO) — ST-024, ST-025, ST-026, ST-027

**Acceptance Criteria**
- [ ] AC-012-01 Event list avoids N+1 query patterns
- [ ] AC-012-02 Event list supports filtering by date range
- [ ] AC-012-03 Event list supports filtering by student
- [ ] AC-012-04 Event list supports filtering by employee

**Test Plan**
- Backend:
  - [ ] Add repository/service tests for query behavior and filters
- Frontend:
  - [ ] N/A (backend capability)
- Manual:
  - [ ] Verify filters compose correctly with pagination and sorting

### Story: S-013 — Parent/Guardian Management
**As a** administrator **I want** parent records available in student create/edit **so that** I can keep guardian contact data up to date.

**Links:** T-014 (DONE) — ST-034

**Acceptance Criteria**
- [ ] AC-013-01 Parents can be created inline during student create/edit
- [ ] AC-013-02 Existing parent can be assigned to a student via dropdown
- [ ] AC-013-03 Parent dropdown lists only active parents (id + name)
- [ ] AC-013-04 Parents are soft-deleted

**Test Plan**
- Backend:
  - [ ] N/A (depends on UI wiring)
- Frontend:
  - [ ] Validate parent dropdown UX for create/edit
- Manual:
  - [ ] Create student with new parent, then reassign to existing parent

### Story: S-014 — Event Session Types
**As a** employee **I want** events classified by session type **so that** scheduling and reporting are consistent across services.

**Links:** T-012 (TODO) — ST-028, ST-029, ST-030, ST-031, ST-032

**Acceptance Criteria**
- [ ] AC-014-01 `sessionType` is required for events and validated
- [ ] AC-014-02 Database stores `sessionType` and existing data is migrated safely
- [ ] AC-014-03 GET `/v1/events` supports filtering by `sessionType`

**Test Plan**
- Backend:
  - [ ] Add DTO validation tests for required `sessionType`
- Frontend:
  - [ ] Update event create/edit form to require and display `sessionType`
- Manual:
  - [ ] Create event with each session type and filter by it

## Tasks

### Task: T-010 — Student listing/search and validation
**Type:** backend
**Status:** DONE
**Depends on:** None

**Description**
- Add student list filters/search (name + activity).

**Subtasks**
- [x] ST-020 — Add student search/filter by name (legacy: T-1.1.2)
- [x] ST-021 — Add student search/filter by activity type (legacy: T-1.1.3)
- [ ] ST-022 — Add bulk import/export (CSV) (legacy: T-1.1.4) (BLOCKED - de-scoped)
- [ ] ST-023 — Student age validation (min/max) (legacy: T-1.1.5) (moved to T-015)

**Files likely affected (best guess)**
- server/

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: cd server/api-aprimorar && ./mvnw test
- Frontend: N/A
- Manual: Call GET `/v1/students` and GET `/v1/students/active` with `name` and `activity` query params and confirm results

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.1
- Added `name` and `activity` query params to GET `/v1/students` and `/v1/students/active`.
- ST-022 (CSV import/export) de-scoped per request on 2026-03-04.
- Risks: None
- Open questions: None

### Task: T-015 — Student age validation (min/max) (ST-023)
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Define the allowed age range (min/max) and enforce it for student create and update flows.
- Reject out-of-range birthdates with a 400 validation error and a clear, stable message.
- Cover boundary conditions with automated tests (exact min, exact max, below min, above max).
- Keep the implementation minimal: server-side validation only (no UI work) and no new persistence.

**Subtasks**
- [ ] ST-035 — Confirm min/max age requirements and document as constants (MVP: code constants)
- [ ] ST-036 — Implement server-side validation for create student (400 + message)
- [ ] ST-037 — Implement server-side validation for update student (400 + message)
- [ ] ST-038 — Add unit tests for min/max boundary conditions and error message
- [ ] ST-039 — If tests/seed data fails: fix any out-of-range fixtures (keep changes minimal)

**Files likely affected (best guess)**
- server/api-aprimorar/src/main/java/com/aprimorar/api/validation/StudentAgeRules.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/controller/dto/CreateStudentDTO.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/controller/dto/UpdateStudentDTO.java
- server/api-aprimorar/src/test/java/com/aprimorar/api/controller/dto/CreateStudentDTOTest.java

**DoD (Definition of Done)**
- [ ] Min/max age bounds are defined (constants or configuration) and referenced by validation logic
- [ ] POST `/v1/students` rejects out-of-range ages with 400
- [ ] PATCH `/v1/students/{id}` rejects out-of-range ages with 400
- [ ] Unit tests cover boundary cases and pass locally

**Verification**
- Backend: cd server/api-aprimorar && ./mvnw test
- Frontend: N/A
- Manual: POST/PATCH a student with an out-of-range birthdate and confirm a 400 with the expected message

**Notes**
- Risks: Existing seed/test data may become invalid once validation is enforced
- Open questions: What exact min/max ages should be enforced, and should the age be computed as-of "today" (request time) or as-of the first scheduled event?

#### Complexity Explanation
- What: compute age from `birthDate` and validate it against min/max bounds.
- Why: prevents invalid student records and avoids downstream scheduling/reporting edge cases.
- Simplest approach: define 2 constants (MIN_AGE, MAX_AGE), validate on DTOs for create/update, and write deterministic tests for boundary dates.

### Task: T-011 — Event list performance and filters
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Fix N+1 and add filters for common operational queries.

**Subtasks**
- [ ] ST-024 — Fix N+1 query problem (legacy: T-1.3.3)
- [ ] ST-025 — Add event filtering by date range (legacy: T-1.3.4)
- [ ] ST-026 — Add event filtering by student (legacy: T-1.3.5)
- [ ] ST-027 — Add event filtering by employee (legacy: T-1.3.6)

**Files likely affected (best guess)**
- server/

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: cd server/api-aprimorar && ./mvnw test
- Frontend: N/A
- Manual: Exercise filters with pagination/sorting in a dev environment

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.3
- Risks: Performance regressions if new filters prevent index usage
- Open questions: None

### Task: T-012 — Add `sessionType` to events
**Type:** fullstack
**Status:** TODO
**Depends on:** None

**Description**
- Persist and validate event `sessionType`; expose it via API; add filtering; update UI fields when available.

**Subtasks**
- [ ] ST-028 — Add `sessionType` enum values (legacy: T-1.5.1)
- [ ] ST-029 — Add `sessionType` to Event entity/DTOs and validate required (legacy: T-1.5.2)
- [ ] ST-030 — Add DB migration for `session_type` on events (legacy: T-1.5.3)
- [ ] ST-031 — Add `sessionType` filtering on GET `/v1/events` (legacy: T-1.5.4)
- [ ] ST-032 — (Frontend) Add session type dropdown + filters (legacy: T-1.5.5)

**Files likely affected (best guess)**
- server/
- client/

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: cd server/api-aprimorar && ./mvnw test
- Frontend: cd client && npm run lint && npm run build
- Manual: Create events with each `sessionType`, then filter GET `/v1/events` by `sessionType`

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.5
- Risks: Backfill/migration strategy for existing events may be non-trivial if `sessionType` becomes required
- Open questions: Should existing events get a default `sessionType` during migration, or should they remain nullable until edited?

### Task: T-013 — Employee management is complete and stable
**Type:** backend
**Status:** DONE
**Depends on:** None

**Description**
- Preserve legacy status: Employee management tasks were marked complete.

**Subtasks**
- [x] ST-033 — Legacy plan indicates "All tasks completed" for employee management

**Files likely affected (best guess)**
- server/

**DoD (Definition of Done)**
- [x] Implementation completed
- [x] Local verification done

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: N/A (legacy status preserved)

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.2
- Risks: None
- Open questions: None

### Task: T-014 — Parent management is complete and stable
**Type:** backend
**Status:** DONE
**Depends on:** None

**Description**
- Preserve legacy status: Parent management tasks were marked complete.

**Subtasks**
- [x] ST-034 — Legacy plan indicates "All tasks completed" for parent management

**Files likely affected (best guess)**
- server/

**DoD (Definition of Done)**
- [x] Implementation completed
- [x] Local verification done

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: N/A (legacy status preserved)

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.4
- Risks: None
- Open questions: None

**Review Notes (append-only)**
- Reviewer notes:
  - 2026-03-04
    - Quality: Consider adding a `**Status:**` line to each Story for faster triage and to avoid Story/Task completion mismatches.
    - Security: For validation-related tasks, standardize error payload shape and message stability for clients.
    - Performance: For list/filter stories, add an explicit check for pagination+filters query plans (indexes) as part of DoD.
