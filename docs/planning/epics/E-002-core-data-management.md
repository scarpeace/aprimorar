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
**Status:** TODO
**Links:** T-019 (TODO), T-020 (TODO), T-021 (TODO), T-022 (TODO), T-023 (TODO), T-024 (TODO), T-025 (TODO)
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

### Task: T-019 — DB migration: drop Student `activity` column
**Type:** backend
**Status:** TODO
**Depends on:** None

**Description**
- Add a Flyway migration to remove the `activity` column from the `students` table.
- Update seed data so local/dev environments still boot and tests can run.

**Files likely affected (best guess)**
- server/api-aprimorar/src/main/resources/db/migration/V*__drop_student_activity.sql
- server/api-aprimorar/src/main/resources/db/seed/V2__seed_data.sql

**DoD (Definition of Done)**
- [ ] App starts with Flyway applying the new migration cleanly
- [ ] Seed data loads without referencing `students.activity`

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: N/A
- Manual: Start API locally and verify Flyway migrations succeed

**Notes**
- Risks: dropping a column is a breaking DB change; ensure prod rollout sequencing is understood.
- Open questions: whether historical activity values should be migrated/archived.

### Task: T-020 — Update student DTO contract (remove `activity`, add `age`)
**Type:** backend
**Status:** TODO
**Depends on:** T-019

**Description**
- Remove `activity` from `CreateStudentDTO` and `UpdateStudentDTO`.
- Change birthdate validation to “cannot be in the future” (allow today) where applicable.
- Update `StudentResponseDTO` to include a computed `age` field and remove `activity`.

**Files likely affected (best guess)**
- server/api-aprimorar/src/main/java/com/aprimorar/api/dto/student/CreateStudentDTO.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/dto/student/UpdateStudentDTO.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/dto/student/StudentResponseDTO.java

**DoD (Definition of Done)**
- [ ] Student create/update requests compile without `activity`
- [ ] Student response DTO includes `age` and no longer includes `activity`
- [ ] Birthdate validation rejects future dates

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: N/A
- Manual: POST/PATCH student payloads in Swagger without `activity`

**Notes**
- Risks: API contract breaking change for any existing client.
- Open questions: exact JSON field ordering/naming expectations for `age`.

### Task: T-021 — Update Student entity + mapper (remove `activity`, compute `age`)
**Type:** backend
**Status:** TODO
**Depends on:** T-020

**Description**
- Remove `activity` from `Student` entity and from `StudentMapper` mappings.
- Compute `age` from `birthdate` when creating `StudentResponseDTO`.
- Delete `Activity` enum if it becomes unused.

**Files likely affected (best guess)**
- server/api-aprimorar/src/main/java/com/aprimorar/api/entity/Student.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/mapper/StudentMapper.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/enums/Activity.java

**DoD (Definition of Done)**
- [ ] Student entity no longer contains `activity`
- [ ] Student responses include correct computed `age`
- [ ] No unused `Activity` references remain in `server/api-aprimorar`

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: N/A
- Manual: `GET /v1/students` shows `age` and no `activity`

**Notes**
- Risks: timezone affects age boundaries on birthdays.
- Open questions: which timezone to use for age calculation (keep consistent with existing rules).

### Task: T-022 — Remove student `activity` filtering + remove age-range service validation
**Type:** backend
**Status:** TODO
**Depends on:** T-021

**Description**
- Remove `activity` query params from student list endpoints.
- Remove repository query methods that filter by `activity`.
- Remove min/max age validation in `StudentService`; rely on DTO birthdate validation only.
- Remove/adjust `StudentValidationException` handling used only for age-range errors.

**Files likely affected (best guess)**
- server/api-aprimorar/src/main/java/com/aprimorar/api/controller/StudentController.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/service/StudentService.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/repository/StudentRepository.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/exception/handler/GlobalExceptionHandler.java
- server/api-aprimorar/src/main/java/com/aprimorar/api/exception/domain/StudentValidationException.java

**DoD (Definition of Done)**
- [ ] `GET /v1/students` and `GET /v1/students/active` no longer accept/use `activity`
- [ ] Creating/updating a student no longer checks min/max age range
- [ ] Validation errors remain clear and stable (400)

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: N/A
- Manual: Call list endpoints with `activity` and confirm behavior matches decision (reject or ignore)

**Notes**
- Risks: potential breaking change for query-string clients.
- Open questions: compatibility strategy for `activity` query param (reject vs ignore).

### Task: T-023 — Update mapper tests for `age` response
**Type:** backend
**Status:** TODO
**Depends on:** T-021

**Description**
- Update `StudentMapperTest` to assert `age` is returned and `activity` is not.

**Files likely affected (best guess)**
- server/api-aprimorar/src/test/java/com/aprimorar/api/mapper/StudentMapperTest.java

**DoD (Definition of Done)**
- [ ] Mapper tests pass and cover `age` computation

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test -Dtest=StudentMapperTest`
- Frontend: N/A
- Manual: N/A

**Notes**
- Risks: flaky tests if age depends on current date; use fixed clock or relative assertions.
- Open questions: whether to inject a clock/zone for deterministic tests.

### Task: T-024 — Update DTO/service tests for removed `activity` and relaxed birthdate rules
**Type:** backend
**Status:** TODO
**Depends on:** T-022

**Description**
- Update `CreateStudentDTOTest` to remove `activity` validation checks.
- Update `StudentServiceTest` to remove activity filtering tests and age-range tests; keep birthdate future-date validation covered.

**Files likely affected (best guess)**
- server/api-aprimorar/src/test/java/com/aprimorar/api/controller/dto/CreateStudentDTOTest.java
- server/api-aprimorar/src/test/java/com/aprimorar/api/service/StudentServiceTest.java

**DoD (Definition of Done)**
- [ ] Student-related backend tests pass after contract change
- [ ] Test coverage remains for rejecting future birthdates

**Verification**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: N/A
- Manual: N/A

**Notes**
- Risks: test suite assumptions about Activity enum values.
- Open questions: whether to add a small integration test for `GET /v1/students` response shape.

### Task: T-025 — Frontend: remove student `activity` usage; show `age`
**Type:** frontend
**Status:** TODO
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

## Review Notes (append-only)
- 2026-03-05:
  - Quality: Epic cleaned to active-vs-archive format to reduce planning drift and status mismatches.
  - Security: Keep API validation errors stable and non-leaky as fields evolve.
  - Performance: Re-check list filter query plans after any future index or filter changes.
