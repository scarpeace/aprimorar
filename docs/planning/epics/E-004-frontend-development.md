# Epic: E-004 — Frontend Development
**Goal:** Deliver a React UI for core day-to-day operational workflows.
**Status:** IN_PROGRESS
**Owner:** Gu
**Related milestone/phase:** Phase 3

## Scope
- In scope:
  - Dashboard + navigation
  - Student management UI (list + filters + CRUD)
  - Event management UI (form + dropdowns + pricing/payment)
  - Shared UI primitives for common flows
  - UX polish for user-facing validation/error messages (UTF-8 accents)
- Out of scope:
  - Calendar views (E-005)
  - Auth UX (E-003)

## Workboard
- Current focus: T-055 UTF-8 sweep on core UI strings
- Blocked: Parent inline create/edit for student flow (backend ready in E-002 / S-013)
- Next up: Story closeout and manual QA pass across students/events/employees

## Stories
### Story: S-030 — Dashboard and Navigation
**Status:** DONE
**Links:** T-030 (DONE)
**Intent:** Provide base navigation between core modules.

**Acceptance Criteria**
- User can navigate between dashboard, students, employees, and events modules.
- Base layout renders on mobile and desktop without broken routing.

**Test Plan**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Click through primary nav links and refresh on each route

### Story: S-031 — Student Management UI
**Status:** DONE
**Links:** T-031 (DONE)
**Intent:** Manage students from UI with pagination/filter/delete safety.

**Acceptance Criteria**
- Student list supports pagination and basic search/filter.
- Delete flow is consistent and guarded (confirm + refresh list state).
- Form validation feedback is shown in the UI for create/edit.

**Test Plan**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create/edit/delete a student and verify list updates and errors render

### Story: S-032 — Event Management UI
**Status:** DONE
**Links:** T-032 (DONE)
**Intent:** Support event create/update flows with usable form controls.

**Acceptance Criteria**
- Event create/edit form validates required fields and date ordering.
- Student/employee selection works and is persisted on save.
- Pricing/payment inputs show validation feedback and do not allow invalid values.

**Test Plan**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create/update an event and verify list/detail reflect changes

### Story: S-033 — UTF-8 Validation and Error Messages
**Status:** IN_PROGRESS
**Links:** T-052 (DONE), T-053 (DONE), T-054 (DONE), T-055 (TODO)
**Intent:** Ensure user-facing validation and error messages use correct Portuguese accents and UTF-8 characters.

**Acceptance Criteria**
- Zod validation messages and enum labels render with proper accents (e.g., "Título é obrigatório", "ID inválido", "Pagamento não pode ser maior que o preço", "Orientação vocacional").
- Core screens no longer display missing-accent Portuguese in validation and error states.
- Frontend build/lint passes with UTF-8 source files.

**Test Plan**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Trigger validation errors on student/event forms and confirm accent rendering

## Tasks
### Task: T-031 — Student list UX baseline (delete/pagination/search/validation feedback)
**Type:** frontend
**Status:** DONE
**Depends on:** None

**Description**
- Improve student list and shared list-page flows: delete safety, pagination, search/filter, and visible validation feedback.
- Keep legacy subtasks/IDs for tracking: ST-101, ST-102, ST-103, ST-104, ST-105 (ST-101 DONE).

**Files likely affected (best guess)**
- client/src/features/students/StudentsPage.tsx
- client/src/features/students/
- client/src/components/
- client/src/lib/

**DoD (Definition of Done)**
- [x] Student list supports pagination and basic search/filter without UI regressions
- [x] Delete flow uses a consistent confirm + refresh pattern (shared where practical)
- [x] Form validation errors are visible and actionable

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Exercise list + create/edit flows and confirm errors and delete refresh behavior

**Notes**
- Risks: inconsistent error rendering across forms if validation mapping differs per page.
- Open questions: final UX for filters (free-text only vs structured filters).

**Implementation Notes**
- Added list pagination and free-text name filter to `client/src/features/students/StudentsPage.tsx`.
- Added stale-request protection for list refresh and extended `studentsApi.list` filter params in `client/src/services/api.ts`.
- Added student edit route/page (`/students/:id/edit`) and detail-page shortcut in `client/src/App.tsx`, `client/src/features/students/StudentEditPage.tsx`, and `client/src/features/students/StudentDetailPage.tsx`.
- Refactored shared student create/edit form logic into `client/src/features/students/StudentForm.tsx` and `client/src/features/students/studentFormUtils.ts`; also polished the student detail view copy/layout.

### Task: T-032 — Event management UI
**Type:** frontend
**Status:** DONE
**Depends on:** None

**Description**
- Implement event creation/editing flow and supporting UX components.
- Keep legacy subtasks/IDs for tracking: ST-106, ST-107, ST-108, ST-109, ST-110, ST-111.

**Files likely affected (best guess)**
- client/src/features/events/
- client/src/lib/schemas/event.ts
- client/src/components/

**DoD (Definition of Done)**
- [x] Event create/edit form is usable (date/time, student/employee selects)
- [x] Validation feedback is visible for required fields and refine rules
- [x] Event list reflects create/update results without stale state

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create/update an event and verify validation, saving, and list refresh

**Notes**
- Risks: date/time picker behavior can be inconsistent across locales; keep it simple.
- Open questions: whether to add a date/time picker library vs native inputs.

**Implementation Notes**
- Extracted a shared event form component in `client/src/features/events/EventForm.tsx` and shared mapping helpers in `client/src/features/events/eventFormUtils.ts`.
- Refactored `client/src/features/events/EventCreatePage.tsx` into a thin create wrapper and added `client/src/features/events/EventEditPage.tsx` for updates.
- Added the `/events/:id/edit` route in `client/src/App.tsx` and edit shortcuts on `client/src/features/events/EventsPage.tsx` and `client/src/features/events/EventDetailPage.tsx`.
- Polished event list/detail copy to accented pt-BR and improved list empty-state navigation.

### Task: T-052 — UTF-8: update event schema labels and validation messages
**Type:** frontend
**Status:** DONE
**Depends on:** None

**Description**
- Update `client` event Zod schema messages and UI-facing labels to use proper Portuguese accents (UTF-8).
- Target: `client/src/lib/schemas/event.ts` (e.g., "Título é obrigatório", "Descrição deve ter no máximo 500 caracteres", "Pagamento não pode ser maior que o preço", "Fim deve ser depois do início").

**Files likely affected (best guess)**
- client/src/lib/schemas/event.ts

**DoD (Definition of Done)**
- [ ] All user-facing strings in `client/src/lib/schemas/event.ts` use the chosen accented Portuguese spelling
- [ ] Frontend lint/build passes

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Trigger event form validation errors and confirm message rendering

**Notes**
- Risks: inconsistent wording vs other schemas if no common phrasing is chosen.
- Open questions: final wording conventions (sentence casing and punctuation).

**Implementation Notes**
- Updated `client/src/lib/schemas/event.ts` user-facing labels and validation messages to accented pt-BR UTF-8.
- Kept scope focused on validation/messages and enum labels only (no helper text or button labels).

### Task: T-053 — UTF-8: update student schema validation messages
**Type:** frontend
**Status:** DONE
**Depends on:** None

**Description**
- Update student Zod schema messages and labels to use proper Portuguese accents (UTF-8).

**Files likely affected (best guess)**
- client/src/lib/schemas/student.ts

**DoD (Definition of Done)**
- [x] All user-facing strings in `client/src/lib/schemas/student.ts` use the chosen accented Portuguese spelling
- [x] Frontend lint/build passes

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Trigger student form validation errors and confirm message rendering

**Notes**
- Risks: mismatch between backend error messages and frontend validation messages.
- Open questions: whether to align field names exactly with UI labels.

**Implementation Notes**
- Updated user-facing student schema messages to UTF-8 accented pt-BR in `client/src/lib/schemas/student.ts`.

### Task: T-054 — UTF-8: update employee/parent schema validation messages
**Type:** frontend
**Status:** DONE
**Depends on:** None

**Description**
- Update employee and parent Zod schema messages and labels to use proper Portuguese accents (UTF-8).

**Files likely affected (best guess)**
- client/src/lib/schemas/employee.ts
- client/src/lib/schemas/parent.ts
- client/src/lib/schemas/index.ts

**DoD (Definition of Done)**
- [x] All user-facing strings in `client/src/lib/schemas/employee.ts` use the chosen accented Portuguese spelling
- [x] All user-facing strings in `client/src/lib/schemas/parent.ts` use the chosen accented Portuguese spelling
- [x] Frontend lint/build passes

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Trigger employee/parent form validation errors and confirm message rendering

**Notes**
- Risks: some strings may be shared via re-exports; ensure no duplication.
- Open questions: whether to centralize common phrases (not required for this polish pass).

**Implementation Notes**
- Updated user-facing employee and parent schema messages to UTF-8 accented pt-BR in `client/src/lib/schemas/employee.ts` and `client/src/lib/schemas/parent.ts`.

### Task: T-055 — UTF-8: sweep UI error/toast/empty-state strings (core screens)
**Type:** frontend
**Status:** TODO
**Depends on:** T-052

**Description**
- Sweep core screens (students, events, employees, parents) for user-facing error/toast/empty-state strings and normalize to UTF-8 accented Portuguese.
- Keep the change small: focus on strings shown during validation, API errors, and empty states.

**Files likely affected (best guess)**
- client/src/features/students/
- client/src/features/events/
- client/src/features/employees/
- client/src/features/parents/
- client/src/lib/

**DoD (Definition of Done)**
- [ ] Core screens no longer display ASCII-only Portuguese where accents are expected
- [ ] Frontend lint/build passes

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Trigger API error states (offline/500) and empty lists and confirm messages render with accents

**Notes**
- Risks: broad sweep can balloon; keep to core screens only.
- Open questions: whether to also normalize button labels and helper text (out of scope unless clearly an error message).

## Archive (DONE)
### Task: T-030 — Dashboard and navigation UI
- Status: DONE
- Scope: Established routing shell and main navigation pages.
- Verification: frontend build/lint/manual navigation checks.
- Commit: not recorded in this epic history.

## Review Notes (append-only)
- Reviewer notes:
- 2026-03-05: Added S-033 and T-052..T-055 for UTF-8/accents normalization in frontend validation and error messages.
- 2026-03-06: Updated navigation acceptance to exclude parents module; marked T-031, T-053, and T-054 as DONE with implementation notes and verification (`cd client && npm run lint && npm run build`).
- 2026-03-06: Marked T-032 as DONE after shared event create/edit form refactor, edit route wiring, and event list/detail polish; verified with `cd client && npm run lint && npm run build`.
