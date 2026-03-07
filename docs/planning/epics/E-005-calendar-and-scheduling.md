# Epic: E-005 - Calendar and Scheduling Expansion

**Goal:** Expand scheduling beyond basic CRUD with external calendar sync and dedicated calendar views.
**Status:** TODO
**Phase:** Phase 4

## Scope

- In scope:
  - Google Calendar sync foundation
  - sync lifecycle and conflict handling
  - calendar-oriented frontend views
- Out of scope:
  - core dashboard work
  - payment gateway work

## Workboard

- Current focus: none
- Blocked: none
- Next up: start with external sync before calendar UI

## Stories

### Story: S-040 - External Calendar Sync

**Status:** TODO
**Intent:** Keep event lifecycle capable of syncing with Google Calendar.
**Links:** T-040, T-041, T-042

**Acceptance Criteria**

- Event create/update/delete can sync to a sandbox calendar.
- Sync failures are visible and recoverable.

### Story: S-041 - Calendar Views

**Status:** TODO
**Intent:** Add visual scheduling views after sync foundations are understood.
**Links:** T-043, T-044

**Acceptance Criteria**

- Users can navigate month and week views without breaking core event workflows.
- Timezone behavior is consistent with backend event data.

## Tasks

### Task: T-040 - Add Google Calendar integration skeleton

**Type:** backend
**Status:** TODO
**Depends on:** None

**Outcome**

- Add credentials/config wiring and a minimal calendar service abstraction.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Load config and initialize the calendar integration in dev

### Task: T-041 - Sync event create/update/delete to sandbox calendar

**Type:** backend
**Status:** TODO
**Depends on:** T-040

**Outcome**

- Sync the basic event lifecycle to Google Calendar for a dev or sandbox setup.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Create, update, and delete an event and confirm external sync behavior

### Task: T-042 - Handle sync errors and conflict recovery

**Type:** backend
**Status:** TODO
**Depends on:** T-041

**Outcome**

- Keep failures explicit and avoid silently drifting event state.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: `./mvnw test`
- Frontend: N/A
- Manual: Simulate a failed sync and verify logs or recovery behavior

### Task: T-043 - Build month/week calendar views

**Type:** frontend
**Status:** TODO
**Depends on:** None

**Outcome**

- Add calendar views for browsing events without replacing the simpler list view.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Navigate month/week views with real event data

### Task: T-044 - Add calendar navigation and event detail handoff

**Type:** frontend
**Status:** TODO
**Depends on:** T-043

**Outcome**

- Make calendar views practical by linking clearly into event detail/edit flows.

**Definition of Done**

- [ ] Code or docs updated
- [ ] Verification completed
- [ ] Relevant epic notes updated

**Verification**

- Backend: N/A
- Frontend: `npm run lint && npm run build`
- Manual: Open an event from the calendar and move into detail or edit flows

## Archive

- No completed calendar tasks yet.

## Review Notes

- 2026-03-07: Split calendar sync from calendar UI so the integration can land independently of the visual scheduler.
