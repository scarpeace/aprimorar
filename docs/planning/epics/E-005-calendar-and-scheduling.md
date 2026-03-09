# Epic: E-005 — Calendar and Scheduling
**Goal:** Integrate scheduling workflows with Google Calendar and provide calendar-style UI views.
**Status:** TODO
**Owner:** Gu
**Related milestone/phase:** Phase 4

## Scope
- In scope:
  - Google Calendar integration for event lifecycle sync
  - Calendar views: month, week, and day
- Out of scope:
  - Payment gateway integration (E-006)

## Workboard
- Current focus: None
- Blocked: None
- Next up: T-040 Google Calendar integration

## Stories
### Story: S-040 — Google Calendar integration
**Status:** TODO
**Links:** T-040 (TODO)
**Intent:** Sync event lifecycle with Google Calendar reliably.

### Story: S-041 — Calendar views
**Status:** TODO
**Links:** T-041 (TODO)
**Intent:** Provide visual scheduling views in the frontend.

## Tasks
### Task: T-040 — Google Calendar integration
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Add a calendar service and manage external event lifecycle.

**DoD**
- [ ] Sandbox calendar integration works for create/update/delete
- [ ] Sync conflict handling is defined
- [ ] Local verification done

**Verification**
- Backend: exercise create/update/delete against a sandbox calendar
- Frontend: N/A
- Manual: confirm sync behavior and logs in dev

### Task: T-041 — Calendar views
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Implement month/week/day calendar views backed by event data.

**DoD**
- [ ] Month/week/day views render usable schedule data
- [ ] Navigation and timezone behavior are validated
- [ ] Local verification done

**Verification**
- Backend: N/A
- Frontend: validate view rendering and navigation
- Manual: verify month/week/day views match API data and timezone behavior

## Archive (DONE)
- None.

## Review Notes (append-only)
- 2026-03-08:
  - Quality: Keep this epic lightweight until API/auth decisions stabilize.
  - Security: Calendar credentials and sync logs need careful handling when implementation starts.
  - Performance: Calendar views should rely on paged/ranged queries rather than client-side overfetch.
