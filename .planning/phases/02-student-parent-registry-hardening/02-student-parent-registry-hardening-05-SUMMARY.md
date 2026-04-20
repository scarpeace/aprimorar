---
phase: 02-student-parent-registry-hardening
plan: 05
subsystem: api
tags: [spring-boot, jpa, student, search, archived-filter, tdd]
requires:
  - phase: 02-student-parent-registry-hardening
    provides: verified student/responsável read flows and phase gap report context
provides:
  - CPF-aware student list search semantics
  - default non-archived student list filtering
  - backend regression coverage for archived toggle behavior
affects: [phase-02-student-list, student-search-specifications]
tech-stack:
  added: []
  patterns: [specification-based search composition, service-level archived defaulting, backend-tdd-regression]
key-files:
  created:
    - server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/repository/StudentSpecificationsTest.java
  modified:
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/repository/StudentSpecifications.java
    - server/api-aprimorar/src/main/java/com/aprimorar/api/domain/student/StudentService.java
    - server/api-aprimorar/src/test/java/com/aprimorar/api/domain/student/StudentServiceTest.java
key-decisions:
  - "Treat archived=false and archived omission as the same active-list path so archived student data is not disclosed by default."
  - "Extend the existing student search specification directly with CPF matching instead of adding a separate filter path."
patterns-established:
  - "Student list safety rules should be asserted by inspecting the composed specification used by the repository call."
requirements-completed: [STUD-04]
duration: 5 min
completed: 2026-04-20
---

# Phase 2 Plan 5: Student & Parent Registry Hardening Summary

**Student discovery now matches the existing secretary-facing list promise: CPF search works, archived students stay hidden by default, and the backend locks both behaviors with regression tests.**

## Performance

- **Duration:** 5 min
- **Tasks:** 2
- **Files modified:** 4

## Accomplishments

- Added failing TDD coverage for CPF search participation and archived toggle semantics in the student list flow.
- Updated the student search specification so CPF fragments match alongside name, email, and school.
- Changed the student list service to exclude archived records unless the archived toggle is explicitly enabled.

## Task Commits

1. **Task 1: Lock student discovery semantics with failing tests** - `6850cffe` (test)
2. **Task 2: Implement CPF-aware search and default non-archived list behavior** - `4c6b96fa` (feat)

## Decisions Made

- The standard student list must compose `notArchived()` whenever `archived` is `false` or omitted.
- CPF belongs inside the existing free-text student search specification instead of a separate endpoint or query parameter.

## Deviations from Plan

None - plan executed exactly as written.

## Auth Gates

None.

## Known Stubs

None.

## Self-Check: PASSED
