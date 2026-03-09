# Epic: E-004 — Frontend Development
**Goal:** Deliver a React UI for core day-to-day operational workflows.
**Status:** IN_PROGRESS
**Owner:** Gu
**Related milestone/phase:** Phase 3

## Scope
- In scope:
  - Dashboard and navigation
  - Student, employee, and event management UI
  - Shared UI primitives for common flows
  - UTF-8/accents polish for user-facing strings
- Out of scope:
  - Calendar views (E-005)
  - Auth UX (E-003)

## Workboard
- Current focus: T-031 student list UX baseline
- Blocked: Inline parent create/select depends on backend-supported flow design staying stable
- Next up: T-032 event management UI, then T-056 retry behavior cleanup

## Stories
### Story: S-030 — Dashboard and navigation
**Status:** DONE
**Links:** T-030 (DONE)
**Intent:** Provide a base shell for core modules.

### Story: S-031 — Student management UI
**Status:** IN_PROGRESS
**Links:** T-031 (IN_PROGRESS)
**Intent:** Manage students with solid list, form, and archive UX.

### Story: S-032 — Event management UI
**Status:** TODO
**Links:** T-032 (TODO)
**Intent:** Support event create/update flows with usable validation and state handling.

### Story: S-033 — UTF-8 validation and error messages
**Status:** DONE
**Links:** T-052 (DONE), T-053 (DONE), T-054 (DONE), T-055 (DONE)
**Intent:** Keep user-facing Portuguese strings correctly accented and readable.

## Tasks
### Task: T-031 — Student list UX baseline
**Type:** frontend
**Status:** IN_PROGRESS
**Depends on:** None

**Description**
- Improve student list flows: archive safety, pagination, filters, and visible validation feedback.

**DoD**
- [ ] Student list supports pagination and basic filters without regressions
- [ ] Archive flow uses a consistent confirm + refresh pattern
- [ ] Validation errors are visible and actionable

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: exercise list + create/edit/archive flows and confirm errors render correctly

**Notes**
- Risks: inconsistent error rendering across forms if patterns diverge.

### Task: T-032 — Event management UI
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Implement event create/edit flow and supporting UX components.

**DoD**
- [ ] Event create/edit form is usable
- [ ] Validation feedback is clear
- [ ] Event list/detail refresh stays in sync after saves

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: create/update an event and verify validation, saving, and list refresh

### Task: T-056 — Replace full-page retry reloads
**Type:** frontend
**Status:** TODO
**Depends on:** None

**Description**
- Replace `window.location.reload()` retry behavior with local refetch callbacks.

**DoD**
- [ ] Retry actions refetch local data without full page reload
- [ ] Shared pattern reused where practical
- [ ] Frontend verification passes

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: trigger an API error and confirm retry refetches in place

## Archive (DONE)
### Task: T-030 — Dashboard and navigation UI
- Status: DONE
- Scope: Established the routing shell and base navigation.
- Verification: `cd client && npm run lint && npm run build` plus manual navigation checks.

### Task: T-052 / T-053 / T-054 / T-055 — UTF-8 and pt-BR message polish
- Status: DONE
- Scope: Updated schemas and core UI states to use proper Portuguese accents and UTF-8 strings.
- Verification: `cd client && npm run lint && npm run build`.

## Review Notes (append-only)
- 2026-03-08:
  - Quality: Epic is active; story/task statuses now reflect completed UTF-8 work and remaining UI tasks.
  - Security: Auth UX remains intentionally out of scope until E-003 starts.
  - Performance: Prefer local refetch patterns over full-page reloads.
