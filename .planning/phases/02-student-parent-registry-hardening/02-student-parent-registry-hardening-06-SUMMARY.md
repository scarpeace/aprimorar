---
phase: 02-student-parent-registry-hardening
plan: 06
subsystem: ui
tags: [react, typescript, vite, tanstack-query, responsible, parents, tdd]
requires:
  - phase: 02-04
    provides: responsible detail and destructive-action UX baseline
provides:
  - active-only linked-student blocker semantics for responsible deletion
  - responsible detail guidance that separates active links from archived history
  - lightweight regression coverage for archived-only linked student history
affects: [phase-02-ui, responsible-crud, student-parent-linking]
tech-stack:
  added: []
  patterns:
    - fetch linked-student totals separately from paginated tables when destructive guidance must consider the full relationship set
    - derive active blockers from archivedAt instead of total linked-record counts
key-files:
  created:
    - client/src/features/parents/utils/getActiveLinkedStudentsCount.ts
    - client/src/features/parents/utils/getActiveLinkedStudentsCount.test.ts
  modified:
    - client/src/features/parents/pages/ParentDetailPage.tsx
    - client/src/features/parents/components/DeleteParentButton.tsx
    - client/src/components/ui/delete-confirmation-modal.tsx
key-decisions:
  - "Use a full linked-student summary query for blocker calculations so delete guidance is based on the real relationship set, not only the visible table page."
  - "Keep archived student history visible in responsible detail and delete flows while reserving destructive blockers for active links only."
patterns-established:
  - "Responsible destructive actions should compute integrity blockers with a shared active-link helper."
  - "Detail-page relationship copy should distinguish active operational links from archived historical records."
requirements-completed: [PARN-03, PARN-04]
duration: 4 min
completed: 2026-04-20
---

# Phase 2 Plan 6: Responsável Active-Link Delete Guidance Summary

**Responsável detail and delete UX now counts only active linked students while preserving archived history visibility and regression coverage.**

## Performance

- **Duration:** 4 min
- **Started:** 2026-04-20T12:36:54Z
- **Completed:** 2026-04-20T12:41:07Z
- **Tasks:** 2
- **Files modified:** 5

## Accomplishments
- Added a tiny shared utility plus tsx regression coverage for active-versus-archived linked student counting.
- Reworked the responsável detail page so the lifecycle copy distinguishes active vínculos from archived-only history.
- Updated responsible deletion guidance so archived-only student history informs the user without blocking exclusion.

## Task Commits

Each task was committed atomically:

1. **Task 1: Add automated coverage for active-versus-archived linked student counting** - `c9c6fe97` (test), `2ce1d1ec` (feat)
2. **Task 2: Rewire responsável detail and delete guidance around active links only** - `cf4a95ef` (feat), `95ff45c0` (refactor)

## Files Created/Modified
- `client/src/features/parents/utils/getActiveLinkedStudentsCount.ts` - shared helper that counts only non-archived linked students.
- `client/src/features/parents/utils/getActiveLinkedStudentsCount.test.ts` - tsx regression tests for archived-only, mixed, and empty linked-student payloads.
- `client/src/features/parents/pages/ParentDetailPage.tsx` - active-versus-archived summary copy plus full-set linked-student blocker query.
- `client/src/features/parents/components/DeleteParentButton.tsx` - active-only blocker semantics and archived-history delete messaging.
- `client/src/components/ui/delete-confirmation-modal.tsx` - customizable linked-record label so responsible deletion copy references alunos instead of eventos.

## Decisions Made
- Used a separate full-set linked-student summary query so destructive guidance is not limited by the paginated table view.
- Kept archived linked students visible as historical context instead of hiding them, while treating only active links as integrity blockers.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 1 - Bug] Corrected shared delete modal linked-record wording**
- **Found during:** Task 2 (Rewire responsável detail and delete guidance around active links only)
- **Issue:** The shared delete confirmation modal hardcoded `evento(s)` for linked records, which made the responsible deletion copy inaccurate even after the blocker logic was fixed.
- **Fix:** Added a customizable linked-record label and passed `aluno(s)` from the responsible delete flow.
- **Files modified:** `client/src/components/ui/delete-confirmation-modal.tsx`, `client/src/features/parents/components/DeleteParentButton.tsx`
- **Verification:** `cd client && npx tsx --test src/features/parents/utils/getActiveLinkedStudentsCount.test.ts && npx eslint src/features/parents/pages/ParentDetailPage.tsx src/features/parents/components/DeleteParentButton.tsx src/features/parents/utils/getActiveLinkedStudentsCount.ts src/features/parents/utils/getActiveLinkedStudentsCount.test.ts src/components/ui/delete-confirmation-modal.tsx`
- **Committed in:** `cf4a95ef`

---

**Total deviations:** 1 auto-fixed (1 bug)
**Impact on plan:** The auto-fix stayed inside the responsible delete flow and made the new guidance accurate instead of partially misleading.

## Issues Encountered

None.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness
- Responsável detail/delete UX now matches the backend active-link integrity rule verified earlier in Phase 2.
- Phase 2 still needs the remaining frontend build blockers resolved in Plan 02-07 for full operational readiness.

## Known Stubs

None.

## Self-Check: PASSED

- FOUND: `.planning/phases/02-student-parent-registry-hardening/02-student-parent-registry-hardening-06-SUMMARY.md`
- FOUND: `c9c6fe97`
- FOUND: `2ce1d1ec`
- FOUND: `cf4a95ef`
- FOUND: `95ff45c0`
