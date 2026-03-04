# Epic: E-004 — Frontend Development
**Goal:** Deliver a React UI that covers day-to-day operational workflows for core entities and dashboards.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 3

## Scope
- In scope:
  - Dashboard + navigation
  - Student management UI (list + filters + CRUD flows)
  - Event management UI (form, pickers, dropdowns, pricing/payment display)
  - Shared UI primitives to avoid duplication (delete flow, list patterns)
- Out of scope:
  - Calendar views (E-005)
  - Auth UX (E-003)

## Stories

### Story: S-030 — Dashboard and Navigation
**As a** user **I want** to navigate the application **so that** I can access different features easily.

**Links:** T-030 (DONE) — ST-100

**Acceptance Criteria**
- [ ] AC-030-01 Main navigation links to core entity pages
- [ ] AC-030-02 Dashboard shows the agreed MVP widgets (KPIs + upcoming list, as available)

**Test Plan**
- Backend:
  - [ ] N/A
- Frontend:
  - [ ] Add basic smoke tests for routing where applicable
- Manual:
  - [ ] Verify navigation works on desktop and mobile breakpoints

### Story: S-031 — Student Management UI
**As a** administrator **I want** to manage students through the UI **so that** I don't need to use API directly.

**Links:** T-031 (TODO) — ST-101, ST-102, ST-103, ST-104, ST-105

**Acceptance Criteria**
- [ ] AC-031-01 Student list supports pagination
- [ ] AC-031-02 Search/filter available where backend supports it
- [ ] AC-031-03 Delete/deactivate flows are safe (confirmations, no races)

**Test Plan**
- Backend:
  - [ ] N/A
- Frontend:
  - [ ] Add basic component/page tests where feasible
- Manual:
  - [ ] CRUD students using UI and verify API calls/responses

### Story: S-032 — Event Management UI
**As a** employee **I want** to manage events through the UI **so that** scheduling is intuitive.

**Links:** T-032 (TODO) — ST-106, ST-107, ST-108, ST-109, ST-110, ST-111

**Acceptance Criteria**
- [ ] AC-032-01 Event form supports date/time selection and validation
- [ ] AC-032-02 Student/employee dropdowns work and are performant
- [ ] AC-032-03 Pricing/payment fields are visible and editable

**Test Plan**
- Backend:
  - [ ] N/A
- Frontend:
  - [ ] Add basic component/page tests where feasible
- Manual:
  - [ ] Create/update events and verify list reflects changes

## Tasks

### Task: T-030 — Dashboard and navigation UI
**Type:** frontend
**Status:** DONE
**Depends on:** None

**Description**
- Build routing and primary pages layout for core modules.

**Subtasks**
- [x] ST-100 — Establish navigation shell and routes (legacy: User Story 3.1)

**Files likely affected (best guess)**
- client/

**DoD**
- [x] Implementation completed
- [x] Tests updated/added when applicable
- [x] Local verification done

**Verification**
- Backend: N/A
- Frontend: Run client build/lint/tests as available
- Manual: Navigate between pages on desktop and mobile widths

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 3 / User Story 3.1

### Task: T-031 — Student management UI improvements
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Improve list pages and shared flows; add pagination and filters.

**Subtasks**
- [x] ST-101 — Add delete buttons to Employees, Students and Events list pages (legacy: T-3.2.1)
- [ ] ST-102 — Refactor list-page delete flow into shared hook/component (legacy: T-3.2.2)
- [ ] ST-103 — Add pagination controls to all listing pages (legacy: T-3.2.3)
- [ ] ST-104 — Add search and filter functionality to all listing pages (legacy: T-3.2.4)
- [ ] ST-105 — Add form validation feedback (legacy: T-3.2.6)

**Files likely affected (best guess)**
- client/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: N/A
- Frontend: Verify list pages render and actions do not race
- Manual: Delete flows confirm and refresh list state correctly

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 3 / User Story 3.2

### Task: T-032 — Event management UI
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Implement the event creation/editing flow and supporting UX components.

**Subtasks**
- [ ] ST-106 — Create event form component (legacy: T-3.3.2)
- [ ] ST-107 — Add date/time picker (legacy: T-3.3.3)
- [ ] ST-108 — Add student/employee dropdowns (legacy: T-3.3.4)
- [ ] ST-109 — Display pricing information (legacy: T-3.3.5)
- [ ] ST-110 — Add payment tracking display (legacy: T-3.3.6)
- [ ] ST-111 — Create event calendar view (legacy: T-3.3.1; Deferred/Post-MVP; moved to E-005)

**Files likely affected (best guess)**
- client/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: N/A
- Frontend: Verify event form validation + dropdown loading
- Manual: Create/update event end-to-end and confirm list updates

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 3 / User Story 3.3

**Review Notes (append-only)**
- Reviewer notes:
