# Epic: E-004 — Frontend Development
**Goal:** Deliver a React UI for core day-to-day operational workflows.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 3

## Scope
- In scope:
  - Dashboard + navigation
  - Student management UI (list + filters + CRUD)
  - Event management UI (form + dropdowns + pricing/payment)
  - Shared UI primitives for common flows
- Out of scope:
  - Calendar views (E-005)
  - Auth UX (E-003)

## Workboard
- Current focus: None started
- Blocked: Parent inline create/edit dependency from E-002 (S-013) lands here
- Next up: T-031 student UI improvements, then T-032 event UI

## Stories
### Story: S-030 — Dashboard and Navigation
**Status:** DONE
**Links:** T-030 (DONE)
**Intent:** Provide base navigation between core modules.

### Story: S-031 — Student Management UI
**Status:** TODO
**Links:** T-031 (TODO)
**Intent:** Manage students from UI with pagination/filter/delete safety.

### Story: S-032 — Event Management UI
**Status:** TODO
**Links:** T-032 (TODO)
**Intent:** Support event create/update flows with usable form controls.

## Tasks
### Task: T-031 — Student management UI improvements
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Improve list pages and shared flows; add pagination and filters.

**Subtasks**
- [x] ST-101 — Add delete buttons to Employees, Students and Events list pages
- [ ] ST-102 — Refactor list-page delete flow into shared hook/component
- [ ] ST-103 — Add pagination controls to all listing pages
- [ ] ST-104 — Add search/filter functionality to all listing pages
- [ ] ST-105 — Add form validation feedback

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: N/A
- Frontend: verify list pages render and actions do not race
- Manual: delete flows confirm and refresh list state correctly

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 3 / User Story 3.2

### Task: T-032 — Event management UI
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Implement event creation/editing flow and supporting UX components.

**Subtasks**
- [ ] ST-106 — Create event form component
- [ ] ST-107 — Add date/time picker
- [ ] ST-108 — Add student/employee dropdowns
- [ ] ST-109 — Display pricing information
- [ ] ST-110 — Add payment tracking display
- [ ] ST-111 — Create event calendar view (Deferred/Post-MVP; moved to E-005)

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: N/A
- Frontend: verify event form validation + dropdown loading
- Manual: create/update event and confirm list updates

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 3 / User Story 3.3

## Archive (DONE)
### Task: T-030 — Dashboard and navigation UI
- Status: DONE
- Scope: Established routing shell and main navigation pages.
- Verification: frontend build/lint/manual navigation checks.
- Commit: not recorded in this epic history.

## Review Notes (append-only)
- Reviewer notes:
