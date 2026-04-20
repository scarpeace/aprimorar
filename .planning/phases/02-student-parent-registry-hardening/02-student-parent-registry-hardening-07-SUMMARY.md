---
phase: 02-student-parent-registry-hardening
plan: 07
subsystem: ui
tags: [react, typescript, vite, react-hook-form, react-router]
requires:
  - phase: 01-authentication-protected-access
    provides: protected SPA shell and generated auth-aware client wiring
provides:
  - shared text input validation feedback that compiles again
  - react-router-dom 7.14.1 lockfile resolution for build-safe types
  - restored frontend production build for the Phase 2 registry SPA
affects: [student-registry, parent-registry, frontend-build, phase-2-verification]
tech-stack:
  added: []
  patterns: [friendly string conversion for mutation alerts, minimal helper typing for linked-student tests]
key-files:
  created: []
  modified:
    - client/src/components/ui/text-input.tsx
    - client/package.json
    - client/package-lock.json
    - client/src/features/employees/pages/EmployeeCreatePage.tsx
    - client/src/features/employees/pages/EmployeeEditPage.tsx
    - client/src/features/events/forms/EventForm.tsx
    - client/src/features/parents/pages/ParentCreatePage.tsx
    - client/src/features/parents/pages/ParentEditPage.tsx
    - client/src/features/parents/utils/getActiveLinkedStudentsCount.ts
    - client/src/features/parents/utils/getActiveLinkedStudentsCount.test.ts
    - client/src/features/students/pages/StudentCreatePage.tsx
key-decisions:
  - "Pin react-router-dom to 7.14.1 instead of patching installed type files."
  - "Convert mutation alert inputs through getFriendlyErrorMessage so page-level error banners match the Alert string API."
  - "Remove the stale event form title field and relax linked-student helper typings to clear existing TypeScript blockers surfaced by the full build."
patterns-established:
  - "Frontend alert banners should receive already-formatted Portuguese strings, not raw mutation error objects."
  - "Build-blocking helper tests can type only the data they consume instead of mirroring full generated DTOs."
requirements-completed: [STUD-01, STUD-02, STUD-04, PARN-01, PARN-02, PARN-03, PARN-04]
duration: 4 min
completed: 2026-04-20
---

# Phase 2 Plan 7: Frontend Build Blockers Summary

**Shared validation rendering and React Router declarations now compile cleanly, restoring a production frontend build for the Phase 2 registry flows.**

## Performance

- **Duration:** 4 min
- **Started:** 2026-04-20T12:44:47Z
- **Completed:** 2026-04-20T12:48:33Z
- **Tasks:** 2
- **Files modified:** 11

## Accomplishments
- Repaired the shared text input so field-level validation messages render safely by identifier.
- Upgraded `react-router-dom` to `7.14.1` and refreshed the lockfile to consume the fixed router type declarations.
- Cleared additional TypeScript blockers exposed by the full build so `npm run build` succeeds again for the SPA.

## Task Commits

Each task was committed atomically:

1. **Task 1: Repair shared text input error rendering** - `68ddcd25` (fix)
2. **Task 2: Upgrade React Router types to a build-safe release and confirm the SPA build** - `5f467b41` (fix)

## Files Created/Modified
- `client/src/components/ui/text-input.tsx` - safely resolves nested React Hook Form error messages for the shared input.
- `client/package.json` - pins `react-router-dom` to the build-safe 7.14.1 release.
- `client/package-lock.json` - refreshes the resolved router dependency graph.
- `client/src/features/employees/pages/EmployeeCreatePage.tsx` - formats mutation errors before rendering alert banners.
- `client/src/features/employees/pages/EmployeeEditPage.tsx` - formats mutation errors before rendering alert banners.
- `client/src/features/parents/pages/ParentCreatePage.tsx` - formats mutation errors before rendering alert banners.
- `client/src/features/parents/pages/ParentEditPage.tsx` - formats mutation errors before rendering alert banners.
- `client/src/features/students/pages/StudentCreatePage.tsx` - formats mutation errors before rendering alert banners.
- `client/src/features/events/forms/EventForm.tsx` - removes a stale `title` field that no longer exists in the event form contract.
- `client/src/features/parents/utils/getActiveLinkedStudentsCount.ts` - narrows helper typing to the archived flag actually consumed by the helper.
- `client/src/features/parents/utils/getActiveLinkedStudentsCount.test.ts` - aligns test fixtures with the helper's minimal input shape.

## Decisions Made
- Pinned the router dependency in source control instead of modifying `node_modules`, matching the plan's source-of-truth requirement.
- Kept the shared text input API unchanged and fixed error lookup internally to preserve existing student and responsável form usage.
- Treated the extra TypeScript failures uncovered by `npm run build` as blocking plan work and fixed them inline so the shippability gate could pass.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 3 - Blocking] Form alert banners passed raw mutation error objects into a string-only alert component**
- **Found during:** Task 2 (Upgrade React Router types to a build-safe release and confirm the SPA build)
- **Issue:** The full frontend build still failed after the router upgrade because several create/edit pages rendered `Alert` with non-string mutation errors.
- **Fix:** Routed those page-level errors through `getFriendlyErrorMessage(...)` before passing them to `Alert`.
- **Files modified:** `client/src/features/employees/pages/EmployeeCreatePage.tsx`, `client/src/features/employees/pages/EmployeeEditPage.tsx`, `client/src/features/parents/pages/ParentCreatePage.tsx`, `client/src/features/parents/pages/ParentEditPage.tsx`, `client/src/features/students/pages/StudentCreatePage.tsx`
- **Verification:** `cd client && npm run build`
- **Committed in:** `5f467b41`

**2. [Rule 3 - Blocking] Existing event form and linked-student helper typings no longer matched the generated contracts**
- **Found during:** Task 2 (Upgrade React Router types to a build-safe release and confirm the SPA build)
- **Issue:** The build surfaced a removed `title` field in `EventForm.tsx` and over-strict linked-student test typings.
- **Fix:** Removed the stale event title field and simplified the linked-student helper/test input types to the archived flag actually required by the helper.
- **Files modified:** `client/src/features/events/forms/EventForm.tsx`, `client/src/features/parents/utils/getActiveLinkedStudentsCount.ts`, `client/src/features/parents/utils/getActiveLinkedStudentsCount.test.ts`
- **Verification:** `cd client && npm run build`
- **Committed in:** `5f467b41`

---

**Total deviations:** 2 auto-fixed (2 blocking)
**Impact on plan:** Both fixes were required to reach the plan's explicit build-success outcome. No scope creep beyond restoring frontend shippability.

## Issues Encountered
- `npm install` and `npm run build` updated tracked files under `client/node_modules/` and `client/dist/`; those generated artifacts were reverted before committing so only source-controlled inputs were captured.

## User Setup Required
None - no external service configuration required.

## Next Phase Readiness
- Phase 2's final frontend shippability blocker is removed and the SPA now builds successfully.
- Remaining repo noise (`server/api-aprimorar/.classpath` and untracked planning artifacts from earlier work) was left untouched because it was outside this plan's scope.

## Self-Check: PASSED
- Found `02-student-parent-registry-hardening-07-SUMMARY.md`.
- Verified task commits `68ddcd25` and `5f467b41` exist.

---
*Phase: 02-student-parent-registry-hardening*
*Completed: 2026-04-20*
