# Epic: E-001 — Planning Structure Migration
**Goal:** Consolidate project, planning, and agent guidance into a stable docs structure.
**Status:** DONE
**Owner:** Gu
**Related milestone/phase:** Phase 0

## Scope
- In scope:
  - Establish canonical planning files under `docs/planning/`
  - Consolidate repo guidance into `../../AGENTS.md`
  - Normalize epic/task IDs and templates
- Out of scope:
  - Product feature changes

## Workboard
- Current focus: None
- Blocked: None
- Next up: Keep future epic updates aligned with the concise template

## Stories
### Story: S-001 — Canonical project guidance
**Status:** DONE
**Links:** T-001 (DONE)
**Intent:** Keep one canonical repository guide for agents and project context.

### Story: S-002 — Stable planning structure
**Status:** DONE
**Links:** T-002 (DONE), T-003 (DONE)
**Intent:** Keep roadmap and epic planning in dedicated files with stable IDs.

### Story: S-003 — Planning consistency pass
**Status:** DONE
**Links:** T-004 (DONE), T-005 (DONE)
**Intent:** Keep planning docs short, consistent, and executable.

## Tasks
- No active tasks.

## Archive (DONE)
### Task: T-001 — Consolidate project docs
- Status: DONE
- Scope: Moved product and agent guidance into canonical repository docs, later finalized in `../../AGENTS.md`.
- Verification: Manual markdown review and legacy spot checks.

### Task: T-002 — Create roadmap and epic index
- Status: DONE
- Scope: Created `docs/planning/roadmap.md` and phase/epic indexing.
- Verification: Manual link and phase validation.

### Task: T-003 — Split legacy planning into epic files
- Status: DONE
- Scope: Created `E-002` through `E-006` with stable IDs.
- Verification: Manual spot checks against migrated planning notes.

### Task: T-004 — Normalize planning template usage
- Status: DONE
- Scope: Standardized epic structure and concise planning fields.
- Verification: Manual template compliance review.

### Task: T-005 — Normalize duplicate task IDs
- Status: DONE
- Scope: Removed duplicated IDs across epic files.
- Verification: Manual grep-based uniqueness check.

## Review Notes (append-only)
- 2026-03-08:
  - Quality: Canonical guidance now lives in `../../AGENTS.md`; legacy archive and project hubs were removed.
  - Security: No runtime impact.
  - Performance: No runtime impact.
