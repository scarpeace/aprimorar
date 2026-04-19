---
phase: 02-student-parent-registry-hardening
plan: 02
subsystem: api
tags: [spring-boot, jpa, openapi, kubb, student, parent]
requires:
  - phase: 01-authentication-protected-access
    provides: protected backend/frontend flows used by the hardened registry contracts
  - phase: 02-student-parent-registry-hardening
    provides: single-responsável invariants and fetch intent from plan 02-01
provides:
  - embedded responsible summaries on student read contracts
  - active-student guardrails for responsible archive/delete actions
  - regenerated Kubb contracts aligned with the current single-responsável model
affects: [phase-02-ui-student-flows, phase-02-ui-parent-flows, client-kubb]
tech-stack:
  added: []
  patterns: [student read DTO embedding, repository-backed active-link lifecycle checks, backend-openapi-to-kubb sync]
key-files:
  created:
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentResponsibleSummaryDTO.java
    - client/src/kubb/types/StudentResponsibleSummaryDTO.ts
    - client/src/kubb/zod/studentResponsibleSummaryDTOSchema.ts
  modified:
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentMapper.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentService.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentRepository.java
    - client/src/kubb/types/StudentResponseDTO.ts
key-decisions:
  - "Keep parentId as the only student write-field while embedding a compact responsible summary on reads."
  - "Block responsible archive/delete only when active students still reference the record; archived student history remains valid."
patterns-established:
  - "Student read contracts can denormalize linked responsible data when the persistence model stays single-link."
  - "Responsible lifecycle checks should use repository queries that target only active student references."
requirements-completed: [STUD-01, STUD-02, STUD-03, STUD-04, PARN-02, PARN-03, PARN-04]
duration: 19 min
completed: 2026-04-19
---

# Phase 2 Plan 2: Student & Parent Registry Hardening Summary

**Student reads now embed the current responsável summary, parent lifecycle rules protect active student links, and Kubb contracts mirror the hardened single-responsável API.**

## Performance

- **Duration:** 19 min
- **Started:** 2026-04-19T14:49:00Z
- **Completed:** 2026-04-19T15:08:13Z
- **Tasks:** 3
- **Files modified:** 22

## Accomplishments
- Added `StudentResponsibleSummaryDTO` to student read payloads while preserving `parentId` as the only write-side linkage.
- Hardened `ParentService` so archive/delete actions fail when active students still depend on the responsible record.
- Regenerated Kubb types, hooks, and Zod schemas so the SPA sees the new embedded responsible summary.

## Task Commits

Each task was committed atomically:

1. **Task 1: Update student APIs and service orchestration around the required responsável** - `8e447f80` (test), `086ecaf5` (feat)
2. **Task 2: Harden responsável archive and delete rules around active student links** - `8a988c53` (test), `66786cb5` (fix)
3. **Task 3: Regenerate OpenAPI and Kubb contracts for student and responsável flows** - `5b64e5ef` (chore)

**Plan metadata:** `057f0746` (docs)

## Files Created/Modified
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentResponsibleSummaryDTO.java` - New embedded responsible summary contract.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/dto/StudentResponseDTO.java` - Student read DTO now includes `responsible`.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentMapper.java` - Maps responsible summary and preserves correct audit timestamp ordering.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java` - Rejects missing responsible IDs explicitly and keeps read flows on the single-link model.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/parent/ParentService.java` - Blocks archive/delete when active student links still exist.
- `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentRepository.java` - Adds active-link existence query for responsible lifecycle guards.
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java` - Covers missing responsible IDs and the new read contract surface.
- `server/api-aprimorar/src/test/java/com/aprimorar/api/domain/parent/ParentServiceTest.java` - Covers active-student guardrails for archive/delete.
- `client/src/kubb/types/StudentResponseDTO.ts` - Generated student response now includes the embedded responsible summary.
- `client/src/kubb/types/StudentResponsibleSummaryDTO.ts` - Generated frontend type for the responsible summary.

## Decisions Made
- Kept the existing one-student-to-one-responsável write flow untouched to avoid relationship redesign scope.
- Embedded enough responsible data in student reads to remove extra SPA fetches for common list/detail rendering.
- Treated active-student checks as lifecycle integrity requirements, while allowing responsíveis with zero or only archived links to remain manageable.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 1 - Bug] Corrected student audit field ordering in the mapper**
- **Found during:** Task 1 (student API hardening)
- **Issue:** `StudentMapper` was wiring `createdAt` and `updatedAt` in the wrong order for `StudentResponseDTO` construction.
- **Fix:** Reordered mapper arguments while adding the embedded responsible summary.
- **Files modified:** `server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentMapper.java`
- **Verification:** `./server/api-aprimorar/mvnw -f ./server/api-aprimorar/pom.xml -Dtest=StudentServiceTest test`
- **Committed in:** `086ecaf5`

---

**Total deviations:** 1 auto-fixed (1 bug)
**Impact on plan:** The fix was required for response correctness and stayed within the planned API-hardening scope.

## Issues Encountered
- `npm --prefix client run build` failed on pre-existing unrelated frontend issues: `client/src/components/ui/text-input.tsx:43` contains invalid syntax and `react-router` type declarations also failed parsing in `node_modules`. Both were logged to `deferred-items.md` instead of being fixed out of scope.

## User Setup Required
None - no external service configuration required.

## Next Phase Readiness
- Student and responsible frontend flows can now consume embedded responsible data directly from generated hooks.
- Responsible archive/delete UX can rely on clear Portuguese guard errors for active linked students.
- Deferred frontend build blockers should be cleared before broader SPA verification in later plans.

## Self-Check: PASSED

---
*Phase: 02-student-parent-registry-hardening*
*Completed: 2026-04-19*
