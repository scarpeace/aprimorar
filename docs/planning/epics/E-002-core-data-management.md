# Epic: E-002 — Core Data Management
**Goal:** Keep core CRUD and domain contracts stable for students, employees, parents, and events.
**Status:** IN_PROGRESS
**Owner:** Gu
**Related milestone/phase:** Phase 1

## Scope
- In scope:
  - Student filters, archive flow, and response contract stability
  - Student birthdate validation and computed `age`
  - Event filters and required `content`
  - Parent rules used by student flows
- Out of scope:
  - Authentication and RBAC (E-003)
  - Calendar integration (E-005)
  - Payment gateway work (E-006)

## Workboard
- Current focus: No active backend task
- Blocked: S-013 frontend inline parent flow depends on E-004
- Next up: review name uniqueness constraints and remaining domain cleanup

## Stories
### Story: S-010 — Student management enhancements
**Status:** DONE
**Links:** T-010 (DONE), T-015 (DONE)
**Intent:** Keep student list/search and validation stable for daily operations.

### Story: S-011 — Employee management baseline
**Status:** DONE
**Links:** T-013 (DONE)
**Intent:** Preserve stable employee CRUD behavior.

### Story: S-012 — Event management improvements
**Status:** DONE
**Links:** T-011 (DONE), T-012-A (DONE), T-012-B (DONE), T-012-C (DONE), T-016 (DONE)
**Intent:** Keep event filtering and `content` classification stable across API and UI.

### Story: S-013 — Parent/guardian flow completion
**Status:** BLOCKED
**Links:** T-014 (DONE)
**Intent:** Backend is ready; frontend inline parent create/select still depends on E-004.

### Story: S-015 — Simplify student domain
**Status:** DONE
**Links:** T-019 (DONE), T-020 (DONE), T-021 (DONE), T-022 (DONE), T-023 (DONE), T-024 (DONE), T-025 (DONE), T-026 (DONE)
**Intent:** Remove legacy student `activity`, expose computed `age`, and keep birthdate rules simple.

### Story: S-017 — Student archiving and return flow
**Status:** DONE
**Links:** T-028 (DONE), T-028-A-A (DONE), T-028-A-B (DONE), T-028-A-C (DONE), T-028-B-A (DONE), T-028-B-B (DONE), T-028-B-C (DONE), ST-166 (DONE)
**Intent:** Replace `active` semantics with archive timestamps while preserving a safe return flow.

## Tasks
- No active tasks.

## Archive (DONE)
### Task: T-010 — Student listing/search and validation
- Status: DONE
- Scope: Added student list/search behavior and stabilized validation responses.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-011 — Event list performance and filters
- Status: DONE
- Scope: Added composable event filters and improved list behavior.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-012-A / T-012-B / T-012-C — Event `content` rollout
- Status: DONE
- Scope: Made `content` required in backend validation, response mapping, and test coverage.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-013 — Employee CRUD baseline
- Status: DONE
- Scope: Preserved stable employee management behavior.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-014 — Parent backend baseline
- Status: DONE
- Scope: Preserved backend parent support required by student flows.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-015 — Legacy student age validation pass
- Status: DONE
- Scope: Earlier age validation work later simplified by the domain cleanup tasks.
- Verification: `cd server/api-aprimorar && ./mvnw test`.

### Task: T-017 — Manual regression verification
- Status: DONE
- Scope: Completed smoke checks for student/event filters and employee CRUD.
- Verification: Manual Swagger/UI pass.

### Task: T-018 — Legacy `active` filter endpoint
- Status: CANCELLED
- Scope: Superseded by `archivedAt` plus `includeArchived`; should not be reintroduced.
- Verification: N/A.

### Task: T-019 / T-020 / T-021 / T-022 / T-023 / T-024 / T-025 / T-026 — Student domain simplification
- Status: DONE
- Scope: Removed `activity`, added `age`, simplified birthdate rules, updated API/UI/tests, and stabilized time-dependent behavior with `Clock`.
- Verification: `cd server/api-aprimorar && ./mvnw test`; `cd client && npm run lint && npm run build` where applicable.

### Task: T-028 and slices — Student archive migration
- Status: DONE
- Scope: Added archive columns and index, dropped legacy `active`, migrated list contract to `includeArchived`, and implemented archive/unarchive plus DELETE alias coverage.
- Verification: `cd server/api-aprimorar && ./mvnw test`; `cd client && npm run lint && npm run build` where applicable.

## Review Notes (append-only)
- 2026-03-08:
  - Quality: Epic is functionally complete except for frontend inline parent flow tracked in E-004.
  - Security: Auth-aware archive/filter scoping still depends on E-003.
  - Performance: Keep list queries index-friendly and re-check plans when filters evolve.
