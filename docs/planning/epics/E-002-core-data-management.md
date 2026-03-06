# Epic: E-002 — Core Data Management
**Goal:** Provide complete, performant CRUD and management for core entities (Students, Employees, Parents, Events).
**Status:** IN_PROGRESS
**Owner:** Gu
**Related milestone/phase:** Phase 1

## Scope
- In scope:
  - Student search/filter (name/includeArchived) and response contract stabilization
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
- Next up: S-016 name uniqueness constraints cleanup (low priority)

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
**Status:** DONE
**Links:** T-019 (DONE), T-020 (DONE), T-021 (DONE), T-022 (DONE), T-023 (DONE), T-024 (DONE), T-025 (DONE), T-026 (DONE)
**Intent:** Remove redundant Student `activity`, relax student age-range rules, and expose computed `age` in responses.

**Acceptance Criteria**
- Student create/update no longer accept `activity`.
- Student responses include computed `age` and no longer include `activity`.
- Student birthdate validation rejects future dates (no min/max age range validation).

**Test Plan**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: Create/update a student and verify `age` field in responses

### Story: S-017 — Student Archiving (`archivedAt`) and Return Flow
**Status:** DONE
**Links:** T-028 (DONE)
**Intent:** Replace `active` semantics with `archivedAt` to keep default UI clean while preserving history and allowing student return without re-registration.

**Acceptance Criteria**
- Student model uses `archivedAt` and `lastReactivatedAt` (no `active` field).
- `GET /v1/students` defaults to active-only; `includeArchived=true` returns all.
- Students can be archived/unarchived without re-registration.
- `DELETE /v1/students/{id}` remains archive alias during transition.

**Test Plan**
- Backend: `cd server/api-aprimorar && ./mvnw test`
- Frontend: `cd client && npm run lint && npm run build`
- Manual: archive/unarchive student and validate list behavior with/without `includeArchived`

## Tasks
No active tasks listed here. See `## Archive (DONE)` for completed work and `## Stories` for next priorities.
## Archive (DONE)
### Task: T-018 — Unify student list endpoint with `active` filter
- Status: CANCELLED
- Scope: Superseded by T-028 (`archivedAt` + `includeArchived`); the `active` list contract should not be reintroduced.
- Verification: N/A

### Task: T-028 — Students: migrate from `active` to `archivedAt`
- Status: DONE
- Scope: Replaced student `active` with `archivedAt` + `lastReactivatedAt`, added `includeArchived` list param, and implemented archive/unarchive endpoints while keeping DELETE as archive alias.
- Verification: `cd server/api-aprimorar && ./mvnw test`; `cd client && npm run lint && npm run build`.
- Notes: Auth/tenant scoping tests are pending until E-003 exists.

### Task: T-028-A-A — DB migration: add archive columns and backfill
- Status: DONE
- Scope: Flyway migration `V6__add_student_archive_columns.sql` adds `archived_at` and `last_reactivated_at` to `tb_student`.
- Backfill: legacy `active=false` sets `archived_at=COALESCE(updated_at, created_at)`; otherwise `archived_at=NULL`.
- Verification: `cd server/api-aprimorar && ./mvnw test`; manual inspect `tb_student` after migration.

### Task: T-028-A — Students archive model + migration (backend)
- Status: DONE
- Scope: Completed via T-028-A-A (add/backfill columns), T-028-A-B (drop `active` + seed alignment), and T-028-A-C (entity/DTO/service alignment).
- Notes: Original task was split due to complexity.

### Task: T-028-A-B — DB migration: drop `active` and align seed
- Status: DONE
- Scope: Dropped `tb_student.active` via `V7__drop_student_active_column.sql`; seed no longer writes `active`.
- Verification: `cd server/api-aprimorar && ./mvnw test`; manual recreate DB/container and start app successfully.

### Task: T-028-A-C — Backend model alignment with archive columns
- Status: DONE
- Scope: Replaced `Student.active` with `archivedAt`/`lastReactivatedAt`; aligned `StudentResponseDTO`/`StudentMapper` and repository queries.
- Notes: Preserved `DELETE /v1/students/{id}` as archive alias; removed `active`-based derived queries and aligned `EventService`/tests.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-028-B-A — List contract migration to `includeArchived`
- Status: DONE
- Scope: Replaced list query param `active` with `includeArchived` (default `false`) on `GET /v1/students` and routed service queries accordingly.
- Notes: Default list returns non-archived; `includeArchived=true` returns archived + non-archived.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-028-B-B — Archive/unarchive endpoints
- Status: DONE
- Scope: Added `PATCH /v1/students/{studentId}/archive` (idempotent) and `PATCH /v1/students/{studentId}/unarchive` (clears `archivedAt`, sets `lastReactivatedAt`); kept `DELETE /v1/students/{studentId}` as archive alias.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-028-B-C — DELETE alias + API behavior tests
- Status: DONE
- Scope: Preserved `DELETE /v1/students/{id}` as archive alias and expanded controller/service integration coverage for list defaults, `includeArchived=true`, archive/unarchive, and DELETE alias behavior.

### Task: ST-166 — DB index for `archived_at`
- Status: DONE
- Scope: Added `idx_tb_student_archived_at` via `V8__add_student_archived_at_index.sql`.
- Verification: app boots and list queries remain stable.
- Notes: Auth/tenant-scoping coverage is tracked under ST-169.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

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

### Task: T-025 — Frontend: remove student `activity` usage; show `age`
- Status: DONE
- Scope: Updated student frontend schemas/pages to remove `activity` and render `age` in create/list/detail flows.
- Verification: `cd client && npm run lint && npm run build`.

### Task: T-026 — Stabilize `age`-related tests with deterministic clock/zone
- Status: DONE
- Scope: Injected `Clock` into student age mapping and made age tests deterministic with fixed date/zone coverage.
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

- 2026-03-06 (T-028):
  - Quality: Keep transition tight (backfill, drop `active`, and preserve DELETE-as-archive behavior); confirm how this interacts with T-018 so contracts don't drift.
  - Security: Ensure `includeArchived` never bypasses auth/tenant scoping; default list must not leak archived records.
  - Performance: Prefer `WHERE archived_at IS NULL` for default list; add an index on `archived_at` and re-check list/pagination query plans.

- 2026-03-06 (T-028 follow-ups):
  - Quality: Track a single source of truth for list semantics (either `includeArchived` or legacy `active`) and document the transition plan to avoid UI/API mismatch.
  - Security: Add tests that `includeArchived=true` does not widen scope beyond the caller's allowed tenant/user context.
  - Performance: Validate that the `archived_at` index is used under typical filters/sorts and that pagination remains stable.

- 2026-03-06 (T-028-A-A):
  - Quality: Migration/backfill logic is centralized in Flyway; confirm behavior when legacy `active` is NULL/unexpected and when `updated_at` is NULL.
  - Security: Migration should not emit row contents to logs; keep schema changes additive until `active` is dropped.
  - Performance: Column add/backfill is one-time; ensure downstream list queries can use ST-166 `archived_at` index.

- 2026-03-06 (T-028-A-C):
  - Quality: Ensure DTO/mapper field names and nullability match the API contract; add/adjust unit coverage for `archivedAt`/`lastReactivatedAt` serialization.
  - Security: Double-check archive-aware queries still enforce any tenant/user scoping and that `includeArchived` cannot widen results beyond authorization.
  - Performance: Confirm default list uses `archived_at IS NULL` (index-friendly) and that pagination/sort behavior remains stable after query changes.

- 2026-03-06 (T-028-B):
  - Quality: `includeArchived` list semantics are in place; remaining gap is explicit unarchive (return flow) and end-to-end controller integration coverage.
  - Security: Verify archived filtering cannot be used to enumerate records outside auth/tenant scope; keep archive/unarchive endpoints scoped and idempotent.
  - Performance: Confirm the default list path stays on `archived_at IS NULL` and that `includeArchived=true` remains paginated and stable under typical sorts.

- 2026-03-06 (T-028-B-B):
  - Quality: Endpoints exist; ensure contract-level tests cover idempotency and verify `lastReactivatedAt` behavior on repeated unarchive calls.
  - Security: Add explicit negative tests for cross-tenant/cross-user access on archive/unarchive and `includeArchived=true` paths.
  - Performance: No expected hot-path impact; keep archive/unarchive updates index-friendly and verify list/pagination remains stable after status toggles.

- 2026-03-06 (T-028-B-C):
  - Quality: Controller/service test coverage verifies default list behavior, `includeArchived=true`, and DELETE-as-archive consistency; keep checks aligned with any future contract tweaks.
  - Security: Ensure auth/tenant scoping is explicitly covered (tracked under ST-169) so `includeArchived` and archive/unarchive cannot widen access.
  - Performance: No runtime impact (tests only); keep default list on `archived_at IS NULL` and re-check query plans after index/filters change.

- 2026-03-06 (T-028-C):
  - Quality: Ensure checkbox defaults match backend defaults (`includeArchived` false/omitted) and remove any lingering `active`-based list wiring in the client.
  - Security: Do not expose archived records by default in UI; treat "show archived" as a visibility toggle only (still fully scoped by API auth/tenant rules).
  - Performance: Avoid extra client-side filtering; rely on backend paging/filtering and keep query params stable to prevent cache misses.
