---
phase: 02-student-parent-registry-hardening
plan: 01
subsystem: api
tags: [spring-boot, jpa, student, parent, testing]
requires:
  - phase: 01-authentication-protected-access
    provides: protected backend foundation for brownfield hardening work
provides:
  - required responsável invariant enforced inside the Student aggregate
  - explicit repository fetch plans for student reads that need parent data
  - regression tests documenting the preserved one-student-to-one-responsável model
affects: [02-02, 02-03, student-contracts, student-ui]
tech-stack:
  added: []
  patterns:
    - centralize aggregate invariants inside the entity
    - use EntityGraph for intentional parent loading on student read paths
key-files:
  created: []
  modified:
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentRepository.java
    - server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentTest.java
key-decisions:
  - Keep the existing tb_students.parent_id mapping and harden it instead of redesigning the relationship model.
  - Use repository-level EntityGraph annotations so student reads load the linked responsável intentionally.
patterns-established:
  - "Student validates the required responsável through one shared business message for create and update flows."
  - "StudentRepository read methods that feed DTO mapping declare parent fetch intent explicitly."
requirements-completed: [STUD-01, STUD-02, STUD-03, PARN-04]
duration: 4 min
completed: 2026-04-19
---

# Phase 2 Plan 1: Student responsável invariant hardening Summary

**Student aggregate guardrails plus explicit parent-loading repository reads for the existing single-responsável model.**

## Performance

- **Duration:** 4 min
- **Started:** 2026-04-19T14:44:55Z
- **Completed:** 2026-04-19T14:49:05Z
- **Tasks:** 2
- **Files modified:** 3

## Accomplishments
- Documented the required-responsável rule with deterministic Student domain tests.
- Centralized the missing-responsável validation in `Student` with a business-facing Portuguese message.
- Marked student read queries with `@EntityGraph(attributePaths = "parent")` so downstream mapping does not rely on accidental lazy loading.

## Task Commits

Each task was committed atomically:

1. **Task 1: Lock in the single required responsável invariant with tests** - `859a7d49` (test)
2. **Task 2: Implement aggregate and repository hardening for the existing responsável link** - `d40de082` (test), `26e1c4ec` (feat)

## Files Created/Modified
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentTest.java` - Captures invariant and repository-fetch expectations with deterministic UUID fixtures.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/Student.java` - Enforces the required responsável through a single aggregate guardrail.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentRepository.java` - Declares explicit parent fetch plans for student read methods.

## Decisions Made
- Kept the existing `Parent parent` / `parent_id` persistence model because Phase 2 explicitly preserves one student to one responsável.
- Strengthened read reliability with repository annotations instead of changing service contracts or introducing new relationship abstractions.

## Deviations from Plan

None - plan executed exactly as written.

## Issues Encountered

None.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness

- Student persistence now guarantees exactly one responsável for create and update flows.
- Student read paths are prepared for Phase 2 contract and UI hardening without hidden lazy-loading dependence.

## Self-Check: PASSED

---
*Phase: 02-student-parent-registry-hardening*
*Completed: 2026-04-19*
