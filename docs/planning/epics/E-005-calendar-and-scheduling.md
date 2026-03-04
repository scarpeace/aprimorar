# Epic: E-005 — Calendar and Scheduling
**Goal:** Integrate scheduling workflows with Google Calendar and provide calendar-style views in the UI.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 4

## Scope
- In scope:
  - Google Calendar integration (create/update/delete events, sync conflict handling)
  - Calendar views (month/week/day) in the UI
- Out of scope:
  - Payment gateway integration (E-006)

## Stories

### Story: S-040 — Google Calendar Integration
**As a** employee **I want** events synced with Google Calendar **so that** I can manage schedules externally.

**Acceptance Criteria**
- [ ] Credentials are configured securely for dev/prod
- [ ] Events are created/updated/deleted in Google Calendar
- [ ] Sync conflicts are handled deterministically

**Test Plan**
- Backend:
  - [ ] Unit tests for calendar service logic
- Frontend:
  - [ ] N/A
- Manual:
  - [ ] Verify a real Google Calendar event is created and updated

### Story: S-041 — Calendar Views
**As a** user **I want** to view events in calendar format **so that** scheduling is visual.

**Acceptance Criteria**
- [ ] Monthly calendar view exists
- [ ] Weekly calendar view exists
- [ ] Daily schedule view exists

**Test Plan**
- Backend:
  - [ ] N/A
- Frontend:
  - [ ] Add basic view tests where feasible
- Manual:
  - [ ] Verify timezone handling and display correctness

## Tasks

### Task: T-040 — Google Calendar integration
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Add a calendar service and manage lifecycle of external calendar events.

**Subtasks**
- [ ] ST-130 — Set up Google Calendar API credentials (legacy: T-4.1.1)
- [ ] ST-131 — Implement calendar service (legacy: T-4.1.2)
- [ ] ST-132 — Create events in Google Calendar (legacy: T-4.1.3)
- [ ] ST-133 — Update calendar events on change (legacy: T-4.1.4)
- [ ] ST-134 — Delete calendar events on cancellation (legacy: T-4.1.5)
- [ ] ST-135 — Handle calendar sync conflicts (legacy: T-4.1.6)

**Files likely affected (best guess)**
- server/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: Exercise create/update/delete against a sandbox calendar
- Frontend: N/A
- Manual: Confirm sync behavior and logs in a dev environment

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 4 / User Story 4.1

### Task: T-041 — Calendar views
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Implement calendar-style UI views for events.

**Subtasks**
- [ ] ST-136 — Implement monthly calendar view (legacy: T-4.2.1)
- [ ] ST-137 — Implement weekly calendar view (legacy: T-4.2.2)
- [ ] ST-138 — Implement daily schedule view (legacy: T-4.2.3)
- [ ] ST-139 — Create event calendar view (legacy: T-3.3.1; moved from Epic 3)

**Files likely affected (best guess)**
- client/

**DoD**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: N/A
- Frontend: Validate view rendering and navigation
- Manual: Verify month/week/day views match API data and timezone

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 4 / User Story 4.2

**Review Notes (append-only)**
- Reviewer notes:
