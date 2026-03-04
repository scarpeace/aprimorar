# Epic: E-002 — Core Data Management
**Goal:** Provide complete, performant CRUD and management for core entities (Students, Employees, Parents, Events).
**Status:** TODO
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

**Acceptance Criteria**
- [ ] Student list supports searching/filtering by name
- [ ] Student list supports filtering by activity type
- [ ] Student age validation rules are enforced (min/max defined)

**Test Plan**
- Backend:
  - [ ] Add service/controller tests for filters and validation
- Frontend:
  - [ ] N/A (backend capability)
- Manual:
  - [ ] Verify student list filters work with pagination

### Story: S-011 — Employee Management
**As a** administrator **I want** employee records managed **so that** I can track teachers and staff.

**Acceptance Criteria**
- [ ] Employee CRUD works and remains stable

**Test Plan**
- Backend:
  - [ ] N/A (existing behavior)
- Frontend:
  - [ ] N/A (backend capability)
- Manual:
  - [ ] Smoke test employee create/update/deactivate

### Story: S-012 — Event/Class Management Improvements
**As a** employee **I want** event listing to be performant and filterable **so that** scheduling and follow-ups are efficient.

**Acceptance Criteria**
- [ ] Event list avoids N+1 query patterns
- [ ] Event list supports filtering by date range
- [ ] Event list supports filtering by student
- [ ] Event list supports filtering by employee

**Test Plan**
- Backend:
  - [ ] Add repository/service tests for query behavior and filters
- Frontend:
  - [ ] N/A (backend capability)
- Manual:
  - [ ] Verify filters compose correctly with pagination and sorting

### Story: S-013 — Parent/Guardian Management
**As a** administrator **I want** parent records available in student create/edit **so that** I can keep guardian contact data up to date.

**Acceptance Criteria**
- [ ] Parents can be created inline during student create/edit
- [ ] Existing parent can be assigned to a student via dropdown
- [ ] Parent dropdown lists only active parents (id + name)
- [ ] Parents are soft-deleted

**Test Plan**
- Backend:
  - [ ] N/A (depends on UI wiring)
- Frontend:
  - [ ] Validate parent dropdown UX for create/edit
- Manual:
  - [ ] Create student with new parent, then reassign to existing parent

### Story: S-014 — Event Session Types
**As a** employee **I want** events classified by session type **so that** scheduling and reporting are consistent across services.

**Acceptance Criteria**
- [ ] `sessionType` is required for events and validated
- [ ] Database stores `sessionType` and existing data is migrated safely
- [ ] GET `/v1/events` supports filtering by `sessionType`

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
**Status:** TODO
**Depends on:** None

**Description**
- Add filters/search and CSV import/export; define and enforce age validation.

**Subtasks**
- [ ] ST-020 — Add student search/filter by name (legacy: T-1.1.2)
- [ ] ST-021 — Add student search/filter by activity type (legacy: T-1.1.3)
- [ ] ST-022 — Add bulk import/export (CSV) (legacy: T-1.1.4)
- [ ] ST-023 — Student age validation (min/max) (legacy: T-1.1.5)

**Files likely affected (best guess)**
- server/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Add/adjust tests for filters/validation and run them
- Frontend: N/A
- Manual: Verify search/filter and CSV flows in a dev environment

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.1

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

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Add tests for query behavior; validate no N+1 in list endpoints
- Frontend: N/A
- Manual: Exercise filters with pagination/sorting

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.3

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

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Create and filter events by `sessionType` via API
- Frontend: Ensure dropdown exists and persists `sessionType`
- Manual: Create events with each `sessionType` end-to-end

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.5

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

**DoD**
- [x] Implementation completed
- [x] Local verification done

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: N/A (legacy status preserved)

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.2

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

**DoD**
- [x] Implementation completed
- [x] Local verification done

**Verification**
- Backend: N/A
- Frontend: N/A
- Manual: N/A (legacy status preserved)

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 1 / User Story 1.4

**Review Notes (append-only)**
- Reviewer notes:
