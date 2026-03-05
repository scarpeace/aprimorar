# Epic: E-005 — Calendar and Scheduling
**Goal:** Integrate scheduling workflows with Google Calendar and provide calendar-style UI views.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 4

## Scope
- In scope:
  - Google Calendar integration (create/update/delete + conflict handling)
  - Calendar views (month/week/day)
- Out of scope:
  - Payment gateway integration (E-006)

## Workboard
- Current focus: None started
- Blocked: None
- Next up: T-040 (Google Calendar integration)

## Stories
### Story: S-040 — Google Calendar Integration
**Status:** TODO
**Links:** T-040 (TODO)
**Intent:** Sync event lifecycle with Google Calendar reliably.

### Story: S-041 — Calendar Views
**Status:** TODO
**Links:** T-041 (TODO)
**Intent:** Provide visual scheduling views in the frontend.

## Tasks
### Task: T-040 — Google Calendar integration
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Add calendar service and manage lifecycle of external calendar events.

**Subtasks**
- [ ] ST-130 — Set up Google Calendar API credentials
- [ ] ST-131 — Implement calendar service
- [ ] ST-132 — Create events in Google Calendar
- [ ] ST-133 — Update calendar events on change
- [ ] ST-134 — Delete calendar events on cancellation
- [ ] ST-135 — Handle calendar sync conflicts

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: exercise create/update/delete against sandbox calendar
- Frontend: N/A
- Manual: confirm sync behavior and logs in dev

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 4 / User Story 4.1

### Task: T-041 — Calendar views
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Implement calendar-style UI views for events.

**Subtasks**
- [ ] ST-136 — Implement monthly calendar view
- [ ] ST-137 — Implement weekly calendar view
- [ ] ST-138 — Implement daily schedule view
- [ ] ST-139 — Create event calendar view (moved from E-004)

**DoD (Definition of Done)**
- [ ] Implementation completed
- [ ] Tests updated/added when applicable
- [ ] Local verification done

**Verification**
- Backend: N/A
- Frontend: validate view rendering and navigation
- Manual: verify month/week/day views match API data/timezone

**Notes**
- Source: `docs/archive/PLANNING.md` Epic 4 / User Story 4.2

## Archive (DONE)
- No DONE tasks archived yet.

## Review Notes (append-only)
- Reviewer notes:
