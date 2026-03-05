# Epic: E-002 — Core Data Management
**Goal:** Provide complete, performant CRUD and management for core entities (Students, Employees, Parents, Events).
**Status:** IN_PROGRESS
**Owner:** Gu
**Related milestone/phase:** Phase 1

## Scope
- In scope:
  - Student search/filter (name/active) and response contract stabilization
  - Student birthdate validation (reject future dates) and response `age` field
  - Removal of Student `activity` (use Event `content` where classification is needed)
  - Event listing performance and core filters
  - Event content classification (`content`) in backend/API
  - Parent rules used by student flows
- Out of scope:
  - Authentication/RBAC (E-003)
  - Calendar integration (E-005)
  - Payment gateway integration (E-006)

## Workboard
- Current focus: None
- Blocked: S-013 frontend parent inline flow depends on E-004
- Next up: S-015 student contract/domain simplification

## Stories
### Story: S-010 — Student Management Enhancements
**Status:** DONE
**Links:** T-010 (DONE), T-015 (DONE)
**Intent:** Keep student search/filter and age validation stable for day-to-day operations.

**Acceptance Criteria**
- Student list supports name filtering and returns stable response contract.
- Invalid student birthdates/age inputs return a clear 400 validation error.

**Test Plan**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Use Swagger to create/list students with valid/invalid filters

### Story: S-011 — Employee Management
**Status:** DONE
**Links:** T-013 (DONE)
**Intent:** Preserve stable employee CRUD baseline.

**Acceptance Criteria**
- Employee CRUD endpoints continue to work with pagination.
- Deactivate (soft delete) behavior remains unchanged.

**Test Plan**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create/update/deactivate an employee via Swagger

### Story: S-012 — Event/Class Management Improvements
**Status:** DONE
**Links:** T-011 (DONE)
**Intent:** Keep event listing performant and filterable.

**Acceptance Criteria**
- `GET /v1/events` supports the documented filters without regressions.
- Basic performance remains acceptable for paginated list queries.

**Test Plan**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Verify event list filters in Swagger with seed data

### Story: S-013 — Parent/Guardian Management
**Status:** BLOCKED
**Links:** T-014 (DONE)
**Intent:** Backend is ready; UI inline parent create/edit is pending in E-004.

**Acceptance Criteria**
- Backend parent endpoints required by student flows remain stable.
- Frontend can create/select a parent inline from student create/edit flow.

**Test Plan**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create student with existing parentId and with inline parent

### Story: S-014 — Event Content Classification
**Status:** DONE
**Links:** T-012-A (DONE), T-012-B (DONE), T-012-C (DONE), T-016 (DONE)
**Intent:** Make `content` required and visible across event create/update/list/detail flows.

**Acceptance Criteria**
- Event create/update reject missing or invalid `content`.
- Event list/detail responses always include `content`.

**Test Plan**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: POST/PATCH/GET events in Swagger and verify `content`

### Story: S-015 — Simplify Student Domain (Remove `activity`, Add `age`)
**Status:** IN_PROGRESS
**Links:** T-019 (DONE), T-020 (DONE), T-021 (DONE), T-022 (DONE), T-023 (DONE), T-024 (DONE), T-025 (DONE), T-026 (TODO)
**Intent:** Remove redundant Student `activity`, relax student age-range rules, and expose computed `age` in responses.

**Acceptance Criteria**
- Student create/update no longer accept `activity`.
- Student responses include computed `age` and no longer include `activity`.
- Student birthdate validation rejects future dates (no min/max age range validation).

**Test Plan**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create/update a student and verify `age` field in responses

## Tasks
### Task: T-018 — Unify student list endpoint with `active` filter
**Type:** fullstack
**Status:** TODO
**Depends on:** None

**Description**
- Add optional `active` query param to `GET /v1/students`.
- Keep `GET /v1/students/active` working for compatibility during transition.
- Update client to consume the unified route and handle change implications in one release.
- Mark `/v1/students/active` as deprecated in API/docs.
- Keep subtasks/IDs for tracking: ST-046, ST-047, ST-048, ST-049, ST-050.

**Files likely affected (best guess)**
- server/api-aprimorar/src/main/java/com/aprimorar/api/controller/StudentController.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/service/StudentService.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/repository/StudentRepository.java
- client/src/features/students/StudentsPage.tsx

**DoD (Definition of Done)**
- [ ] `GET /v1/students` supports optional `active` filter without breaking existing filters
- [ ] Client list flows use unified route (`/v1/students?...`) successfully
- [ ] `/v1/students/active` still works for compatibility
- [ ] Deprecation note is documented

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Compare `/v1/students?active=true` with `/v1/students/active` and validate UI list behavior

**Notes**
- Risks: temporary dual-route period can drift if both paths are not kept aligned.
- Open questions: removal date for deprecated route (define in a later release note).

### Task: T-025 — Frontend: remove student `activity` usage; show `age`
**Type:** frontend
**Status:** DONE
**Depends on:** T-020

**Description**
- Update client student schema/types to remove `activity` and accept `age`.
- Remove activity input from student create page.
- Update student list/detail pages to stop rendering `activity` and display `age` instead.

**Files likely affected (best guess)**
- client/src/lib/schemas/student.ts
- client/src/features/students/StudentCreatePage.tsx
- client/src/features/students/StudentsPage.tsx
- client/src/features/students/StudentDetailPage.tsx

**DoD (Definition of Done)**
- [ ] Frontend builds against the new student API contract
- [ ] Student pages no longer reference `activity`
- [ ] Student pages render `age` where helpful

**Verification**
- Backend: N/A
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create a student and confirm list/detail show `age`

**Notes**
- Risks: API and frontend may be deployed out-of-sync; coordinate release.
- Open questions: whether to show `age` on list table or only in detail view.

**Implementation Notes**
- Removed `activity` from student create/request schema and response schema usage in frontend.
- Updated student create form to stop collecting `activity`.
- Updated student list/detail pages to render `age` instead of `activity`.

### Task: T-026 — Stabilize `age`-related tests with deterministic clock/zone
**Type:** backend
**Status:** TODO
**Depends on:** T-023

**Description**
- Ensure `age` computation tests cannot become date-dependent (e.g., around birthdays or timezone boundaries).
- Prefer injecting a `Clock` (or equivalent) into the mapper/service layer and using a fixed clock in tests.

**DoD (Definition of Done)**
- [ ] All `age`-related unit tests are deterministic (no reliance on the current date)
- [ ] Zone used for `age` calculation is explicit and covered by tests

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: N/A

## Archive (DONE)
### Task: T-010 — Student listing/search and validation
- Status: DONE
- Scope: Added student filters by name/activity and stabilized list endpoints.
- Verification: backend tests + manual API checks.
- Commit: not recorded in this epic history.

### Task: T-011 — Event list performance and filters
- Status: DONE
- Scope: Added composable event filters (`start`, `end`, `studentId`, `employeeId`) and query/perf improvements.
- Verification: backend tests and endpoint smoke checks.
- Commit: not recorded in this epic history.

### Task: T-012 — Add required `content` to events (parent)
- Status: DONE
- Scope: Parent pointer task, completed via T-012-A/B/C slices.
- Verification: delegated to slice tasks.
- Commit: mixed across slice commits.

### Task: T-012-A — Backend model/DTO wiring for event `content`
- Status: DONE
- Scope: Added `EventContent` enum and required create/update validation.
- Verification: backend tests for missing/invalid content.
- Commit: not recorded in this epic history.

### Task: T-012-B — Response contract wiring for event `content`
- Status: DONE
- Scope: Included `content` in event response mapping and mapper tests.
- Verification: mapper/controller test coverage.
- Commit: not recorded in this epic history.

### Task: T-013 — Employee management is complete and stable
- Status: DONE
- Scope: Preserved legacy completion state for employee management.
- Verification: regression covered by T-017 smoke task.
- Commit: legacy/mixed.

### Task: T-014 — Parent management is complete and stable
- Status: DONE
- Scope: Preserved backend completion; frontend inline flow moved to E-004.
- Verification: final UX verification pending E-004.
- Commit: legacy/mixed.

### Task: T-015 — Student age validation (min/max)
- Status: DONE
- Scope: Enforced age range validation and boundary coverage.
- Verification: backend tests + API validation behavior.
- Commit: `c2fd221` (planning status close); implementation commit mixed.

### Task: T-017 — Manual regression verification for core list filters
- Status: DONE
- Scope: Completed manual smoke checks for student/event filters, student age 400 validation, and employee create/update/deactivate.
- Verification: Swagger/UI pass with existing dev seed data (`server/api-aprimorar/src/main/resources/db/seed/V2__seed_data.sql`).
- Commit: not recorded in this epic history.

### Task: T-012-C — Backend tests + release-safety checklist for event `content`
- Status: DONE
- Scope: Finalized backend validation coverage for required/invalid `content` and response presence; release checklist is in place.
- Verification: `cd server/api-aprimorar && ./mvnw test` plus manual POST/PATCH/GET list/detail smoke checks.
- Commit: not recorded in this epic history.

### Task: T-016 — (Frontend) Event `content` dropdown and display
- Status: DONE
- Scope: Added required `content` to event create flow and rendered `content` in event list/detail using API enum values.
- Verification: `cd client && npm run lint && npm run build`.
- Commit: not recorded in this epic history.

### Task: T-019 — DB migration: drop Student `activity` column
- Status: DONE
- Scope: Removed `tb_student.activity` from baseline schema; aligned seed data for the simplified student model.
- Verification: `cd server/api-aprimorar && ./mvnw test` + Flyway local startup.
- Notes: Migration-history rewrite requires fresh DB/container.

### Task: T-020 — Update student DTO contract (remove `activity`, add `age`)
- Status: DONE
- Scope: Requests drop `activity`; responses expose computed `age`; birthdate validation rejects future dates.
- Verification: `cd server/api-aprimorar && ./mvnw test`.
- Notes: `age` computed with `America/Sao_Paulo`.

### Task: T-021 — Update Student entity + mapper (remove `activity`, compute `age`)
- Status: DONE
- Scope: Removed `activity` from entity/mappings; response mapping computes `age`.
- Verification: `cd server/api-aprimorar && ./mvnw test`.
- Notes: Timezone choice impacts boundary behavior.

### Task: T-022 — Remove student `activity` filtering + remove age-range service validation
- Status: DONE
- Scope: Removed backend activity filtering; removed min/max age-range checks; kept future-date birthdate validation.
- Verification: `cd server/api-aprimorar && ./mvnw test`.
- Notes: Incoming `activity` query params are ignored (compat behavior).

### Task: T-023 — Update mapper tests for `age` response
- Status: DONE
- Scope: Updated `StudentMapperTest` to assert `age` is returned and `activity` is absent.
- Verification: `cd server/api-aprimorar && ./mvnw test -Dtest=StudentMapperTest`.
- Notes: Ensure tests remain non-flaky as dates advance.

### Task: T-024 — Update DTO/service tests for removed `activity` and relaxed birthdate rules
- Status: DONE
- Scope: Updated student DTO/service tests to reflect removed `activity` and relaxed birthdate rules.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

## Review Notes (append-only)
- 2026-03-05:
  - Quality: Epic cleaned to active-vs-archive format to reduce planning drift and status mismatches.
  - Security: Keep API validation errors stable and non-leaky as fields evolve.
  - Performance: Re-check list filter query plans after any future index or filter changes.

- 2026-03-05 (T-023):
  - Quality: Mapper tests now assert `age` presence and `activity` absence; add a deterministic-clock follow-up to prevent date-boundary flakiness.
  - Security: No new surface area; keep DTO error messages consistent after contract changes.
  - Performance: No runtime impact (test-only); avoid adding heavyweight time utilities in hot paths.
